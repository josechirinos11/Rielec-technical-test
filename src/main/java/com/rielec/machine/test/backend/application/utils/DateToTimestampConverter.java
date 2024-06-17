package com.rielec.machine.test.backend.application.utils;

import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.util.Date;

public class DateToTimestampConverter implements Converter<Date, Timestamp> {

	public Timestamp convert(Date source) {
		return new Timestamp(source.getTime());
	}
   
}