package com.aj.options;


import com.aj.options.entity.Option;
import com.aj.options.service.OptionService;
import com.fasterxml.jackson.datatype.jdk8.OptionalSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;


import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//@SpringBootTest
class OptionTest {

	//@Autowired
	private OptionService service = new OptionService();

	@Test
	public void testParser() throws Exception {
		String ulSymbol = "SPY";
		List<Long> expirys = service.getExpirys(ulSymbol);
		assertNotNull(expirys);
	}

	@Test
	public void testExpiry() {
		System.out.println(new Date(1598400000L * 1000));
	}
}
