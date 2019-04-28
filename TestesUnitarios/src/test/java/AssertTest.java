

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void assertTest() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		Assert.assertEquals(1, 1); //Primeiro valor e o esperado, o segundo e o real
		Assert.assertEquals(0.23,0.23,0.01); //Precisa declarar a precisão da comparação
		Assert.assertEquals(Math.PI, 3.14,0.01);
		
		int i=5;
		Integer i2=5;
		Assert.assertEquals(i, i2.intValue()); //Não compara tipo primitivo com objeto
		//Assert.assertEquals(Integer.valueOf(i),i2);
		
		Assert.assertEquals("bola","bola");
		Assert.assertNotEquals("bola","Carro");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola")); //Not case sensitive
		Assert.assertTrue("bola".startsWith("bo"));
		
		
		Usuario u1=new Usuario("U1");
		Usuario u2=new Usuario("U1");
		Assert.assertEquals(u1, u2);
		Assert.assertSame(u1, u1);//Compara se são mesma instância
		
		
		Usuario u3=null;
		Assert.assertNull(u3);
		
		Assert.assertEquals("Erro de comparação",1, 1); //String para quando ocorre um erro
		
		
		//AssertThat: Verifique que...
		
		
		
		
	}

}
