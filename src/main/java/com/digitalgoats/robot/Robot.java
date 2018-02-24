package com.digitalgoats.robot;

import com.digitalgoats.util.LogitechF310;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

  LogitechF310 driver, operator;

  SystemGroup systemGroup;
  AutoGroup autoGroup;

  public void robotInit() {

    driver = new LogitechF310(0);
    operator = new LogitechF310(1);
    systemGroup = new SystemGroup();

    autoGroup = new AutoGroup(systemGroup);
    autoGroup.addToShuffleboard();

  }

  public void autonomousInit() {
    systemGroup.autoInit();
  }

  public void autonomousPeriodic() {
    systemGroup.update();
  }

  public void teleopInit() {
    systemGroup.teleopInit();
  }

  public void teleopPeriodic() {
    systemGroup.teleopUpdate(driver, operator);
    systemGroup.update();
  }

}
