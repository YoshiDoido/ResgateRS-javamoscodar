package uol.compass.domain.repository;

import java.util.List;
import java.util.Optional;

public interface SimpleCrud<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID entityId);
    <S extends T> S save(S entity);
    <S extends T> S update(ID entityId, S entity);
    void deleteById(ID entityId);
}
