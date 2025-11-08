package br.com.fiap.mottomap.dto;

public record Token(
    String token,
    String type,
    String prefix,
    Long usuarioId,
    String nomeUsuario
) {}
