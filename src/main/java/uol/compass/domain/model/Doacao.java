package uol.compass.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Doacao {
    @Getter
    public enum Categoria {
        ALIMENTO("Item [ARROZ / FEIJAO / LEITE / AGUA]: "),
        HIGIENE("Item [ESCOVA_DE_DENTES / PASTA_DE_DENTES / ABSORVENTE]: "),
        ROUPA("Item [AGASALHO / CAMISA]: ");

        private final String printItens;

        Categoria(String printItens) {
            this.printItens = printItens;
        }
    }
    public enum Item {ARROZ, FEIJAO, LEITE, SABONETE, ESCOVA_DE_DENTES, PASTA_DE_DENTES, ABSORVENTE, AGASALHO, CAMISA}
    public enum Sexo {F, M}
    public enum Tamanho {INFANTIL, PP, P, M, G, GG}

    private Integer id;
    private Categoria categoria;
    private Item item;
    private Sexo sexo;
    private Tamanho tamanho;
    private Integer quantidade;
    private Integer armazemId;

}
