package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.com.wcaquino.exception.FilmeSemEstoqueException;
import br.com.wcaquino.exception.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	private LocacaoService locacaoService;
	
	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public double valorLocacao;
	
	@Parameter(value=2)
	public String cenario;
	
	private static Filme filme1=new Filme("Filme 1", 2, 4.0);
	private static Filme filme2=new Filme("Filme 2", 2, 4.0);
	private static Filme filme3=new Filme("Filme 3", 2, 4.0);
	private static Filme filme4=new Filme("Filme 4", 2, 4.0);
	private static Filme filme5=new Filme("Filme 5", 2, 4.0);
	private static Filme filme6=new Filme("Filme 6", 2, 4.0);
	private static Filme filme7=new Filme("Filme 7", 2, 4.0);

	@Before
	public void setup() {
		locacaoService = new LocacaoService();

	}
	
	@Parameters (name="Teste {index} - {2}")
	public static Collection<Object[]> getParametros(){ //Cada linha da matriz é um cenário //Enão tem-se uma lista 
		
			
		return Arrays.asList(new Object[][]{
			{Arrays.asList(filme1,filme2,filme3),11.0,"3º Filme: 25%"},
			{Arrays.asList(filme1,filme2,filme3,filme4),13.0,"4º Filme: 50%"},
			{Arrays.asList(filme1,filme2,filme3,filme4,filme5),14.0,"5º Filme: 75%"},
			{Arrays.asList(filme1,filme2,filme3,filme4,filme5,filme6),14.0,"6º Filme:100%"},
			{Arrays.asList(filme1,filme2,filme3,filme4,filme5,filme6,filme7),18.0,"7º Filme: Sem desconto"},
			{Arrays.asList(filme1,filme2),8.0,"2 Filmes: Sem desconto"},
		});
		
	}

	@Test //O numero de testes exectudos é de acordo com o número de cenários passados
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		// given
		Usuario usuario = new Usuario("user 1");
		
		// when
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

		// then
		// 4+4+3
		assertThat(resultado.getValor(), is(valorLocacao));

	}

}
