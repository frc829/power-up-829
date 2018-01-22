package com.digitalgoats.systems;

import com.digitalgoats.util.LogitechF310;

/**
 * The system for controlling the arm
 * @author Blake
 */
public class Arm implements IGoatSystem {

  // region Constants

  // endregion

  // region Fields

  // endregion

  // region Objects

  // endregion

  // region Constructor

  // endregion

  // region Autonomous Methods

  // endregion

  // region Update Methods

  // endregion

  // region Getters & Setters

  // endregion

  // region Overridden Methods

  @Override
  public void disabledUpdateSystem() {
  }

  @Override
  public void autonomousUpdateSystem() {
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {
  }

  @Override
  public void updateSmartDashboard() {
  }

  @Override
  public String getSystemName() {
    return "Example";
  }

  // endregion

}
