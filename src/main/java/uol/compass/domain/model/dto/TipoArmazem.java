package uol.compass.domain.model.dto;

import lombok.Getter;

@Getter
public enum TipoArmazem {
    ABRIGO("abrigo_id"),
    CENTRO_DISTRIBUICAO("centro_distribuicao_id");

    private final String tipo;

    TipoArmazem(String tipo) {
        this.tipo = tipo;
    }
}
