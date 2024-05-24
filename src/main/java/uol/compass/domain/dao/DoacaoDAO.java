package uol.compass.domain.dao;

import uol.compass.domain.model.Doacao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DoacaoDAO {
    List<Doacao> findCentroDistribuicaoAllDoacoes(Integer id);

    List<Doacao> getAllDoacoesAbrigo(Integer abrigoId);

    Doacao inserirDoacao(Integer id, Doacao doacao);

    void removerDoacao(Integer doacaoId);

    Doacao atualizarDoacao(Integer id, Doacao doacao);

    Doacao atualizarDoacaoDeItemIgual(Doacao doacao, Doacao doacaoAtual);

    Optional<Doacao> getDoacaoByCentroIdAndItem(Integer centroId, Doacao.Item item);

    Map<Doacao.Categoria, Integer> armazemTotalDoacoes(Integer id);

    int totalCategoria(Integer armazemId, Doacao.Categoria categoria);

}
