package uol.compass.api.controller;

import uol.compass.domain.model.CentroDistribuicao;
import uol.compass.domain.service.CentroDistribuicaoService;

import java.util.List;

public class CentroDistribuicaoController {

    private final CentroDistribuicaoService centroDistribuicaoService = new CentroDistribuicaoService();

    public List<CentroDistribuicao> listAllCentrosDistribuicao() {
        return centroDistribuicaoService.findAll();
    }

    public CentroDistribuicao getCentroDistribuicaoById(Integer id) {
        return centroDistribuicaoService.findByIdOrException(id);
    }

    public CentroDistribuicao saveCentroDistribuicao(CentroDistribuicao centroDistribuicao) {
        return centroDistribuicaoService.save(centroDistribuicao);
    }

    public void deleteCentroDistribuicaoById(Integer id) {
        centroDistribuicaoService.deleteById(id);
    }

}
