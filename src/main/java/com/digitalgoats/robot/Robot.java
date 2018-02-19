package com.digitalgoats.robot;

import com.digitalgoats.util.LogitechF310;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The main robot class for FIRST POWER UP
 */
public class Robot extends IterativeRobot {

  public LogitechF310 driver, operator;

  public SystemGroup systems;
  public AutoGroup autos;

  @Override
  public void robotInit() {

    driver = new LogitechF310(0);
    operator = new LogitechF310(1);

    systems = new SystemGroup();
    autos = new AutoGroup(systems);

  }

  @Override
  public void autonomousInit() {}

  @Override
  public void teleopInit() {}

  @Override
  public void disabledPeriodic() {
    systems.disabledPeriodic();
    systems.systemsUpdate();
  }

  @Override
  public void autonomousPeriodic() {
    autos.execute();
    systems.systemsUpdate();
  }

  @Override
  public void teleopPeriodic() {
    systems.teleopPeriodic(driver, operator);
    systems.systemsUpdate();
  }

}
