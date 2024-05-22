package uol.compass.domain.model.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uol.compass.domain.model.Doacao;

@Getter
@Setter
@ToString
public class AbrigoNecessidades {
    private Doacao.Categoria categoria;
    private Doacao.Item item;
    private Integer Quantidade;
}
