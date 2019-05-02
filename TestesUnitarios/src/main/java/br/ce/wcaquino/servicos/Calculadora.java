 package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exception.NaoPodeDividirPorZeroException;

public class Calculadora {

	public int somar(int a, int b) {
		
		return a+b;
	}

	public int subtrair(int a, int b) {
		return a-b;
	} 

	public int divide(int a, int b) {
		if(b==0) {
			throw new NaoPodeDividirPorZeroException();
		}
		return a/b;
	}
	
	public void imprime() {
		System.out.println("Imprime()");
		 
	}

}
