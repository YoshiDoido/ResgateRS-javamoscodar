package uol.compass.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Doacao {

    private Integer id;
    private String centro;
    private LocalDate data;
    private CentroDistribuicao centroDistribuicao;

}
