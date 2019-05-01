package br.ce.wcaquino.matchers;

import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

	private int diferencaDias;
	
	
	public DataDiferencaDiasMatcher(int diferencaDias) {
		super();
		this.diferencaDias = diferencaDias;
	}

	public void describeTo(Description description) {
		Date date=obterDataComDiferencaDias(this.diferencaDias);
		DateFormat format= new SimpleDateFormat("dd/MM/yyyy");
		description.appendText(format.format(date));

	}

	@Override
	protected boolean matchesSafely(Date date) {
		return DataUtils.isMesmaData(date, DataUtils.obterDataComDiferencaDias(this.diferencaDias));

	}

}
