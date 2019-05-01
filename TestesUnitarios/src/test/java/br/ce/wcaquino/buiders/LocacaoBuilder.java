package br.ce.wcaquino.buiders;

import static br.ce.wcaquino.buiders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.buiders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;

import java.util.Arrays;
import java.util.Date;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;


public class LocacaoBuilder {
	private Locacao elemento;
	private LocacaoBuilder(){}

	public static LocacaoBuilder umLocacao() {
		LocacaoBuilder builder = new LocacaoBuilder();
		inicializarDadosPadroes(builder);
		return builder;
	}

	public static void inicializarDadosPadroes(LocacaoBuilder builder) {
		builder.elemento = new Locacao();
		Locacao elemento = builder.elemento;

		
		elemento.setUsuario(umUsuario().build());
		elemento.setListaFilmes(Arrays.asList(umFilme().build()));
		elemento.setDataLocacao(new Date());
		elemento.setDataRetorno(obterDataComDiferencaDias(1));
		elemento.setValor(4.0);
	}

	public LocacaoBuilder comUsuario(Usuario param) {
		elemento.setUsuario(param);
		return this;
	}

	public LocacaoBuilder comListaListaFilmes(Filme... params) {
		elemento.setListaFilmes(Arrays.asList(params));
		return this;
	}

	public LocacaoBuilder comDataLocacao(Date param) {
		elemento.setDataLocacao(param);
		return this;
	}

	public LocacaoBuilder comDataRetorno(Date param) {
		elemento.setDataRetorno(param);
		return this;
	}

	public LocacaoBuilder comValor(Double param) {
		elemento.setValor(param);
		return this;
	}
	
	public LocacaoBuilder atrasada() {
		this.elemento.setDataLocacao(obterDataComDiferencaDias(-4));
		this.elemento.setDataRetorno(obterDataComDiferencaDias(-2));
		return this;
	}

	public Locacao build() {
		return elemento;
	}
}