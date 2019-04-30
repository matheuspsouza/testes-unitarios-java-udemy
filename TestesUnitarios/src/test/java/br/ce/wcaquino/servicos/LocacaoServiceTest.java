package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.buiders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.buiders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatcherProprio.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatcherProprio.ehHoje;
import static br.ce.wcaquino.matchers.MatcherProprio.ehHojeComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import br.ce.wcaquino.buiders.LocacaoBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import br.com.wcaquino.exception.FilmeSemEstoqueException;
import br.com.wcaquino.exception.LocadoraException;

public class LocacaoServiceTest {

	// Junit reinicializa todas as variaveis da classe para garantir independencia
	// entre os testes
	private LocacaoService locacaoService;
	private SPCService spcService;
	private LocacaoDAO dao;
	private EmailService emailService;

	// Contador
	@SuppressWarnings("unused")
	private static int cont; // Pertece ao escopo da classe e não do teste

	@Rule
	public ErrorCollector error = new ErrorCollector(); // Mostra todos os erros

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setup() {
		locacaoService = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		locacaoService.setLocacaoDAO(dao);
		spcService = Mockito.mock(SPCService.class);
		locacaoService.setSPCService(spcService);
		emailService = Mockito.mock(EmailService.class);
		locacaoService.setEmailService(emailService);

	}

	@Test
	public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// given
		Usuario usuario = umUsuario().build();
		List<Filme> filmes = Arrays.asList(umFilme().build());

		// when
		Locacao retorno = locacaoService.alugarFilme(usuario, filmes);

		// then
		boolean segunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(segunda);

		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}

	// Testando exceção
	// Elegante (Simples, enxuta, superficial) //Nao verifica mensagem de uma
	// excecao generica
	// Ideal excecao apenas por um motivo
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// (Cenario): Matheus quer alugar um filme que não está no estoque
		// Dado que o usuário Matheus quer alugar Pantera Negra

		Usuario user1 = umUsuario().build();
		List<Filme> listaFilmes = Arrays.asList(umFilme().semEstoque().build());

		// (Acao) When: Ocorre uma locação
		locacaoService.alugarFilme(user1, listaFilmes);

		// (Verificacao) Then: Exceção deve ser informada
	}

	// Testando exceção
	// Robusta
	// Melhor controle da execução do código que a opção Elegante
	@Test
	public void naoDeveAlugaraFilmeSemUsuario() throws FilmeSemEstoqueException {
		// (Cenario): Testando alugar com usuário vazio

		List<Filme> listaFilmes = Arrays.asList(umFilme().build());

		// (Acao) When: Ocorre uma locação
		try {
			locacaoService.alugarFilme(null, listaFilmes);
			// como vc esta testando uma exceção, caso não ocorra deve-se lançar uma falha
			fail("Deveria lançar exceção");
		} catch (LocadoraException e) {
			// (Verificacao) Then: Exceção deve ser informada
			assertThat(e.getMessage(), is("Usuario vazio"));

		}

	}

	// Testando exceção
	// Nova
	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		// (Cenario): Testando alugar com filme vazio
		Usuario user1 = umUsuario().build();

		// Indicacao da excecao dessa forma faz parte do cenario
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Filme vazio");

		// (Acao) When: Ocorre uma locação
		locacaoService.alugarFilme(user1, null);

		// (Verificacao) Then: Exceção deve ser informada

	}

	@Test
	public void deveAlugarFilme() throws Exception { // Teste optado por funcionalidade
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// (Cenario): Matheus quer alugar um filme (pantera negra)
		// Dado que o usuário Matheus quer alugar Pantera Negra
		Usuario user1 = umUsuario().build();
		List<Filme> listaFilmes = Arrays.asList(umFilme().comValor(3.5).build());
		// (Acao) When: Ocorre uma locação
		Locacao locacao = locacaoService.alugarFilme(user1, listaFilmes);

		// (Verificacao) Then: o filme deve ser alugado (Através do objeto locação)
		// Mostra todos os erros
		error.checkThat(locacao.getValor(), is(equalTo(3.5)));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));

	}

	@Test
	public void naoDeveAlugarFilmePararNegativaoSPC() throws FilmeSemEstoqueException {
		// given
		Usuario usuario = umUsuario().build();
		List<Filme> listaFilmes = Arrays.asList(umFilme().build());

		when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		// when
		try {
			locacaoService.alugarFilme(usuario, listaFilmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertEquals("Usuário Negativado", e.getMessage());
		}

		// then
		Mockito.verify(spcService).possuiNegativacao(usuario);

	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//given
		Usuario usuario=umUsuario().build();
		Usuario usuario2=umUsuario().comNome("Usuario em dia").build();
		Usuario usuario3=umUsuario().comNome("Usuario atrasado 2").build();
		List<Locacao> locacoes= Arrays.asList(
				LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario).build(),
				LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario3).build(),
				LocacaoBuilder.umLocacao().comUsuario(usuario2).build(),
				LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario3).build()
				);
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//when
		locacaoService.notificarAtrasos();
		
		//then
		verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(emailService).notificarAtraso(usuario); //Unica forma de validação para esse exemplo
		verify(emailService,Mockito.atLeastOnce()).notificarAtraso(usuario3);
		verify(emailService, never()).notificarAtraso(usuario2);
		verifyNoMoreInteractions(emailService);
		
		
		
	}
}
