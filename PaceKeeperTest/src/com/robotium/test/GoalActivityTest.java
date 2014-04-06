/*
 * This is an example test project created in Eclipse to test NotePad which is a sample 
 * project located in AndroidSDK/samples/android-11/NotePad
 * 
 * 
 * You can run these test cases either on the emulator or on device. Right click
 * the test project and select Run As --> Run As Android JUnit Test
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

package com.robotium.test;

import com.comp3111.pacekeeper.GoalActivity;
import com.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;



public class GoalActivityTest extends ActivityInstrumentationTestCase2<GoalActivity>{

	private Solo solo;

	public GoalActivityTest() {
		super(GoalActivity.class);

	}

	@Override
	public void setUp() throws Exception {
		//setUp() is run before a test case is started. 
		//This is where the solo object is created.
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		//tearDown() is run after a test case has finished. 
		//finishOpenedActivities() will finish all the activities that have been opened during the test execution.
		solo.finishOpenedActivities();
	}

	public void testTimeGoal() throws Exception {
		// select time goal with 30 min
		solo.clickOnButton("Time goal");
		solo.clickLongOnText("30 minutes");
		solo.assertCurrentActivity("Expected MusicActivity", "MusicActivity");
		boolean timeGoalDetermined = solo.searchText("Time Goal");
		assertTrue("Time Goal is not set", timeGoalDetermined);
	}
	
	public void testStepGoal() throws Exception {
		// select time goal with 30 min
		solo.clickOnButton("Step goal");
		solo.clickLongOnText("5000 steps");
		solo.assertCurrentActivity("Expected MusicActivity", "MusicActivity");
		boolean timeGoalDetermined = solo.searchText("Step Goal");
		assertTrue("Step Goal is not set", timeGoalDetermined);
	}
	
	public void testDistanceGoal() throws Exception {
		// select time goal with 30 min
		solo.clickOnButton("Distance goal");
		solo.clickLongOnText("1000 miles");
		solo.assertCurrentActivity("Expected MusicActivity", "MusicActivity");
		boolean timeGoalDetermined = solo.searchText("Distance Goal");
		assertTrue("Distance Goal is not set", timeGoalDetermined);
	}
	
	public void testCardioGoal() throws Exception {
		// select time goal with 30 min
		solo.clickOnButton("Cardio goal");
		solo.clickLongOnText("1 hour");
		solo.assertCurrentActivity("Expected MusicActivity", "MusicActivity");
		boolean timeGoalDetermined = solo.searchText("Cardio Goal");
		assertTrue("Cardio Goal is not set", timeGoalDetermined);
	}
}
