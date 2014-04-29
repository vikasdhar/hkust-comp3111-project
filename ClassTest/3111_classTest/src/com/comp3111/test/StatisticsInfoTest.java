package com.comp3111.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comp3111.pedometer.StatisticsInfo;

public class StatisticsInfoTest {
	
	StatisticsInfo st;

	@Before
	public void setUp() throws Exception {
		st = new StatisticsInfo(68);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddTime() {
		// add 0.1s
		st.addTime(0.1d);
		assertEquals(st.getTimeLasted(), 0.1d, 0.01d);
	}

	@Test
	public void testSetWeight() {
		st.setWeight(100);
		assertEquals(st.weight, 100.0d, 0.01d);
	}

	@Test
	public void testSetStepSize00() {
		st.setStepSize(StatisticsInfo.stepSize.LARGE);
		assertEquals(st.stepLength, 160);
	}
	
	@Test
	public void testSetStepSize01() {
		st.setStepSize(StatisticsInfo.stepSize.MEDIUM);
		assertEquals(st.stepLength, 80);
	}
	
	@Test
	public void testSetStepSize02() {
		st.setStepSize(StatisticsInfo.stepSize.SMALL);
		assertEquals(st.stepLength, 40);
	}

	@Test
	public void testSetTotalSteps() {
		st.setTotalSteps(100);
		assertEquals(st.getTotalSteps(), 100);
	}

	@Test
	public void testGetTotalSteps() {
		st.setTotalSteps(100);
		assertEquals(st.getTotalSteps(), 100);
	}

	@Test
	public void testGetWeight() {
		st.setWeight(100);
		assertEquals(st.getWeight(), 100.0d, 0.01d);
	}

	@Test
	public void testGetStepsize() {
		st.setStepSize(StatisticsInfo.stepSize.LARGE);
		assertEquals(st.getStepsize(), StatisticsInfo.stepSize.LARGE);
	}

	@Test
	public void testGetCaloriesBurn() {
		st.setTotalSteps(100);
		st.setWeight(65);
		st.metValue = 5;
		assertEquals(st.getCaloriesBurn(), 5 * 65 * (100 / 360.0), 0.1f);
	}

	@Test
	public void testGetDistanceTravelled() {
		st.setStepSize(StatisticsInfo.stepSize.LARGE);
		st.setTotalSteps(100);
		assertEquals(st.getDistanceTravelled(), (160 * 100) / 160.0, 0.1f);
	}
/*
	@Test
	public void testGetStepPerTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDistancePerTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTimeLasted() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDateString() {
		fail("Not yet implemented");
	}

	@Test
	public void testToJSON() {
		fail("Not yet implemented");
	}
*/
}
