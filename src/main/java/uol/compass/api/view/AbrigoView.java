package uol.compass.api.view;

import uol.compass.api.exception.OperacaoInvalidaException;
import uol.compass.api.util.Validate;
import uol.compass.domain.dao.DoacaoDAO;
import uol.compass.domain.exception.AbrigoNaoEncontradoException;
import uol.compass.domain.exception.CategoriaLimiteMaximoException;
import uol.compass.domain.exception.CentroDeDistribuicaoNaoEncontradoException;
import uol.compass.domain.model.Abrigo;
import uol.compass.domain.dto.AbrigoNecessidades;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.OrdemPedido;
import uol.compass.domain.service.AbrigoService;
import uol.compass.domain.service.CentroDistribuicaoService;
import uol.compass.domain.service.OrdemPedidoService;
import uol.compass.infrastructure.dao_implementation.DoacaoDAOImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AbrigoView implements TableView {

    public static final int LIST_ALL_ABRIGOS = 1;
    public static final int GET_ABRIGO_BY_ID = 2;
    public static final int SAVE_ABRIGO = 3;
    public static final int UPDATE_ABRIGO = 4;
    public static final int DELETE_ABRIGO = 5;
    public static final int LISTAR_NECESSIDADES = 6;
    public static final int LIST_DOACOES = 7;

    private static final int TAMANHO_CEP = 9;

    public static final Scanner SCANNER = new Scanner(System.in);

    private final AbrigoService abrigoService = new AbrigoService();
    private final CentroDistribuicaoService centroDistribuicaoService = new CentroDistribuicaoService();
    private final OrdemPedidoService ordemPedidoService = new OrdemPedidoService();

    @Override
    public void showOperations() {
        System.out.println();
        System.out.println("----------------------------------------------------");
        System.out.println("[1] - Mostrar todos Abrigos");
        System.out.println("[2] - Encontrar um Abrigo por ID");
        System.out.println("[3] - Salvar um Abrigo");
        System.out.println("[4] - Atualizar um Abrigo");
        System.out.println("[5] - Apagar um Abrigo");
        System.out.println();
        System.out.println("[6] - Listar necessidades");
        System.out.println("[7] - Mostrar doações de um Abrigo");
        System.out.println("[8] - Ordens de Pedido feitas por um Abrigo");
        System.out.println("----------------------------------------------------");
    }


    public void getOperation(int intUserInput) throws OperacaoInvalidaException {
        switch (intUserInput) {
            case LIST_ALL_ABRIGOS -> getAbrigo();
            case GET_ABRIGO_BY_ID -> readAbrigoId();
            case SAVE_ABRIGO -> saveNewAbrigo();
            case UPDATE_ABRIGO -> updateAbrigo();
            case DELETE_ABRIGO -> deleteAbrigoById();
            case LISTAR_NECESSIDADES -> listarNecessidades();
            case LIST_DOACOES -> getAbrigoDoacoes();
            case 8 -> getAllAbrigoOrdemPedidos();
            default -> throw new OperacaoInvalidaException(intUserInput);
        }
    }

    public void getAbrigoDoacoes() {
        System.out.println();
        while (true) {
            try {
                System.out.print("ID do Abrigo: ");
                var id = SCANNER.nextInt();
                SCANNER.nextLine();
                System.out.println();
                try {
                    var doacoes = abrigoService.getAllDoacoesAbrigo(id);
                    if (doacoes.isEmpty()) {
                        System.out.println("Nenhuma doação encontrada para o abrigo de ID " + id);
                    } else {
                        System.out.println("\nQuantidade de cada Categoria: ");
                        abrigoService.abrigoTotalDoacoes(id);
                        System.out.println();
                        doacoes.forEach(System.out::println);
                    }
                    break;
                } catch (AbrigoNaoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }
    }


    private void getAllAbrigoOrdemPedidos() {
        System.out.println();
        try {
            var abrigoId = abrigoService.findByIdOrException(Validate.validateIntegerInput(SCANNER,"ID do Abrigo: "));
            System.out.println();
            abrigoService.listAbrigoOrdensPedido(abrigoId.getId()).forEach(System.out::println);
        } catch (AbrigoNaoEncontradoException e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    private void listarNecessidades() {
        Abrigo abrigo;
        try {
//            SCANNER.nextLine();
            System.out.println();
            abrigo = abrigoService.findByIdOrException(Validate.validateIntegerInput(SCANNER, "ID do Abrigo: "));
            System.out.println();
            var abrigoNecessidades = new AbrigoNecessidades();
            SCANNER.nextLine();
            abrigoNecessidades.setCategoria(Validate.validateProdutoCategoria(SCANNER));

            abrigoService.verificarTotalCategoriaAbrigo(abrigo.getId(), abrigoNecessidades.getCategoria());

            abrigoNecessidades.setItem(Validate.validateProdutoItem(SCANNER, abrigoNecessidades.getCategoria()));
            abrigoNecessidades.setQuantidade(Validate.validateIntegerInRange(SCANNER, "Quantidade: ", 1, 200));
            System.out.println();
            var centroslist = abrigoService.listarNecessidades(abrigoNecessidades);
            if (centroslist.isEmpty()) {
                System.out.println("Nenhum Centro de Distribuicao pode atender as necessidades no momento. Tente novamente mais tarde.");
                return;
            }
            centroslist.forEach(System.out::println);
            System.out.println();
            SCANNER.nextLine();
            if (!Validate.solicitarOrdemPedido(SCANNER)) {
                return;
            }
            SCANNER.nextLine();
            fazerOrdemPedido(abrigo, abrigoNecessidades);
        } catch (AbrigoNaoEncontradoException | CategoriaLimiteMaximoException e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    private void fazerOrdemPedido(Abrigo abrigo, AbrigoNecessidades abrigoNecessidades) {
        int centroDistribuicaoId = centroDistribuicaoIdOrdemPedido();
        CentroDistribuicao centroDistribuicao;
        try {
            centroDistribuicao = centroDistribuicaoService.findByIdOrException(centroDistribuicaoId);
        } catch (CentroDeDistribuicaoNaoEncontradoException e) {
            System.out.println(e.getMessage());
            return;
        }
        var ordemPedido = new OrdemPedido();
        ordemPedido.setAbrigoId(abrigo.getId());
        ordemPedido.setCentroDistribuicaoId(centroDistribuicao.getId());
        ordemPedido.setItem(abrigoNecessidades.getItem());
        ordemPedido.setQuantidade(abrigoNecessidades.getQuantidade());
        ordemPedido.setCategoria(abrigoNecessidades.getCategoria());
        System.out.println("\n" + ordemPedidoService.save(ordemPedido));
    }


    private int centroDistribuicaoIdOrdemPedido() {
        System.out.println();
        while (true) {
            try {
                System.out.print("ID do Centro de Distribuição que deseja fazer o pedido: ");
                return SCANNER.nextInt();
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }
    }


    public void getAbrigo() {
        System.out.println();
        abrigoService.findAll().forEach(System.out::println);
    }

    public void updateAbrigo() {
        System.out.println();
        while (true) {
            try {
                System.out.print("ID do Abrigo que deseja atualizar: ");
                var id = SCANNER.nextInt();
                SCANNER.nextLine();
                System.out.println();
                try {
                    var abrigo = abrigoService.findByIdOrException(id);

                    var newAbrigo = new Abrigo();
                    newAbrigo.setNome(validateNome());
                    newAbrigo.setEndereco(validateEndereco());
                    newAbrigo.setCep(validateCep());
                    newAbrigo.setCidade(validateCidade());
                    newAbrigo.setResponsavel(validateResponsavel());
                    newAbrigo.setTelefone(validateTelefone());
                    newAbrigo.setEmail(validateEmail());
                    newAbrigo.setCapacidade(validateCapacidade());
                    newAbrigo.setOcupacao(validateOcupacao());

                    abrigo = abrigoService.update(abrigo.getId(), newAbrigo);
                    System.out.println("\n" + abrigo);
                } catch (AbrigoNaoEncontradoException e) {
                    System.out.println(e.getMessage());
                }

                break;

            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }
    }

    private void deleteAbrigoById() {
        SCANNER.nextLine();
        System.out.println();
        while (true) {
            try {
                System.out.print("ID do Abrigo que deseja apagar: ");
                var id = SCANNER.nextInt();
                abrigoService.deleteById(id);
                break;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
                System.out.println();
            }
        }
    }

    private void saveNewAbrigo() {
        SCANNER.nextLine();
        System.out.println();
        var abrigo = new Abrigo();
        abrigo.setNome(validateNome());
        abrigo.setEndereco(validateEndereco());
        abrigo.setCep(validateCep());
        abrigo.setCidade(validateCidade());
        abrigo.setResponsavel(validateResponsavel());
        abrigo.setTelefone(validateTelefone());
        abrigo.setEmail(validateEmail());
        abrigo.setCapacidade(200);
        abrigo.setOcupacao(0);
        Abrigo savedAbrigo = abrigoService.save(abrigo);
        System.out.println(savedAbrigo);
    }

    private String validateNome() {
        String nome;
        while (true) {
            System.out.print("Nome: ");
            nome = SCANNER.nextLine();
            if (nome != null && !(nome.isBlank())) {
                break;
            }
            System.out.println();
            System.out.println("Nome não pode ser vazio. Por favor, tente novamente.");
            System.out.println();
        }

        return nome;
    }

    private String validateEndereco() {
        String endereco;
        while (true) {
            System.out.print("Endereço: ");
            endereco = SCANNER.nextLine();
            if (endereco != null && !(endereco.isBlank())) {
                break;
            }
            System.out.println();
            System.out.println("Endereço não pode ser vazio. Por favor, tente novamente.");
            System.out.println();
        }

        return endereco;
    }

    private String validateCep() {
        String cep;
        while (true) {
            System.out.print("CEP: ");
            cep = SCANNER.nextLine();
            if (cep != null && !(cep.isBlank()) && cep.length() == TAMANHO_CEP) {
                break;
            }
            System.out.println();
            System.out.println("CEP não pode ser vazio e deve conter 9 caracteres. Por favor, tente novamente.");
            System.out.println();
        }

        return cep;
    }

    private String validateCidade() {
        String cidade;
        while (true) {
            System.out.print("Cidade: ");
            cidade = SCANNER.nextLine();
            if (cidade != null && !(cidade.isBlank())) {
                break;
            }
            System.out.println();
            System.out.println("Cidade não pode ser vazia. Por favor, tente novamente.");
            System.out.println();
        }

        return cidade;
    }

    private String validateResponsavel() {
        String responsavel;
        while (true) {
            System.out.print("Responsável: ");
            responsavel = SCANNER.nextLine();
            if (responsavel != null && !(responsavel.isBlank())) {
                break;
            }
            System.out.println();
            System.out.println("Responsável não pode ser vazio. Por favor, tente novamente.");
            System.out.println();
        }

        return responsavel;
    }

    private String validateTelefone() {
        String telefone;
        while (true) {
            System.out.print("Telefone: ");
            telefone = SCANNER.nextLine();
            if (telefone != null && !(telefone.isBlank())) {
                break;
            }
            System.out.println();
            System.out.println("Telefone não pode ser vazio. Por favor, tente novamente.");
            System.out.println();
        }

        return telefone;
    }

    private String validateEmail() {
        String email;
        while (true) {
            System.out.print("Email: ");
            email = SCANNER.nextLine();
            if (email != null && !(email.isBlank())) {
                break;
            }
            System.out.println();
            System.out.println("Email não pode ser vazio. Por favor, tente novamente.");
            System.out.println();
        }

        return email;
    }

    private int validateCapacidade() {
        int capacidade;
        while (true) {
            System.out.print("Capacidade: ");
            try {

                capacidade = SCANNER.nextInt();
                if (capacidade > 0) {
                    break;
                }
                System.out.println();
                System.out.println("Capacidade não pode ser vazia e deve ser maior que 0. Por favor, tente novamente.");
                System.out.println();
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }

        return capacidade;
    }

    private int validateOcupacao() {
        int ocupacao;
        while (true) {
            System.out.print("Ocupação: ");
            try {
                ocupacao = SCANNER.nextInt();
                if (ocupacao >= 0 && ocupacao <= 100) {
                    break;
                }
                System.out.println();
                System.out.println("Ocupação não pode ser vazia e deve ser um valor entre 0 e 100. Por favor, tente novamente.");
                System.out.println();
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }

        return ocupacao;
    }

    public void readAbrigoId() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println();
                System.out.print("ID do Abrigo: ");
                var id = scanner.nextInt();
                try {
                    var abrigo = abrigoService.findByIdOrException(id);
                    System.out.printf("\nEstoque do abrigo %s: \n", abrigo.getNome());
                    abrigoService.abrigoTotalDoacoes(abrigo.getId());
                    System.out.println("\n" + abrigo);
                } catch (AbrigoNaoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
                break;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }

    }
}
