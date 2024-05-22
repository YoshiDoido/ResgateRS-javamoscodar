package uol.compass.api.view;

import uol.compass.api.exception.OperacaoInvalidaException;
import uol.compass.domain.exception.OrdemPedidoNaoEncontradoException;
import uol.compass.domain.service.OrdemPedidoService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class OrdemPedidoView implements TableView {

    public static final int LIST_ALL_ORDENS = 1;
    public static final int GET_ORDEM_BY_ID = 2;

    public static final Scanner SCANNER = new Scanner(System.in);

    // Depois implementar a classe OrdemPedidoService
    private final OrdemPedidoService ordemPedidoService = new OrdemPedidoService();

    public void showOperations() {
        System.out.println();
        System.out.println("---------------------------------------------------------------");
        System.out.println("[1] - Mostrar todas as Ordens");
        System.out.println("[2] - Encontrar uma Ordem por ID");
        System.out.println("---------------------------------------------------------------");
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
            case LIST_ALL_ORDENS -> getOrdens();
            case GET_ORDEM_BY_ID -> readOrdemId();
            default -> throw new OperacaoInvalidaException(intUserInput);
        }
    }

    private void getOrdens() {
        System.out.println();
        ordemPedidoService.findAll().forEach(System.out::println);
    }

    private void readOrdemId() {
        while (true) {
            try {
                System.out.println();
                System.out.println("Digite o ID do Ordem: ");
                var id = SCANNER.nextInt();
                try {
                    var ordemPedido = ordemPedidoService.findByIdOrException(id);
                    System.out.println("\n" + ordemPedido);
                } catch (OrdemPedidoNaoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
                break;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }
    }
}