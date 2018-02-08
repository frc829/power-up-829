package com.digitalgoats.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.auto.AutosGroup;
import com.digitalgoats.systems.SystemsGroup;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechAxis;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The main robot class for FIRST POWER UP
 */
public class Robot extends IterativeRobot {

  // Controllers
  LogitechF310[] controllers = new LogitechF310[2];

  TalonSRX testMotor;

  // Systems and Autos
  SystemsGroup systemsGroup;
  AutosGroup autosGroup;

  @Override
  public void robotInit() {

    // Setup Controllers
    controllers[0] = new LogitechF310(0);
    controllers[1] = new LogitechF310(1);

    testMotor = new TalonSRX(20);

    // Setup Systems and Autos
    systemsGroup = new SystemsGroup();
    autosGroup = new AutosGroup(systemsGroup);

    // Send SendableChooser
    SmartDashboard.putData("AutoChooser", autosGroup.createSendableChooser());

  }

  @Override
  public void disabledInit() {}

  @Override
  public void autonomousInit() {
    autosGroup.setSelectedAuto((SendableChooser<String>)SmartDashboard.getData("AutoChooser"));
  }

  @Override
  public void teleopInit() {}

  @Override
  public void disabledPeriodic() {

    systemsGroup.disabledUpdateSystem();
    systemsGroup.updateSmartDashboard();

  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousPeriodic() {

    autosGroup.executeAutonomous();
    systemsGroup.autonomousUpdateSystem();
    systemsGroup.updateSmartDashboard();

  }

  @Override
  public void teleopPeriodic() {

    testMotor.set(ControlMode.PercentOutput, controllers[1].getAxisValue(LogitechAxis.LEFT_Y));

    systemsGroup.teleopUpdateSystem(controllers[0], controllers[1]);
    SmartDashboard.putNumber("Motor Value", testMotor.getSensorCollection().getPulseWidthPosition());
    systemsGroup.updateSmartDashboard();

  }

}
