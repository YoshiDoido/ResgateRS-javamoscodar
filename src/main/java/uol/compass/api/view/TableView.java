package uol.compass.api.view;

import uol.compass.api.exception.OperacaoInvalidaException;

import java.util.InputMismatchException;
import java.util.Scanner;

public interface TableView {
    void showOperations();

    void getOperation(int intUserInput);

    default void readOperation() {
        var scanner = new Scanner(System.in);
        showOperations();
        while (true) {
            try {
                System.out.print("Escolha uma das operações: ");
                int intUserInput = scanner.nextInt();
                getOperation(intUserInput);
                break;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
                System.out.println();
            } catch (OperacaoInvalidaException e) {
                scanner.nextLine();
                System.out.printf("\n%s. Por favor, tente novamente.\n", e.getMessage());
                System.out.println();
            }
        }
    }
}
