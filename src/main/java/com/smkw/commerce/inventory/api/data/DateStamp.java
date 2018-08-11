package com.smkw.commerce.inventory.api.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Embeddable
@AllArgsConstructor
public class DateStamp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5353873437945548877L;
	private int century;
	private int year;
	private int month;
	private int day;

	public DateStamp() {
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
	}

	public Date getDate() {
		Calendar aCal = Calendar.getInstance();
		aCal.set(this.century * 100 + this.year, this.month, this.day);
		return aCal.getTime();
	}
}
