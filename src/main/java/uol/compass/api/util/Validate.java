package uol.compass.api.util;

import uol.compass.domain.dto.OrdemPedidoInputStatus;
import uol.compass.domain.model.Doacao;

import java.util.InputMismatchException;
import java.util.Scanner;

import static uol.compass.api.view.CentroDistribuicaoView.TAMANHO_CEP;

public class Validate {

    private Validate() {}

    public static int validateIntegerInRange(Scanner scanner, String message, int minRange, int maxRange) {
        int quantidade;
        while (true) {
            System.out.print(message);
            try {
                quantidade = scanner.nextInt();
                if (quantidade >= minRange && quantidade <= maxRange) {
                    break;
                }
                System.out.printf("Quantidade deve ser um valor de %s a %s\n", minRange, maxRange);
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }
        return quantidade;
    }

    public static int validateIntegerInput(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo inteiro. Por favor, tente novamente.");
            }
        }
    }


    public static boolean validateBooleanInput(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextBoolean();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo booleano. Por favor, tente novamente.");
            }
        }
    }


    public static String validateString(Scanner scanner, String printMessage, String errorMessage) {
        String nome;
        while (true) {
            System.out.print(printMessage);
            nome = scanner.nextLine();
            if (nome != null && !(nome.isBlank())) {
                break;
            }

            

            System.out.println();
            System.out.println(errorMessage);
            System.out.println();
        }

        return nome;
    }

    public static Doacao.Sexo validateProdutoSexo(Scanner scanner) {
        Doacao.Sexo sexo;
        while (true) {
            System.out.print("Sexo [F / M]: ");
            String sexoInput = scanner.nextLine().toUpperCase();
            try {
                sexo = Doacao.Sexo.valueOf(sexoInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Sexo inválido. Tente novamente.");
            }
        }

        return sexo;
    }

    public static Doacao.Categoria validateProdutoCategoria(Scanner scanner) {
        Doacao.Categoria categoria;
        while (true) {
            System.out.print("Categoria [ROUPA, HIGIENE, ALIMENTO, LIMPEZA]: ");
            String categoriaInput = scanner.nextLine().toUpperCase();
            try {
                categoria = Doacao.Categoria.valueOf(categoriaInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Categoria inválida. Tente novamente.");
            }
        }
        return categoria;
    }


    public static Doacao.Item validateProdutoItem(Scanner scanner, Doacao.Categoria categoria) {
        Doacao.Item item;
        while (true) {
            System.out.print(categoria.getPrintItens());
            String itemInput = scanner.nextLine().toUpperCase();
            try {
                item = Doacao.Item.valueOf(itemInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Item inválido. Tente novamente.");
            }
        }
        return item;
    }


    public static Doacao.Tamanho validateProdutoTamanho(Scanner scanner) {
        Doacao.Tamanho tamanho;
        while (true) {
            System.out.print("Tamanho [INFANTIL / PP / P / M / G / GG]: ");
            String tamanhoInput = scanner.nextLine().toUpperCase();
            try {
                tamanho = Doacao.Tamanho.valueOf(tamanhoInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Item inválido. Tente novamente.");
            }
        }
        return tamanho;
    }


    public static String validateCEP(Scanner scanner) {
        String cep;
        while (true) {
            System.out.print("CEP: ");
            cep = scanner.nextLine();
            if ((cep != null) && !(cep.isBlank()) && (cep.length() == TAMANHO_CEP)) {
                break;
            }
            System.out.println();
            System.out.println("Cep não pode ser nulo ou vazio e deve conter 9 caracteres. Tente novamente.");
            System.out.println();
        }

        return cep;
    }


    public static OrdemPedidoInputStatus validateOrdemPedidoStatus(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Aceitar ou Recusar Ordem de Pedido? [ACEITAR / RECUSAR]: ");
                return OrdemPedidoInputStatus.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("\nSão aceitos apenas os valores do tipo String [ACEITAR / RECUSAR]. Tente novamente.");
            }
        }
    }


    public static boolean solicitarOrdemPedido(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Solicitar Ordem de Pedido a um dos Centros de Distribuição? [true/false]: ");
                return scanner.nextBoolean();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("\nSão aceitos apenas valores do tipo booleano. Por favor, tente novamente.");
            }
        }
    }

}
