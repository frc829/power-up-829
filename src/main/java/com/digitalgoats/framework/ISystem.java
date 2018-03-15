package com.digitalgoats.framework;

import com.digitalgoats.util.LogitechF310;

/**
 * The primary interface for robot systems
 */
public interface ISystem {

  /** Called on autonomous initialization */
  public default void autoInit() {
  }

  /** Called to update shuffleboard */
  public default void shuffleboard() {
  }

  /** Called on teleop initialization */
  public default void teleopInit() {
  }

  /** Called during teleop period */
  public default void teleopUpdate(LogitechF310 driver, LogitechF310 operator) {
  }

  /** Called during each period of the robot */
  public default void update() {
  }

}
