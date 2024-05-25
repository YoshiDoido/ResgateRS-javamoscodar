package uol.compass.domain.service;

import lombok.extern.slf4j.Slf4j;
import uol.compass.domain.dao.ArmazemDAO;
import uol.compass.domain.dao.DoacaoDAO;
import uol.compass.domain.exception.AbrigoNaoEncontradoException;
import uol.compass.domain.exception.CategoriaLimiteMaximoException;
import uol.compass.domain.model.Abrigo;
import uol.compass.domain.dao.AbrigoDAO;
import uol.compass.domain.dto.AbrigoNecessidades;
import uol.compass.domain.dto.CentroDistribuicaoAbrigoNecessidade;
import uol.compass.domain.dto.TipoArmazem;
import uol.compass.domain.model.Doacao;
import uol.compass.domain.model.OrdemPedido;
import uol.compass.infrastructure.dao_implementation.AbrigoDAOImpl;
import uol.compass.infrastructure.dao_implementation.ArmazemDAOImpl;
import uol.compass.infrastructure.dao_implementation.DoacaoDAOImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class AbrigoService {

    private final AbrigoDAO abrigoRepository = new AbrigoDAOImpl();
    private final ArmazemDAO armazemDAO = new ArmazemDAOImpl();
    private final DoacaoDAO doacaoDAO = new DoacaoDAOImpl();


    public List<Abrigo> findAll() {
        return abrigoRepository.findAll();
    }

    public Abrigo findByIdOrException(Integer id) {
        Objects.requireNonNull(id);
        return abrigoRepository.findById(id)
                .orElseThrow(() -> new AbrigoNaoEncontradoException(id));
    }


    public Abrigo save(Abrigo abrigo) {
        Abrigo savedAbrigo = abrigoRepository.save(abrigo);
        armazemDAO.save(abrigo.getId(), TipoArmazem.ABRIGO);
        System.out.println();
        log.info("Novo abrigo de id {} salvo com sucesso!\n", savedAbrigo.getId());
        log.info("Armazem do abrigo salvo com sucesso!\n");
        return savedAbrigo;
    }


    public Abrigo update(Integer id, Abrigo abrigo) {
        Abrigo update = abrigoRepository.update(id, abrigo);
        System.out.println();
        log.info("Abrigo de id {} atualizado com sucesso!", id);
        return update;
    }


    public void deleteById(Integer id) {
        abrigoRepository.deleteById(id);
        System.out.println();
        log.info("Abrigo de id {} apagado com sucesso!", id);
    }


    public List<Doacao> getAllDoacoesAbrigo(Integer abrigoId) {
        findByIdOrException(abrigoId);
        return doacaoDAO.getAllDoacoesAbrigo(abrigoId);
    }


    public void abrigoTotalDoacoes(Integer id) {
        var armazemId = abrigoRepository.getAbrigoArmazemId(id);
        var totalMap = doacaoDAO.armazemTotalDoacoes(armazemId);
        Arrays.stream(Doacao.Categoria.values()).sequential().forEach(categoria -> {
            if (!totalMap.containsKey(categoria)) {
                totalMap.put(categoria, 0);
            }
        });
        totalMap.forEach((categoria, quantidade) -> System.out.print(categoria + ": " + quantidade + " | "));
    }

    public List<CentroDistribuicaoAbrigoNecessidade> listarNecessidades(AbrigoNecessidades abrigoNecessidades) {
        return abrigoRepository.listarNecessidades(abrigoNecessidades);
    }


    public List<OrdemPedido> listAbrigoOrdensPedido(Integer id) {
        return abrigoRepository.abrigoOrdensPedido(id);
    }


    public Integer getAbrigoArmazemId(Integer id) {
        Integer abrigoArmazemId = abrigoRepository.getAbrigoArmazemId(id);
        if (abrigoArmazemId == null) {
            abrigoArmazemId = armazemDAO.save(id, TipoArmazem.ABRIGO);
        }
        return abrigoArmazemId;
    }


    public void verificarTotalCategoriaAbrigo(Integer abrigoId, Doacao.Categoria categoria) {
        int armazemId = getAbrigoArmazemId(abrigoId);
        int totalCategoria = doacaoDAO.totalCategoria(armazemId, categoria);
        if (totalCategoria >= OrdemPedidoService.CATEGORIA_ABRIGO_ESTOQUE_MAXIMO) {
            throw new CategoriaLimiteMaximoException(
                    String.format("Categoria %s atingiu o seu limite máximo (%d).", categoria,
                            OrdemPedidoService.CATEGORIA_ABRIGO_ESTOQUE_MAXIMO)
            );
        }
    }

}
