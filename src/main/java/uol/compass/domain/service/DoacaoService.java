package uol.compass.domain.service;

import uol.compass.domain.dao.DoacaoDAO;
import uol.compass.infrastructure.dao_implementation.DoacaoDAOImpl;

public class DoacaoService {

    private final DoacaoDAO doacaoDao;

    public DoacaoService() {
        this.doacaoDao = new DoacaoDAOImpl();
    }

    public void realizarDoacao(){
        System.out.printf("Doação Realizada");
    }

}
