package br.com.fiap.mottomap.filter;

import br.com.fiap.mottomap.model.Area;

public record PosicaoPatioFilter(
    String identificacao,
    Area area,
    Boolean ocupado,
    Long filialId
    
) {}