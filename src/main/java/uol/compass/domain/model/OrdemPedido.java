package uol.compass.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class OrdemPedido {

    private Integer id;
    private CentroDistribuicao centroDistribuicaoId;
    private String item;
    private Integer quantidade;

}
