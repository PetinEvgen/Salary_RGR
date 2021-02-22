package application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SalaryTest {

	@Test
	void testFullWage() {
		
		assertEquals(9400.0, Main.getFullWage(10000, 1.12f, 1000, 20, 15),"должно быть 9400");
		assertEquals("111000,945", String.format("%.3f", Main.getFullWage(100000.45f, 1.155f, 1000.45f, 21, 20),"должно быть 111000.945"));
		assertEquals(-1.0, Main.getFullWage(10000, 0, 1000, 20, 15),"должно быть -1");
		assertEquals(-1.0, Main.getFullWage(10000, 1.12f, 1000, 0, 15),"должно быть -1");
		assertEquals(-1.0, Main.getFullWage(0, 0, 0, 20, 15),"должно быть -1");
		assertEquals(1000.0, Main.getFullWage(10000, 1.12f, 1000, 0, 0),"должно быть 1000");
	}
	
	@Test
	void testNDFL() {
		assertEquals(1300, Main.getNDFL(10000, 0.13f),"должно быть 1300");
		assertEquals(-1, Main.getNDFL(-10000, 0.13f),"должно быть -1");
		assertEquals(-1, Main.getNDFL(10000,  -0.13f),"должно быть -1");
	}
	
	@Test
	void testSalary() {
		assertEquals(8700, Main.getSalary(10000, 1300),"должно быть 8700");
		assertEquals(-1, Main.getSalary(-10000, 1300),"должно быть -1");
		assertEquals(-1, Main.getSalary(10000, -1300),"должно быть -1");
	}

}
