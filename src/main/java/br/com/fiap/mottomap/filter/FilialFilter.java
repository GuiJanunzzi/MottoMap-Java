package br.com.fiap.mottomap.filter;

public record FilialFilter(
    String nome,
    String cidade,
    String siglaEstado,
    Integer capacidadeMin

) {} 