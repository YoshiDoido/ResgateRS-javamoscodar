package uol.compass.domain.service;

import lombok.extern.slf4j.Slf4j;
import uol.compass.domain.dao.ArmazemDAO;
import uol.compass.domain.exception.AbrigoNaoEncontradoException;
import uol.compass.domain.model.Abrigo;
import uol.compass.domain.model.Doacao;
import uol.compass.domain.dao.AbrigoDAO;
import uol.compass.domain.model.dto.AbrigoNecessidades;
import uol.compass.domain.model.dto.CentroDistribuicaoAbrigoNecessidade;
import uol.compass.domain.model.dto.TipoArmazem;
import uol.compass.infrastructure.dao_implementation.AbrigoDAOImpl;
import uol.compass.infrastructure.dao_implementation.ArmazemDAOImpl;

import java.util.List;
import java.util.Objects;

@Slf4j
public class AbrigoService {

    private final AbrigoDAO abrigoRepository = new AbrigoDAOImpl();
    private final ArmazemDAO armazemDAO = new ArmazemDAOImpl();

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
        log.info("Armazem do centro de distribuição salvo com sucesso!\n");
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


    public List<CentroDistribuicaoAbrigoNecessidade> listarNecessidades(AbrigoNecessidades abrigoNecessidades) {
        return abrigoRepository.listarNecessidades(abrigoNecessidades);
    }


    /*
    * criar o método List<Doacoes> getAllDoacoesAbrigo(Integer abrigoId)`na classe AbrigoService. Dentro desse método precisamos verificar se o abrigo existe ou não, basta chamar o método `findByIdOrException(id) da mesma classe.
     * */
    public List<Doacao> getAllDoacoesAbrigo(Integer abrigoId) {

        try {
            findByIdOrException(abrigoId);
            return abrigoRepository.getAllDoacoesAbrigo(abrigoId);
        } catch (AbrigoNaoEncontradoException e) {
            log.error("Abrigo de id {} não encontrado", abrigoId);
            throw e;
        }
    }

}
