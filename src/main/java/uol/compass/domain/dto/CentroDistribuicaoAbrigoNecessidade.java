package uol.compass.domain.dto;

import uol.compass.domain.model.Doacao;

public record CentroDistribuicaoAbrigoNecessidade(Integer centroDistribuicaoId, Doacao.Item item, Integer quantidade) {
}
