package br.ce.wcaquino.buiders;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {
	
	private Usuario usuario;
	
	private UsuarioBuilder() {
		
	}
	
	public static UsuarioBuilder umUsuario() {
		UsuarioBuilder usuarioBuilder=new UsuarioBuilder();
		usuarioBuilder.usuario=new Usuario();
		usuarioBuilder.usuario.setNome("User 1");
		return usuarioBuilder;
	}
	
	public UsuarioBuilder comNome(String nome) {
		this.usuario.setNome(nome);
		return this;
		
	}
	public Usuario build() {
		return usuario;
	}
	
	
}
