package time;

// Amjed

import java.util.Calendar;
import java.util.GregorianCalendar;

// This DateServer is copied from the Library App, which is made by Hubert Baumeister

public class DateServer {
	
	public Calendar getDate() {
		Calendar calendar = new GregorianCalendar();
		Calendar c = new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
		return c;
	}
}
