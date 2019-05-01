package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;



public class CalculadoraMockTest {
	
	
	@Mock
	private Calculadora calcMock;
	@Spy
	private Calculadora calcSpy;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void diferencaMockSpy(){
		Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
		//Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);//executa somar pelo menos uma vez por causa da ordem de precedencia
		Mockito.doReturn(5).when(calcSpy).somar(1, 2); //para o java isso não é uma chamada de função, seria se estivesse tudo entre ()
		Mockito.doNothing().when(calcSpy).imprime();
		
		System.out.println("Mock:"+ calcMock.somar(1, 3));
		System.out.println("Spy:"+ calcSpy.somar(1, 3)); //Spy chama método real caso expectativa não tenha sido definida
		
		System.out.println("Spy:");
		calcSpy.imprime();
		System.out.println("Mock:");
		calcMock.imprime();
		
	}
	

	@Test
	public void teste() {
		
		//given
		Calculadora calculadora=Mockito.mock(Calculadora.class);
		ArgumentCaptor<Integer> argCap= ArgumentCaptor.forClass(Integer.class);

		Mockito.when(calculadora.somar(Mockito.eq(1), argCap.capture()) ).thenReturn(5); //Todos parametros devem possuir macther caso pelo menos um possua;
		
		//when
		int resultado=calculadora.somar(1,50000);
		
		//System.out.println(argCap.getAllValues());
		 
		
		//then
		assertEquals(5,resultado);		
		
	}
}
