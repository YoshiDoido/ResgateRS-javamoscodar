package uol.compass.domain.dto;

import lombok.Getter;
import uol.compass.domain.model.OrdemPedido;

@Getter
public enum OrdemPedidoInputStatus {
    ACEITAR(OrdemPedido.Status.ACEITO),
    RECUSAR(OrdemPedido.Status.RECUSADO);

    private final OrdemPedido.Status status;

    OrdemPedidoInputStatus(OrdemPedido.Status status) {
        this.status = status;
    }
}
