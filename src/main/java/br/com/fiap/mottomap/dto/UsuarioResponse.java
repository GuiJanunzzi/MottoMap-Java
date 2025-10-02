package br.com.fiap.mottomap.dto;

import br.com.fiap.mottomap.model.Usuario;

public record UsuarioResponse(
        Long id,
        String nome,
        String email
) {
    public static UsuarioResponse fromUsuario(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}