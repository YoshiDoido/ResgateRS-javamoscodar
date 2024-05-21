package uol.compass.api.view;

import uol.compass.api.controller.AbrigoController;
import uol.compass.api.exception.OperacaoInvalidaException;
import uol.compass.domain.exception.AbrigoNaoEncontradoException;
import uol.compass.domain.model.Abrigo;
import uol.compass.domain.service.AbrigoService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AbrigoView implements TableView {

    public static final int LIST_ALL_ABRIGOS = 1;
    public static final int GET_ABRIGO_BY_ID = 2;
    public static final int SAVE_ABRIGO = 3;
    public static final int UPDATE_ABRIGO = 4;
    public static final int DELETE_ABRIGO = 5;

    private static final int TAMANHO_CEP = 9;

    public static final Scanner SCANNER = new Scanner(System.in);

    private final AbrigoController abrigoController = new AbrigoController();
    private final AbrigoService abrigoService = new AbrigoService();

    private void showOperations() {
        System.out.println();
        System.out.println("----------------------------------------------------");
        System.out.println("[1] - Mostrar todos Abrigos");
        System.out.println("[2] - Encontrar um Abrigo por ID");
        System.out.println("[3] - Salvar um Abrigo");
        System.out.println("[4] - Atualizar um Abrigo");
        System.out.println("[5] - Apagar um Abrigo");
        System.out.println("----------------------------------------------------");
    }



    @Override
    public void readOperation() {
        showOperations();
        while (true) {
            try {
                System.out.println("Escolha uma das operações: ");
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

    private void getOperation(int intUserInput) throws OperacaoInvalidaException {
        switch (intUserInput) {
            case LIST_ALL_ABRIGOS -> getAbrigo();
            case GET_ABRIGO_BY_ID -> readAbrigoId();
            case SAVE_ABRIGO -> saveNewAbrigo();
            case UPDATE_ABRIGO -> updateAbrigo();
            case DELETE_ABRIGO -> deleteAbrigoById();
            default -> throw new OperacaoInvalidaException(intUserInput);
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
                System.out.println("ID do Abrigo que deseja atualizar: ");
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
                System.out.println("ID do Abrigo que deseja apagar: ");
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
        abrigo.setCapacidade(validateCapacidade());
        abrigo.setOcupacao(validateOcupacao());

        Abrigo savedAbrigo = abrigoService.save(abrigo);
        System.out.println(savedAbrigo);
    }

    private String validateNome() {
        String nome;
        while (true) {
            System.out.println("Nome: ");
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
            System.out.println("Endereço: ");
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
            System.out.println("CEP: ");
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
            System.out.println("Cidade: ");
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
            System.out.println("Responsável: ");
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
            System.out.println("Telefone: ");
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
            System.out.println("Email: ");
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

    private Integer validateCapacidade() {
        Integer capacidade;
        while (true) {
            System.out.println("Capacidade: ");
            capacidade = SCANNER.nextInt();
            if (capacidade != null && capacidade > 0) {
                break;
            }
            System.out.println();
            System.out.println("Capacidade não pode ser vazia e deve ser maior que 0. Por favor, tente novamente.");
            System.out.println();
        }

        return capacidade;
    }

    private Double validateOcupacao() {
        Double ocupacao;
        while (true) {
            System.out.println("Ocupação: ");
            ocupacao = SCANNER.nextDouble();
            if (ocupacao != null && ocupacao >= 0 && ocupacao <= 100) {
                break;
            }
            System.out.println();
            System.out.println("Ocupação não pode ser vazia e deve ser um valor entre 0 e 100. Por favor, tente novamente.");
            System.out.println();
        }

        return ocupacao;
    }

    public void readAbrigoId() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println();
                System.out.println("ID do Abrigo: ");
                var id = scanner.nextInt();
                try {
                    var abrigo = abrigoService.findByIdOrException(id);
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
