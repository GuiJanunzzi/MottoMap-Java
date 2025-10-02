package br.com.fiap.mottomap.filter;

import br.com.fiap.mottomap.model.ModeloMoto;
import br.com.fiap.mottomap.model.StatusMoto;

public record MotoFilter(
    String placa,
    String chassi,
    ModeloMoto modeloMoto,
    StatusMoto statusMoto
) {}