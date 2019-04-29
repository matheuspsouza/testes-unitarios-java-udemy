package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.buiders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.buiders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatcherProprio.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatcherProprio.ehHoje;
import static br.ce.wcaquino.matchers.MatcherProprio.ehHojeComDiferencaDias;
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
import br.ce.wcaquino.utils.DataUtils;
import br.com.wcaquino.exception.FilmeSemEstoqueException;
import br.com.wcaquino.exception.LocadoraException;
import buildermaster.BuilderMaster;


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
		Usuario usuario=umUsuario().build();
		List<Filme> filmes=Arrays.asList(umFilme().build());
		
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
		LocacaoService locacaoService = new LocacaoService();
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
		error.checkThat(locacao.getDataLocacao(),ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));

	}  
	
	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}

	
}