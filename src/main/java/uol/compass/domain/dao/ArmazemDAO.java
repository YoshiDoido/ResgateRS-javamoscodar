package uol.compass.domain.dao;

import uol.compass.domain.model.dto.TipoArmazem;

public interface ArmazemDAO {
    void save(Integer referenceId, TipoArmazem tipo);
}
