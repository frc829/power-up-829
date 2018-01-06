package com.digitalgoats.systems;

import com.digitalgoats.util.LogitechF310;

/**
 * The interface that represents a particular system for the robot
 * @author Blake
 */
public interface IGoatSystem {

  /**
   * System update periodically called while robot is disabled
   */
  public void disabledUpdate();

  /**
   * System update method called periodically while robot is in teleoperated period
   * @param driver
   *  The controller used by the primary driver
   * @param operator
   *  The controller used by the systems operator
   */
  public void updateSystem(LogitechF310 driver, LogitechF310 operator);

  /**
   * SmartDashboard update method called periodically while robot is in teleoperated period
   */
  public void updateSmartDashboard();

  /**
   * Find the system name for the particular GoatSystem
   * @return
   *  The current GoatSystem's name
   */
  default public String getSystemName() {
    return "No System Name Specified";
  };

}
