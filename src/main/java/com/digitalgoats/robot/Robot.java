package com.digitalgoats.robot;

import com.digitalgoats.auto.AutosGroup;
import com.digitalgoats.systems.SystemsGroup;
import com.digitalgoats.util.LogitechF310;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The main robot class for FIRST POWER UP
 */
public class Robot extends IterativeRobot {

  // Controllers
  LogitechF310[] controllers = new LogitechF310[2];

  // Systems and Autos
  SystemsGroup systemsGroup;
  AutosGroup autosGroup;

  @Override
  public void robotInit() {

    // Setup Controllers
    controllers[0] = new LogitechF310(0);
    controllers[1] = new LogitechF310(1);

    // Setup Systems and Autos
    systemsGroup = new SystemsGroup();
    autosGroup = new AutosGroup(systemsGroup);

    // Send SendableChooser
    SmartDashboard.putData("AutoChooser", autosGroup.createSendableChooser());

  }

  @Override
  public void autonomousInit() {
    autosGroup.setSelectedAuto((SendableChooser<String>)SmartDashboard.getData("AutoChooser"));
  }

  @Override
  public void disabledPeriodic() {

    systemsGroup.disabledUpdateSystem();
    systemsGroup.updateSmartDashboard();

  }

  @Override
  public void autonomousPeriodic() {

    autosGroup.executeAutonomous();
    systemsGroup.autonomousUpdateSystem();
    systemsGroup.updateSmartDashboard();

  }

  @Override
  public void teleopPeriodic() {

    systemsGroup.teleopUpdateSystem(controllers[0], controllers[1]);
    systemsGroup.updateSmartDashboard();

  }

}
