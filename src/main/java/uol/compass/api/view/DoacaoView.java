package uol.compass.api.view;

import uol.compass.api.exception.OperacaoInvalidaException;
import uol.compass.domain.service.DoacaoService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DoacaoView implements TableView {

    private static final Scanner SCANNER = new Scanner(System.in);

    private final DoacaoService doacaoService = new DoacaoService();

    private static final int LIST_ALL_DOACOES = 1;
    private static final int GET_DOACAO_BY_ID = 2;
    private static final int SAVE_DOACAO = 3;
    public static final int UPDATE_DOACAO = 4;
    public static final int DELETE_DOACAO = 5;



    private void showOperations() {
        System.out.println();
        System.out.println("---------------------------------------------------------------");
        System.out.println("[1] - Mostrar todas as Doações");
        System.out.println("[2] - Encontrar uma Doação por ID");
        System.out.println("[3] - Realizar Nova Doação");
        System.out.println("[4] - Atualizar Doação");
        System.out.println("[5] - Apagar uma Doação");
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
            case LIST_ALL_DOACOES -> doacaoService.realizarDoacao();
            case GET_DOACAO_BY_ID -> doacaoService.realizarDoacao();
            case SAVE_DOACAO -> doacaoService.realizarDoacao();
            case UPDATE_DOACAO -> doacaoService.realizarDoacao();
            case DELETE_DOACAO -> doacaoService.realizarDoacao();
            default -> throw new OperacaoInvalidaException(intUserInput);
        }
    }

}
