package uol.compass.domain.service;

import lombok.extern.slf4j.Slf4j;
import uol.compass.domain.exception.AbrigoNaoEncontradoException;
import uol.compass.domain.model.Abrigo;
import uol.compass.domain.dao.AbrigoDAO;
import uol.compass.infrastructure.dao_implementation.AbrigoDAOImpl;

import java.util.List;
import java.util.Objects;

@Slf4j
public class AbrigoService {

    private final AbrigoDAO abrigoRepository = new AbrigoDAOImpl();

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
        System.out.println();
        log.info("Novo abrigo de id {} salvo com sucesso!\n", savedAbrigo.getId());
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

}
