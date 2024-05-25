package uol.compass.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Abrigo implements Doavel{

    private Integer id;
    private String nome;
    private String endereco;
    private String responsavel;
    private String cep;
    private String cidade;
    private String telefone;
    private String email;
    private Integer capacidade;
    private Integer ocupacao;

}
