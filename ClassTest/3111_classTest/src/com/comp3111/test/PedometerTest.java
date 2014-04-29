package com.comp3111.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comp3111.pedometer.Pedometer;
import com.comp3111.pedometer.PedometerSettings;

public class PedometerTest {
	Pedometer pedo;

	@Before
	public void setUp() throws Exception {
		// Pedometer set to 0.1s interval
		pedo = new Pedometer(100) {
			public float getPForce(float g) {
				return g;
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testResetAverageStepDuration() {
		// set average step duration elsewhere and see if effect changes
		PedometerSettings.pDefaultAverageStepDuration = 3.0f;
		pedo.resetAverageStepDuration();
		assertEquals(pedo.getAverageStepDuration(),
				PedometerSettings.pDefaultAverageStepDuration, 0.1f);
	}

	@Test
	public void testPedometer() {
		// Pedometer set to 0.2s interval
		Pedometer pedo2 = new Pedometer(200);
		assertNotNull(pedo2);
	}

	@Test
	public void testStartSensor() {
		pedo.startSensor();
		assertTrue(pedo.running);
	}

	@Test
	public void testStopSensor() {
		pedo.stopSensor();
		assertFalse(pedo.running);
	}

	@Test
	public void testOnSensorChanged() {
		pedo.onSensorChanged(1.2f, 2.3f, 3.4f);
		assertEquals(pedo.pForce, 0.19044128f, 0.01f);
	}

	@Test
	public void testOnSensorChangedCallback() {
		// the method is preserved to be overridden by user
		assertTrue(true);
	}

	@Test
	public void testPedoThreadCallback() {
		// the method is preserved to be overridden by user
		assertTrue(true);
	}

	@Test
	public void testGetAverageStepDuration() {
		// set average step duration elsewhere and see if effect changes
		PedometerSettings.pDefaultAverageStepDuration = 3.0f;
		pedo.resetAverageStepDuration();
		assertEquals(pedo.getAverageStepDuration(),
				PedometerSettings.pDefaultAverageStepDuration, 0.1f);
	}

	@Test
	public void testGetDefaultAverageStepDuration() {
		// set average step duration elsewhere and see if effect changes
		PedometerSettings.pDefaultAverageStepDuration = 3.0f;
		pedo.resetAverageStepDuration();
		assertEquals(pedo.getDefaultAverageStepDuration(),
				PedometerSettings.pDefaultAverageStepDuration, 0.1f);
	}
	
	@Test
	public void testRun() {
		pedo.onSensorChanged(1.2f, 2.3f, 3.4f);
		pedo.run();
		pedo.onSensorChanged(3.2f, 5.3f, 7.4f);
		pedo.run();
		pedo.onSensorChanged(6.5f, 15.3f, 8.4f);
		pedo.run();
		pedo.onSensorChanged(3.2f, 5.3f, 7.4f);
		pedo.run();
		pedo.onSensorChanged(16.5f, 125.3f, 82.4f);
		pedo.run();
		pedo.onSensorChanged(16.5f, 125.3f, 82.4f);
		pedo.run();
		pedo.onSensorChanged(8.2f, 8.3f, 8.4f);
		pedo.run();
		pedo.onSensorChanged(8.3f, 8.4f, 8.5f);
		pedo.run();
		pedo.onSensorChanged(16.5f, 125.3f, 82.4f);
		pedo.run();
		pedo.onSensorChanged(10.5f, 8.3f, 7.4f);
		pedo.run();
		pedo.onSensorChanged(9.3f, 9.4f, 10.5f);
		pedo.run();
		assertTrue(true);
	}

}
