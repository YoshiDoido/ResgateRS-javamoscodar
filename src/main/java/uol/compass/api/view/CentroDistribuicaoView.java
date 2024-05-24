package uol.compass.api.view;

import uol.compass.api.exception.OperacaoInvalidaException;
import uol.compass.domain.dto.OrdemPedidoHistorico;
import uol.compass.domain.dto.OrdemPedidoInputStatus;
import uol.compass.api.util.Validate;
import uol.compass.domain.exception.*;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Doacao;
import uol.compass.domain.model.OrdemPedido;
import uol.compass.domain.service.CentroDistribuicaoService;
import uol.compass.domain.service.OrdemPedidoService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CentroDistribuicaoView implements TableView {

    private static final int LIST_ALL_CENTROS_DISTRIBUICAO = 1;
    private static final int GET_CENTRO_DISTRIBUICAO_BY_ID = 2;
    private static final int SAVE_CENTRO_DISTRIBUICAO = 3;
    public static final int UPDATE_CENTRO_DISTRIBUICAO = 4;
    public static final int DELETE_CENTRO_DISTRIBUICAO = 5;
    public static final int INSERIR_DOACAO = 6;
    public static final int GET_CENTRO_DISTRIBUICAO_DOACOES = 7;
    public static final int GET_CENTRO_DISTRIBUICAO_ORDENS_PEDIDO = 8;
    public static final int CENTRO_DISTRIBUICAO_HISTORICO_ORDENS_PEDIDO = 9;

    public static final int TAMANHO_CEP = 9;

    public static final Scanner SCANNER = new Scanner(System.in);

    private final CentroDistribuicaoService centroDistribuicaoService = new CentroDistribuicaoService();
    private final OrdemPedidoService ordemPedidoService = new OrdemPedidoService();

    public void showOperations() {
        System.out.println();
        System.out.println("---------------------------------------------------------------");
        System.out.println("[1] - Mostrar todos Centros de Distribuição");
        System.out.println("[2] - Encontrar um Centro de Distribuição por ID");
        System.out.println("[3] - Salvar um Centro de Distribuição");
        System.out.println("[4] - Atualizar um Centro de Distribuição");
        System.out.println("[5] - Apagar um Centro de Distribuição");
        System.out.println();
        System.out.println("[6] - Inserir uma nova doação");
        System.out.println("[7] - Mostrar todas doações de um Centro de Distribuição");
        System.out.println("[8] - Mostrar Ordens de Pedido de um Centro de Distribuição");
        System.out.println("[9] - Mostrar Histórico de Ordens de Pedido de um Centro de Distribuição");
        System.out.println("---------------------------------------------------------------");
    }


    public void getOperation(int intUserInput) {
        switch (intUserInput) {
            case LIST_ALL_CENTROS_DISTRIBUICAO -> getCentrosDistribuicao();
            case GET_CENTRO_DISTRIBUICAO_BY_ID -> readCentroDistribuicaoId();
            case SAVE_CENTRO_DISTRIBUICAO -> saveNewCentroDistribuicao();
            case UPDATE_CENTRO_DISTRIBUICAO -> updateCentroDistribuicao();
            case DELETE_CENTRO_DISTRIBUICAO -> deleteCentroDistribuicaoById();
            case INSERIR_DOACAO -> inserirDoacao();
            case GET_CENTRO_DISTRIBUICAO_DOACOES -> getCentroDistribuicaoDoacoes();
            case GET_CENTRO_DISTRIBUICAO_ORDENS_PEDIDO -> getCentroDistribuicaoOrdensPedido();
            case CENTRO_DISTRIBUICAO_HISTORICO_ORDENS_PEDIDO -> getCentroDistribuicaoHistoricoOrdensPedido();
            default -> throw new OperacaoInvalidaException(intUserInput);
        }
    }

    private void getCentroDistribuicaoHistoricoOrdensPedido() {
        System.out.println();
        try {
            int id = Validate.validateIntegerInput(SCANNER, "ID do Centro de Distribuição: ");
            centroDistribuicaoService.findByIdOrException(id);
            System.out.println();
            centroDistribuicaoService.getCentroDistribuicaoOrdensPedido(id, OrdemPedidoHistorico.HISTORICO)
                    .forEach(System.out::println);
        } catch (CentroDeDistribuicaoNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getCentroDistribuicaoOrdensPedido() {
        try {
            System.out.println();
            int idCentroDeDistribuicao = Validate.validateIntegerInput(SCANNER, "ID do Centro de Distribuição: ");
            var centroDistribuicao = centroDistribuicaoService.findByIdOrException(idCentroDeDistribuicao);
            int centroId = centroDistribuicao.getId();
            List<OrdemPedido> centroDistribuicaoOrdensPedido;
            centroDistribuicaoOrdensPedido = centroDistribuicaoService.getCentroDistribuicaoOrdensPedido(centroId, OrdemPedidoHistorico.PENDENTE);
            System.out.println();
            if (centroDistribuicaoOrdensPedido.isEmpty()) {
                System.out.println("Este Centro de Distribuição não possui nenhuma Ordem de Pedido pendente.");
                return;
            }
            centroDistribuicaoOrdensPedido.forEach(System.out::println);

            System.out.println();
            String message = "Acatar a Ordem de Pedido feita por um dos abrigos? [true/false]: ";
            if (!Validate.validateBooleanInput(SCANNER, message)) {
                return;
            }

            System.out.println();
            message = "ID da Ordem de Pedido: ";
            OrdemPedido ordemPedido = ordemPedidoService.findByIdOrException(Validate.validateIntegerInput(SCANNER, message));
            SCANNER.nextLine();
            System.out.println();
            OrdemPedidoInputStatus inputStatus = Validate.validateOrdemPedidoStatus(SCANNER);
            ordemPedido.setStatus(inputStatus.getStatus());
            if (inputStatus.equals(OrdemPedidoInputStatus.RECUSAR)) {
                String errorMessage = "Motivo da recusa não pode ser nulo ou em branco. Tente novamente";
                ordemPedido.setMotivo(Validate.validateString(SCANNER, "Motivo da Recusa: ", errorMessage));
            }

            ordemPedidoService.efetuarTransferenciaOrdemPedido(ordemPedido);


        } catch (CentroDeDistribuicaoNaoEncontradoException | OrdemPedidoNaoEncontradaException |
                 AbrigoNaoEncontradoException | DoacaoNaoEncontradaException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getCentroDistribuicaoDoacoes() {
        System.out.println();
        while (true) {
            try {
                System.out.print("ID do Centro de Distribuição: ");
                var id = SCANNER.nextInt();
                System.out.println("\nQuantidade de cada Categoria: ");
                centroDistribuicaoService.centroDistribuicaoTotalDoacoes(id);
                centroDistribuicaoService.listAllProdutosCentroDistribuicao(id).forEach(System.out::println);
                break;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            } catch (CentroDeDistribuicaoNaoEncontradoException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }

    private void inserirDoacao() {
        try {
            System.out.println();
            String message = "ID do Centro de Distribuição em que a doação será feita: ";
            var id = Validate.validateIntegerInput(SCANNER, message);
            centroDistribuicaoService.findByIdOrException(id);
            SCANNER.nextLine();
            System.out.println();

            var produto = new Doacao();
            produto.setArmazemId(centroDistribuicaoService.getCentroDistribuicaoArmazemId(id));
            produto.setCategoria(Validate.validateProdutoCategoria(SCANNER));

            centroDistribuicaoService.verificarTotalCategoriaCentroDistribuicao(id, produto.getCategoria());

            produto.setItem(Validate.validateProdutoItem(SCANNER, produto.getCategoria()));
            if (produto.getCategoria().equals(Doacao.Categoria.ROUPA)) {
                produto.setSexo(Validate.validateProdutoSexo(SCANNER));
                produto.setTamanho(Validate.validateProdutoTamanho(SCANNER));
            }
            produto.setQuantidade(Validate.validateIntegerInRange(SCANNER, "Quantidade: ", 1, 1000));
            Doacao doacaoSalvo = centroDistribuicaoService.inserirDoacao(id, produto);
            System.out.println(doacaoSalvo);
        } catch (InputMismatchException e) {
            SCANNER.nextLine();
            System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
        } catch (CentroDeDistribuicaoNaoEncontradoException | CategoriaLimiteMaximoException e) {
            System.out.println("\n" + e.getMessage());
        }
    }


    private void updateCentroDistribuicao() {
        System.out.println();
        while (true) {
            try {
                System.out.print("ID do Centro de Distribuição que deseja atualizar: ");
                var id = SCANNER.nextInt();
                SCANNER.nextLine();
                System.out.println();
                try {
                    var centroDistribuicao = centroDistribuicaoService.findByIdOrException(id);

                    var newCentroDistribuicao = new CentroDistribuicao();
                    String nomeErrorMessage = "Nome do Centro de Distribuicao não pode ser nulo ou vazio. Tente novamente.";
                    String enderecoErrorMessage = "Endereço do Centro de Distribuicao não pode ser nulo ou vazio. Tente novamente.";
                    newCentroDistribuicao.setNome(Validate.validateString(SCANNER, "Nome: ", nomeErrorMessage));
                    newCentroDistribuicao.setEndereco(Validate.validateString(SCANNER, "Endereço: ", enderecoErrorMessage));
                    newCentroDistribuicao.setCep(Validate.validateCEP(SCANNER));

                    centroDistribuicao = centroDistribuicaoService.update(centroDistribuicao.getId(), newCentroDistribuicao);
                    System.out.println("\n" + centroDistribuicao);
                } catch (CentroDeDistribuicaoNaoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
                break;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }
    }

    private void deleteCentroDistribuicaoById() {
        SCANNER.nextLine();
        System.out.println();
        while (true) {
            try {
                System.out.print("ID do Centro de Distribuição que deseja apagar: ");
                var id = SCANNER.nextInt();
//                centroDistribuicaoController.deleteCentroDistribuicaoById(id);
                centroDistribuicaoService.deleteById(id);
                break;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
                System.out.println();
            }
        }
    }


    private void saveNewCentroDistribuicao() {
        SCANNER.nextLine();
        System.out.println();
        var centroDistribuicao = new CentroDistribuicao();
        String nomeErrorMessage = "Nome do Centro de Distribuicao não pode ser nulo ou vazio. Tente novamente.";
        String enderecoErrorMessage = "Endereço do Centro de Distribuicao não pode ser nulo ou vazio. Tente novamente.";
        centroDistribuicao.setNome(Validate.validateString(SCANNER, "Nome: ", nomeErrorMessage));
        centroDistribuicao.setEndereco(Validate.validateString(SCANNER, "Endereço: ", enderecoErrorMessage));
        centroDistribuicao.setCep(Validate.validateCEP(SCANNER));

//        CentroDistribuicao savedCentroDistribuicao = centroDistribuicaoController.saveCentroDistribuicao(centroDistribuicao);
        CentroDistribuicao savedCentroDistribuicao = centroDistribuicaoService.save(centroDistribuicao);

        System.out.println(savedCentroDistribuicao);
    }


    private void readCentroDistribuicaoId() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println();
                System.out.print("ID do Centro de Distribuição: ");
                var id = scanner.nextInt();
                try {
                    var centroDistribuicao = centroDistribuicaoService.findByIdOrException(id);
                    System.out.println("\n" + centroDistribuicao);
                } catch (CentroDeDistribuicaoNaoEncontradoException e) {
                    System.out.println("\n" + e.getMessage());
                }
                break;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }
    }


    private void getCentrosDistribuicao() {
        System.out.println();
        centroDistribuicaoService.findAll().forEach(System.out::println);
    }

}
