package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;



public class CalculadoraMockTest {

	@Test
	public void teste() {
		
		//given
		Calculadora calculadora=Mockito.mock(Calculadora.class);
		Mockito.when(calculadora.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5); //Todos parametros devem possuir macther caso pelo menos um possua;
		
		//when
		int resultado=calculadora.somar(1,50000);
		System.out.println(resultado);
		
		//then
		assertEquals(5,resultado);		
		
	}
}
