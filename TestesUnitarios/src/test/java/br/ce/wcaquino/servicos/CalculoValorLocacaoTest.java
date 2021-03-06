package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.buiders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.buiders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	@InjectMocks
	private LocacaoService locacaoService;

	@Mock
	private SPCService spcService;

	@Mock
	private LocacaoDAO dao;

	@Parameter
	public List<Filme> filmes;

	@Parameter(value = 1)
	public double valorLocacao;

	@Parameter(value = 2)
	public String cenario;

	private static Filme filme1 = umFilme().build();
	private static Filme filme2 = umFilme().build();
	private static Filme filme3 = umFilme().build();
	private static Filme filme4 = umFilme().build();
	private static Filme filme5 = umFilme().build();
	private static Filme filme6 = umFilme().build();
	private static Filme filme7 = umFilme().build();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		CalculadoraTest.ordem.append(3);

	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println(CalculadoraTest.ordem.toString());
	}

	@Parameters(name = "Teste {index} - {2}")
	public static Collection<Object[]> getParametros() { // Cada linha da matriz é um cenário //Enão tem-se uma lista

		return Arrays.asList(new Object[][] { { Arrays.asList(filme1, filme2, filme3), 11.0, "3º Filme: 25%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4º Filme: 50%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5º Filme: 75%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6º Filme:100%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0,
						"7º Filme: Sem desconto" },
				{ Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem desconto" }, });

	}

	@Test // O numero de testes exectudos é de acordo com o número de cenários passados
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException, InterruptedException {
		// given
		Usuario usuario = umUsuario().build();
		
		//testando paralelismo
		Thread.sleep(5000);

		// when
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

		// then
		// 4+4+3
		assertThat(resultado.getValor(), is(valorLocacao));

	}

}
