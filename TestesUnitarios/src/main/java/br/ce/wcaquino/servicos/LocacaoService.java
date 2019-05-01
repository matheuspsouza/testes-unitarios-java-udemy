package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import br.com.wcaquino.exception.FilmeSemEstoqueException;
import br.com.wcaquino.exception.LocadoraException;

public class LocacaoService {

	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;

	public Locacao alugarFilme(Usuario usuario, List<Filme> listaFilmes)
			throws FilmeSemEstoqueException, LocadoraException {

		if (usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}

		if (listaFilmes == null || listaFilmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}
		for (Filme filme : listaFilmes) {

			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
		}
		boolean negativado;
		try {
			negativado = spcService.possuiNegativacao(usuario);
		} catch (Exception e) {
			throw new LocadoraException("SPC com problemas, tente novamente mais tarde");
		}

		if (negativado) {
			throw new LocadoraException("Usuário Negativado");

		}

		Locacao locacao = new Locacao();
		locacao.setListaFilmes(listaFilmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(Calendar.getInstance().getTime());
		locacao.setValor(calcularValorLocacao(listaFilmes));

		// Entrega no dia seguinte
		Date dataEntrega = Calendar.getInstance().getTime();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		dao.salvar(locacao);

		return locacao;
	}

	private double calcularValorLocacao(List<Filme> listaFilmes) {
		double precoLocacao=0d;
		for (int i = 0; i < listaFilmes.size(); i++) {

			double valorFilme = listaFilmes.get(i).getPrecoLocacao();

			switch (i) {
			case 2:
				valorFilme = valorFilme * 0.75;
				break;
			case 3:
				valorFilme = valorFilme * 0.50;
				break;
			case 4:
				valorFilme = valorFilme * 0.25;
				break;
			case 5:
				valorFilme = 0;
				break;
			}

			precoLocacao += valorFilme;

		}
		return precoLocacao;
	}

	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
		for (Locacao locacao : locacoes) {
			if (locacao.getDataRetorno().before(new Date())) {
				emailService.notificarAtraso(locacao.getUsuario());
			}
		}

	}

	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao=new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setListaFilmes(locacao.getListaFilmes());
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor()*dias);
		
		dao.salvar(novaLocacao);
		
		
		
	}

}