package com.smkw.commerce.inventory.api.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class TimeStamp implements Serializable {

	public TimeStamp() {
		super();
		this.initialize(new Timestamp(System.currentTimeMillis()));
	}

	protected void initialize(Timestamp aTimestamp) {
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime((java.util.Date) aTimestamp);

		this.century = aCalendar.get(Calendar.YEAR) / 100;
		this.year = aCalendar.get(Calendar.YEAR) % 1000;
		this.month = aCalendar.get(Calendar.MONTH) + 1;
		this.day = aCalendar.get(Calendar.DATE);
		this.hour = aCalendar.get(Calendar.HOUR_OF_DAY);
		this.minutes = aCalendar.get(Calendar.MINUTE);
		this.seconds = aCalendar.get(Calendar.SECOND);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3809950577659189931L;
	private int century;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minutes;
	private int seconds;

}
