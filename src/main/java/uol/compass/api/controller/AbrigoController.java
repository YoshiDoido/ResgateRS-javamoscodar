package uol.compass.api.controller;

import java.util.List;

import uol.compass.domain.model.Abrigo;
import uol.compass.domain.service.AbrigoService;

public class AbrigoController {

    private final AbrigoService AbrigoService = new AbrigoService();

    public List<Abrigo> listAllAbrigos() {
        return AbrigoService.findAll();
    }

    public Abrigo getAbrigoById(Integer id) {
        return AbrigoService.findByIdOrException(id);
    }

    public Abrigo saveAbrigo(Abrigo Abrigo) {
        return AbrigoService.save(Abrigo);
    }

    public void deleteAbrigoById(Integer id) {
        AbrigoService.deleteById(id);
    }

}
