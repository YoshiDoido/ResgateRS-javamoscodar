package uol.compass.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrdemPedido {
    public enum Status {ACEITO, RECUSADO, PENDENTE}

    private Integer id;
    private Integer centroDistribuicaoId;
    private Integer centroDistribuicaoEnvioId;
    private Integer abrigoId;
    private Doacao.Item item;
    private Status status;
    private Integer quantidade;
    private String motivo;
    private Doacao.Categoria categoria;
}
