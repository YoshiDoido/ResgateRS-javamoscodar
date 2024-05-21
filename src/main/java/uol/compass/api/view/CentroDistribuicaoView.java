package uol.compass.api.view;

import uol.compass.api.controller.CentroDistribuicaoController;
import uol.compass.api.exception.OperacaoInvalidaException;
import uol.compass.domain.exception.CategoriaLimiteMaximoException;
import uol.compass.domain.exception.CentroDeDistribuicaoNaoEncontradoException;
import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.model.Produto;
import uol.compass.domain.service.CentroDistribuicaoService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CentroDistribuicaoView implements TableView {

    private static final int LIST_ALL_CENTROS_DISTRIBUICAO = 1;
    private static final int GET_CENTRO_DISTRIBUICAO_BY_ID = 2;
    private static final int SAVE_CENTRO_DISTRIBUICAO = 3;
    public static final int UPDATE_CENTRO_DISTRIBUICAO = 4;
    public static final int DELETE_CENTRO_DISTRIBUICAO = 5;

    private static final int TAMANHO_CEP = 9;

    public static final Scanner SCANNER = new Scanner(System.in);

    private final CentroDistribuicaoController centroDistribuicaoController = new CentroDistribuicaoController();
    private final CentroDistribuicaoService centroDistribuicaoService = new CentroDistribuicaoService();

    private void showOperations() {
        System.out.println();
        System.out.println("---------------------------------------------------------------");
//        System.out.println("[0] - Inicio");
//        System.out.println();
        System.out.println("[1] - Mostrar todos Centros de Distribuição");
        System.out.println("[2] - Encontrar um Centro de Distribuição por ID");
        System.out.println("[3] - Salvar um Centro de Distribuição");
        System.out.println("[4] - Atualizar um Centro de Distribuição");
        System.out.println("[5] - Apagar um Centro de Distribuição");
        System.out.println("[6] - Inserir uma nova doação");
        System.out.println("[7] - Mostrar todas doações de um Centro de Distribuição");
        System.out.println("---------------------------------------------------------------");
    }


    @Override
    public void readOperation() {
        showOperations();
        while (true) {
            try {
                System.out.print("Escolha uma das operações: ");
                int intUserInput = SCANNER.nextInt();
                getOperation(intUserInput);
                break;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
                System.out.println();
            } catch (OperacaoInvalidaException e) {
                SCANNER.nextLine();
                System.out.printf("\n%s. Por favor, tente novamente.\n", e.getMessage());
                System.out.println();
            }
        }
    }


    private void getOperation(int intUserInput) {
        switch (intUserInput) {
            case LIST_ALL_CENTROS_DISTRIBUICAO -> getCentrosDistribuicao();
            case GET_CENTRO_DISTRIBUICAO_BY_ID -> readCentroDistribuicaoId();
            case SAVE_CENTRO_DISTRIBUICAO -> saveNewCentroDistribuicao();
            case UPDATE_CENTRO_DISTRIBUICAO -> updateCentroDistribuicao();
            case DELETE_CENTRO_DISTRIBUICAO -> deleteCentroDistribuicaoById();
            case 6 -> inserirDoacao();
            case 7 -> getCentroDistribuicaoDoacoes();
            default -> throw new OperacaoInvalidaException(intUserInput);
        }
    }

    private void getCentroDistribuicaoDoacoes() {
        System.out.println();
        while (true) {
            try {
                System.out.print("ID do Centro de Distribuição: ");
                var id = SCANNER.nextInt();
                centroDistribuicaoService.findByIdOrException(id);
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
        System.out.println();
        try {
            System.out.print("ID do Centro de Distribuição em que a doação será feita: ");
            var id = SCANNER.nextInt();
            centroDistribuicaoService.findByIdOrException(id);
            SCANNER.nextLine();
            System.out.println();

            var produto = new Produto();
            produto.setCentroDistribuicaoId(id);
            produto.setCategoria(validateProdutoCategoria());
            produto.setItem(validateProdutoItem(produto.getCategoria()));
            if (produto.getCategoria().equals(Produto.Categoria.ROUPA)) {
                produto.setSexo(validateProdutoSexo());
                produto.setTamanho(validateProdutoTamanho());
            }
            produto.setQuantidade(validateProdutoQuantidade());
            Produto produtoSalvo = centroDistribuicaoService.inserirDoacao(id, produto);
            System.out.println(produtoSalvo);
        } catch (InputMismatchException e) {
            SCANNER.nextLine();
            System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
        } catch (CentroDeDistribuicaoNaoEncontradoException | CategoriaLimiteMaximoException e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    private Integer validateProdutoQuantidade() {
        int quantidade;
        while (true) {
            System.out.print("Quantidade: ");
            try {
                quantidade = SCANNER.nextInt();
                if (quantidade > 0 && quantidade <= 1000) {
                    break;
                }
                System.out.println("Quantidade deve ser um valor entre 1 até 1000");
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }

        return quantidade;
    }

    private Produto.Tamanho validateProdutoTamanho() {
        Produto.Tamanho tamanho;
        while (true) {
            System.out.print("Tamanho [INFANTIL / PP / P / M / G / GG]: ");
            String tamanhoInput = SCANNER.nextLine().toUpperCase();
            try {
                tamanho = Produto.Tamanho.valueOf(tamanhoInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Item inválido. Tente novamente.");
            }
        }
        return tamanho;
    }

    private Produto.Item validateProdutoItem(Produto.Categoria categoria) {
        Produto.Item item;
        while (true) {
            printProdutoItem(categoria);
            String itemInput = SCANNER.nextLine().toUpperCase();
            try {
                item = Produto.Item.valueOf(itemInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Item inválido. Tente novamente.");
            }
        }
        return item;
    }

    private void printProdutoItem(Produto.Categoria categoria) {
        if (categoria.equals(Produto.Categoria.ROUPA)) {
            System.out.print("Item [AGASALHO / CAMISA]: ");
        } else if (categoria.equals(Produto.Categoria.HIGIENE)) {
            System.out.print("Item [ESCOVA_DE_DENTES / PASTA_DE_DENTES / ABSORVENTE]: ");
        } else  {
            System.out.print("Item [ARROZ / FEIJAO / LEITE]: ");
        }
    }

    private Produto.Sexo validateProdutoSexo() {
        Produto.Sexo sexo;
        while (true) {
            System.out.print("Sexo [F / M]: ");
            String sexoInput = SCANNER.nextLine().toUpperCase();
            try {
                sexo = Produto.Sexo.valueOf(sexoInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Sexo inválido. Tente novamente.");
            }
        }

        return sexo;
    }

    private Produto.Categoria validateProdutoCategoria() {
        Produto.Categoria categoria;
        while (true) {
            System.out.print("Categoria [ROUPA, HIGIENE, ALIMENTO]: ");
            String categoriaInput = SCANNER.nextLine().toUpperCase();
            try {
                categoria = Produto.Categoria.valueOf(categoriaInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Categoria inválida. Tente novamente.");
            }
        }
        return categoria;
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
                    newCentroDistribuicao.setNome(validateNome());
                    newCentroDistribuicao.setEndereco(validateEndereco());
                    newCentroDistribuicao.setCep(validateCEP());

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
        centroDistribuicao.setNome(validateNome());
        centroDistribuicao.setEndereco(validateEndereco());
        centroDistribuicao.setCep(validateCEP());

//        CentroDistribuicao savedCentroDistribuicao = centroDistribuicaoController.saveCentroDistribuicao(centroDistribuicao);
        CentroDistribuicao savedCentroDistribuicao = centroDistribuicaoService.save(centroDistribuicao);

        System.out.println(savedCentroDistribuicao);
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
            System.out.println("Nome não pode ser nulo ou vazio. Tente novamente.");
            System.out.println();
        }

        return nome;
    }


    private String validateCEP() {
        String cep;
        while (true) {
            System.out.print("CEP: ");
            cep = SCANNER.nextLine();
            if ((cep != null) && !(cep.isBlank()) && (cep.length() == TAMANHO_CEP)) {
                break;
            }
            System.out.println();
            System.out.println("Cep não pode ser nulo ou vazio e deve conter 9 caracteres. Tente novamente.");
            System.out.println();
        }

        return cep;
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
            System.out.println("Endereco não pode ser nulo ou vazio. Tente novamente.");
            System.out.println();
        }

        return endereco;
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
