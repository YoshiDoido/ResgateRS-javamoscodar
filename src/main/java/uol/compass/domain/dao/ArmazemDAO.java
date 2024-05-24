package uol.compass.domain.dao;

import uol.compass.domain.dto.TipoArmazem;

public interface ArmazemDAO {
    Integer save(Integer referenceId, TipoArmazem tipo);
}
