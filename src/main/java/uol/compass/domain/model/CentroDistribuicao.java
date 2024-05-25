package uol.compass.domain.model;

import lombok.*;

@Getter
@Setter
@ToString
public class CentroDistribuicao implements Doavel {
    private Integer id;
    private String nome;
    private String endereco;
    private String cep;
}
