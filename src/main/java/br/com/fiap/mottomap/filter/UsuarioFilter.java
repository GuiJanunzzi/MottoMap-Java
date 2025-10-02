package br.com.fiap.mottomap.filter;

import br.com.fiap.mottomap.model.CargoUsuario;

public record UsuarioFilter(
    String nome,
    String email,
    CargoUsuario cargoUsuario
) {}