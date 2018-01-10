package com.digitalgoats.robot;

import com.digitalgoats.systems.Systems;
import com.digitalgoats.util.LogitechF310;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The main robot class for FIRST POWER UP
 */
public class Robot extends IterativeRobot {

  // Objects
  LogitechF310[] controllers = new LogitechF310[2];
  Systems systemsGroup;

  @Override
  public void robotInit() {

    controllers[0] = new LogitechF310(0);
    controllers[1] = new LogitechF310(1);
    systemsGroup = new Systems();

  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void disabledPeriodic() {

    systemsGroup.disabledUpdateSystem();

  }

  @Override
  public void autonomousPeriodic() {

    systemsGroup.autonomousUpdateSystem();

  }

  @Override
  public void teleopPeriodic() {

    systemsGroup.teleopUpdateSystem(controllers[0], controllers[1]);

  }

}
