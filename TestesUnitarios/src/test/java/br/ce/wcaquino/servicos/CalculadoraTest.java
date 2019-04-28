package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.wcaquino.exception.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	
	private Calculadora calc;
	
	@Before
	public void setUp() {
		calc=new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		
		//cenario-given
		int a=5;
		int b=3;
		//Calculadora calc=new Calculadora();
		
		//acao-when
		int resultado=calc.somar(a,b);
		
		//verificacao - then 
		Assert.assertEquals(8, resultado);

		
		
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		//given
		int a=8;
		int b=5;
	
		//when
		int resultado=calc.subtrair(a,b);
		
		//then
		Assert.assertEquals(3, resultado);
		
	}
	
	@Test
	public void deveDividirDoisValores(){
		//given
		int a=6;
		int b=3;
				
		//when
		int resultado =calc.divide(a,b);
		
		//then
		Assert.assertEquals(2, resultado);
		
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() {
		//cenario
		int a=3;
		int b=0;
		
		//when
		calc.divide(a, b);
		
		//then -Should raise exception
	}

}
