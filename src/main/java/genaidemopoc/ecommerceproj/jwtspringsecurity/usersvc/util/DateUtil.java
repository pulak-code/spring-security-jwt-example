package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil extends AppUtil {

	private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
	private static final String DATE_FORMAT_PATTERN = AppConstants.DATE_FORMAT;

	/**
	 * Formats a date into a string with the standard format (yyyy-MM-dd).
	 *
	 * @param date the date to format
	 * @return formatted date as string, or null if the date is null
	 */
	private DateUtil() {
		throw new IllegalStateException(AppConstants.UTILITY_CLASS);
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		return formatter.format(date);
	}

	/**
	 * Parses a date string into a Date object using the standard format
	 * (yyyy-MM-dd).
	 *
	 * @param dateString the date string to parse
	 * @return the parsed Date object, or null if parsing fails
	 */
	public static Date parseDate(String dateString) {
		if (AppUtil.isNullOrEmpty(dateString)) {
			return null;
		}
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);
			return formatter.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Calculates the number of days between a given date and today.
	 *
	 * @param dateString the date string (yyyy-MM-dd) to compare
	 * @return the number of days, or -1 if parsing fails
	 */
	public static long countDaysFrom(String dateString) {
		Date date = parseDate(dateString);
		if (date == null) {
			return -1;
		}
		LocalDate givenDate = LocalDate.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault());
		LocalDate today = LocalDate.now();
		return ChronoUnit.DAYS.between(givenDate, today);
	}
}
