package br.ce.wcaquino.matchers;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DiaSemanaMatcher extends TypeSafeMatcher<Date> { //Recebe um date (valor real)

	
	private Integer diaSemana; //valor esperado
	
	
	
	public DiaSemanaMatcher(Integer diaSemana) {
		this.diaSemana=diaSemana;
	}

	public void describeTo(Description description) { //Mensagem Erro
		Calendar data= Calendar.getInstance();
		data.set(Calendar.DAY_OF_WEEK, diaSemana);
		String dataString= data.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, new Locale("en","US"));
		
		description.appendText(dataString);

	}

	@Override
	protected boolean matchesSafely(Date date) { //Onde realiza a comparacao
		return DataUtils.verificarDiaSemana(date, diaSemana);
	}

}
