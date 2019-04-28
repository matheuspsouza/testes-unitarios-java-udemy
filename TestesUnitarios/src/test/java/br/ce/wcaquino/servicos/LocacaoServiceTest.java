package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.matchers.MatcherProprio.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatcherProprio.ehHoje;
import static br.ce.wcaquino.matchers.MatcherProprio.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.matchers.MatcherProprio;
import br.ce.wcaquino.utils.DataUtils;
import br.com.wcaquino.exception.FilmeSemEstoqueException;
import br.com.wcaquino.exception.LocadoraException;


public class LocacaoServiceTest {

	// Junit reinicializa todas as variaveis da classe para garantir independencia
	// entre os testes
	private LocacaoService locacaoService;

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

	}
	
	@Test
	public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//given
		Usuario usuario=new Usuario("User 1");
		List<Filme> filmes=Arrays.asList(new Filme("Filme 1",2,5.0));
		
		//when
		Locacao retorno= locacaoService.alugarFilme(usuario, filmes);
		
		//then
		boolean segunda=DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
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

		Usuario user1 = new Usuario("Matheus");
		List<Filme> listaFilmes = Arrays.asList(new Filme("Pantera Negra", 0, 3.5));

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
		LocacaoService locacaoService = new LocacaoService();
		List<Filme> listaFilmes = Arrays.asList(new Filme("Pantera Negra", 1, 3.5));

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
		Usuario user1 = new Usuario("Matheus");

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
		Usuario user1 = new Usuario("Matheus");
		List<Filme> listaFilmes = Arrays.asList(new Filme("Pantera Negra", 1, 3.5));
		// (Acao) When: Ocorre uma locação
		Locacao locacao = locacaoService.alugarFilme(user1, listaFilmes);

		// (Verificacao) Then: o filme deve ser alugado (Através do objeto locação)
		// Mostra todos os erros
		error.checkThat(locacao.getValor(), is(equalTo(3.5)));
		error.checkThat(locacao.getDataLocacao(),ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));

	}

	/*
	 * @Test public void testeLocacao1() { // Teste optado por funcionalidade
	 * 
	 * // (Cenario): Matheus quer alugar um filme (pantera negra) // Dado que o
	 * usuário Matheus quer alugar Pantera Negra LocacaoService locacaoService = new
	 * LocacaoService(); Usuario user1 = new Usuario("Matheus"); Filme filme1 = new
	 * Filme("Pantera Negra", 1, 3.5);
	 * 
	 * // (Acao) When: Ocorre uma locação Locacao locacao =
	 * locacaoService.alugarFilme(user1, filme1);
	 * 
	 * // (Verificacao) Then: o filme deve ser alugado (Através do objeto locação)
	 * // AssertThat -> Verifique que (Método mais legivel)
	 * assertThat(locacao.getValor(), is(3.5)); assertThat(locacao.getValor(),
	 * is(equalTo(3.5))); assertThat(locacao.getValor(), is(not(5.0)));
	 * assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
	 * assertThat(isMesmaData(locacao.getDataRetorno(),
	 * DataUtils.obterDataComDiferencaDias(1)), is(true)); }
	 * 
	 * @Test public void testeLocacao0() {
	 * 
	 * // (Cenario): Matheus quer alugar um filme (pantera negra) // Dado que o
	 * usuário Matheus quer alugar Pantera Negra LocacaoService locacaoService = new
	 * LocacaoService(); Usuario user1 = new Usuario("Matheus"); Filme filme1 = new
	 * Filme("Pantera Negra", 1, 3.5);
	 * 
	 * // (Acao) When: Ocorre uma locação Locacao locacao =
	 * locacaoService.alugarFilme(user1, filme1);
	 * 
	 * // (Verificacao) Then: o filme deve ser alugado (Através do objeto locação)
	 * Assert.assertEquals(3.5, locacao.getValor(), 0.01);
	 * Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new
	 * Date())); Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(),
	 * DataUtils.obterDataComDiferencaDias(1)));
	 * 
	 * }
	 */
}
