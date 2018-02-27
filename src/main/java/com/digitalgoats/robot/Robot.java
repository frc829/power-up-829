package com.digitalgoats.robot;

import com.digitalgoats.util.LogitechF310;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

  Compressor compressor = new Compressor(1);
  LogitechF310 driver, operator;

  SystemGroup systemGroup;
  AutoGroup autoGroup;

  public void robotInit() {

    compressor.start();
    driver = new LogitechF310(0);
    operator = new LogitechF310(1);
    systemGroup = new SystemGroup();

    autoGroup = new AutoGroup(systemGroup);
    autoGroup.addToShuffleboard();

  }

  public void robotPeriodic() {
    systemGroup.update();
    systemGroup.shuffleboard();
  }

  public void autonomousInit() {
    systemGroup.autoInit();
    autoGroup.autoInit();
  }

  public void autonomousPeriodic() {
    autoGroup.execute();
  }

  public void teleopInit() {
    systemGroup.teleopInit();
  }

  public void teleopPeriodic() {
    systemGroup.teleopUpdate(driver, operator);
  }

}
