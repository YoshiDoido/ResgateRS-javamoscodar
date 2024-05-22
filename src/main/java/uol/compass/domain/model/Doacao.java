package uol.compass.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Doacao {
    public enum Categoria {ALIMENTO, HIGIENE, ROUPA}
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
