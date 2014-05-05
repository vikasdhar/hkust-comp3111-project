package com.comp3111.pacekeeper.test;

import android.app.Instrumentation.ActivityMonitor;
import android.test.TouchUtils;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.comp3111.pacekeeper.GreetActivity;
import com.comp3111.pacekeeper.MusicActivity;
import com.comp3111.pacekeeper.R;
import com.comp3111.pedometer.Pedometer;
import com.robotium.solo.Solo;

public class GreetActivityTest extends android.test.ActivityInstrumentationTestCase2<GreetActivity> {

  private View viewId;
  private GreetActivity activity;
  private ActivityMonitor activityMonitor;
  private Solo solo;
  
  public GreetActivityTest() {
    super(GreetActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), getActivity());
  }
  
  @Override
  public void tearDown() throws Exception {
	//tearDown() is run after a test case has finished. 
	//finishOpenedActivities() will finish all the activities that have been opened during the test execution.
	solo.finishOpenedActivities();
  }

  public void testMusicActivity() throws InterruptedException {
	  Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
	  @SuppressWarnings("deprecation")
	  int width = display.getWidth();
	  int height = display.getHeight();
	  
	  // test for music activity
	  activityMonitor = getInstrumentation().addMonitor(MusicActivity.class.getName(), null, false);
	  solo.clickOnScreen(width / 4, (int) (height * 0.5));
	  MusicActivity startedActivity = (MusicActivity) activityMonitor.waitForActivityWithTimeout(2000);
	  assertNotNull(startedActivity);
	  solo.clickOnScreen(width / 2, (int) (height * 0.5));
	  
	  // dragging test
	  float xStart = width - 100 ;
	  float xEnd = 100;
	  solo.drag(xStart, xEnd, 1000, 1000, 12);	  
	  solo.drag(xEnd, xStart, 1000, 1000, 12);	 
	  
	  // start stop test
	  activity = getActivity();
	  //viewId = solo.getView(com.comp3111.pacekeeper.R.id.mus_btn_trigger);
	  solo.clickOnScreen(xStart / 2, (int) (height * 0.5));
	  Thread.sleep(1000);
	  solo.clickOnScreen(xStart / 2, (int) (height * 0.5));
	   
	  // simulate pedometer action
	  Pedometer mShaker = new Pedometer(activity.getApplicationContext(), 100);
	  mShaker.setPForce(1.5f);
	  mShaker.pedoThreadRunAlgorithm();
	  mShaker.setPForce(1.0f);
	  mShaker.pedoThreadRunAlgorithm();
	  mShaker.setPForce(1.5f);
	  mShaker.pedoThreadRunAlgorithm();
	  mShaker.setPForce(1.8f);
	  mShaker.pedoThreadRunAlgorithm();
	  mShaker.setPForce(1.2f);
	  mShaker.pedoThreadRunAlgorithm();
	  mShaker.setPForce(1.0f);
	  mShaker.pedoThreadRunAlgorithm();
	  
  }

  public void testGoalActivity01() {
	  Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
	  @SuppressWarnings("deprecation")
	  int width = display.getWidth();
	  int height = display.getHeight();
	  // test for goal activity
	  solo.clickOnScreen(width * 3 / 4, (int) (height * 0.5));
	  solo.assertCurrentActivity("Expected GoalActivity", "GoalActivity");
	  // select time goal with 30 min
	  solo.clickOnButton("Time goal");
	  solo.clickLongOnText("30 minutes");
	  solo.assertCurrentActivity("Expected MusicActivity", "MusicActivity");
	  solo.clickOnScreen(width *3/4, (int) (height * 0.5));
	  boolean goalDetermined = solo.searchText("Time Goal");
	  assertTrue("Time Goal is not set", goalDetermined);
  }

  public void testGoalActivity02() {
	  // test for goal activity
	  Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
	  @SuppressWarnings("deprecation")
	  int width = display.getWidth();
	  int height = display.getHeight();
	  // test for goal activity
	  solo.clickOnScreen(width * 3 / 4, (int) (height * 0.5));
	  solo.assertCurrentActivity("Expected GoalActivity", "GoalActivity");
	  // select step goal with 5000 steps
	  solo.clickOnButton("Step goal");
	  solo.clickLongOnText("5000 steps");
	  solo.assertCurrentActivity("Expected MusicActivity", "MusicActivity");
	  solo.clickOnScreen(width / 4, (int) (height * 0.5));
	  boolean goalDetermined = solo.searchText("Step Goal");
	  assertTrue("Step Goal is not set", goalDetermined);
  }
  
  public void testGoalActivity03() {
	  Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
	  @SuppressWarnings("deprecation")
	  int width = display.getWidth();
	  int height = display.getHeight();
	  // test for goal activity
	  solo.clickOnScreen(width * 3 / 4, (int) (height * 0.5));
	  solo.assertCurrentActivity("Expected GoalActivity", "GoalActivity");
	  // select dist goal with 1000 miles
	  solo.clickOnButton("Distance goal");
	  solo.clickLongOnText("1000 miles");
	  solo.assertCurrentActivity("Expected MusicActivity", "MusicActivity");
	  solo.clickOnScreen(width / 2, (int) (height * 0.5));
	  boolean goalDetermined = solo.searchText("Distance Goal");
	  assertTrue("Distance Goal is not set", goalDetermined);
  }
  
  public void testGoalActivity04() {
	  Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
	  @SuppressWarnings("deprecation")
	  int width = display.getWidth();
	  int height = display.getHeight();
	  // test for goal activity
	  solo.clickOnScreen(width * 3 / 4, (int) (1280 * 0.5));
	  solo.assertCurrentActivity("Expected GoalActivity", "GoalActivity");
	  // select cardio goal with 1 hr
	  solo.clickOnButton("Cardio goal");
	  solo.clickLongOnText("1 hour");
	  solo.assertCurrentActivity("Expected MusicActivity", "MusicActivity");
	  solo.clickOnScreen(width / 2, (int) (height * 0.5));
	  boolean goalDetermined = solo.searchText("Cardio Goal");
	  assertTrue("Cardio Goal is not set", goalDetermined);
  }
  
  public void testCalibrateActivity() {
	  Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
	  @SuppressWarnings("deprecation")
	  int width = display.getWidth();
	  int height = display.getHeight();
	  // test for profile activity
	  viewId = activity.findViewById(com.comp3111.pacekeeper.R.id.greet_profilelist);
	  TouchUtils.clickView(this, viewId);
	  solo.assertCurrentActivity("Expected ProfileListActivity", "ProfileListActivity");
	  // add a profile
	  solo.clickOnScreen(width / 2, height - 20);
	  // edit a profile
	  solo.clickOnText("Default");
	  solo.clickOnImageButton(0);
	  //getCurrentListViews
	  /*
	  // fill contents
	  activity = getActivity();
	  EditText textname = (EditText) activity.findViewById(com.comp3111.pacekeeper.R.id.Name);
	  EditText textemail = (EditText) activity.findViewById(com.comp3111.pacekeeper.R.id.Email);
	  EditText textage = (EditText) activity.findViewById(com.comp3111.pacekeeper.R.id.Age);
	  EditText textjogging = (EditText) activity.findViewById(com.comp3111.pacekeeper.R.id.jogging);
	  EditText textwalking = (EditText) activity.findViewById(com.comp3111.pacekeeper.R.id.walking);
	  EditText textsprinting = (EditText) activity.findViewById(com.comp3111.pacekeeper.R.id.sprinting);

	  solo.enterText(textname, "TestName");
	  solo.enterText(textemail, "TestEmail");
	  solo.enterText(textage, "TestAge");
	  solo.enterText(textjogging, "TestJogging");
	  solo.enterText(textwalking, "TestWalking");
	  solo.enterText(textsprinting, "TestSprinting");
	  // click the add_profile button
	  solo.clickOnButton("ok");*/
  }

} 
