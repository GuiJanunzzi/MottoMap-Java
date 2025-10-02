package br.com.fiap.mottomap.filter;

import java.time.LocalDate;

import br.com.fiap.mottomap.model.TipoProblema;

public record ProblemaFilter(
    TipoProblema tipoProblema,
    LocalDate dataInicio,
    LocalDate dataFim,
    Boolean resolvido,
    Long motoId
) {}
