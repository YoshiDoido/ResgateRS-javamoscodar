package uol.compass.api.view;

import uol.compass.api.exception.TabelaInvalidaException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GeneralView {

    public static final int CENTRO_DISTRIBUICAO = 1;

    private static final Scanner SCANNER = new Scanner(System.in);


    public void start() {
        while (true) {
            TableView tableView = readTableUserInput();
            tableView.readOperation();

            if (exit()) {
                System.out.println("\nFinalizando...");
                break;
            }

            printSpacing();
        }
    }


    private TableView readTableUserInput() {
        startPrint();
        while (true) {
            try {
                System.out.print("Escolha uma das tabelas: ");
                var intUserInput = SCANNER.nextInt();
                return getTableView(intUserInput);
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
                System.out.println();
            } catch (TabelaInvalidaException e) {
                SCANNER.nextLine();
                System.out.printf("\n%s. Por favor, tente novamente.\n", e.getMessage());
                System.out.println();
            }
        }
    }


    private void startPrint() {
        System.out.println("---------------------------------------------------------------");
        System.out.println("[1] - Centro de Distribuição");
        System.out.println("[2] - Abrigo");
        System.out.println("[?] - ...");
        System.out.println("[?] - ...");
        System.out.println("---------------------------------------------------------------");
    }


    private TableView getTableView(int tableInt) {
        return switch (tableInt) {
            case CENTRO_DISTRIBUICAO -> new CentroDistribuicaoView();
            case 2 -> new AbrigoView();
            default -> throw new TabelaInvalidaException(tableInt);
        };
    }


    private boolean exit() {
        while (true) {
            try {
                System.out.print("\nDeseja Continuar? [true/false]: ");
                return !(SCANNER.nextBoolean());
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo booleano. Por favor, tente novamente.");
            }
        }
    }


    private static void printSpacing() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
