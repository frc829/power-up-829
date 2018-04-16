package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.framework.ISystem;
import com.digitalgoats.robot.SystemMap;
import com.digitalgoats.util.LogitechAxis;
import com.digitalgoats.util.LogitechF310;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator implements ISystem {

  // region Constants

  public static final double WHEEL_DIAMETER = .32;
  public static final int ENC_COUNTS = 1440;
  public static final int ENC_T = 50;
  public static final int MAX_V = 168;
  public static final int PID_SLOT = 0;
  public static final int PID_TIMEOUT = 10;

  // endregion

  // region Fields

  private ControlMode elevatorControlMode;
  private double elevatorSetPoint;

  // endregion

  // region Objects

  private TalonSRX elevatorMaster, elevatorSlave;

  // endregion

  public Elevator() {

    this.setElevatorControlMode(ControlMode.PercentOutput);
    this.setElevatorSetPoint(0);

    this.elevatorMaster = new TalonSRX(SystemMap.Elevator.ELEVATOR_MASTER);
    this.elevatorSlave = new TalonSRX(SystemMap.Elevator.ELEVATOR_SLAVE);

    //this.setupEncoders();
    //this.resetEncoders();

  }

  // region Miscellaneous Functions

  public void setupEncoders() {

    this.elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_SLOT, PID_TIMEOUT);
    this.elevatorMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, ENC_T, PID_TIMEOUT);

  }

  public void resetEncoders() {

    this.elevatorMaster.setSelectedSensorPosition(0, PID_SLOT, PID_TIMEOUT);

  }

  public boolean getForwardSwitch() {
    return this.elevatorMaster.getSensorCollection().isFwdLimitSwitchClosed();
  }

  public boolean getReverseSwitch() {
    return this.elevatorMaster.getSensorCollection().isRevLimitSwitchClosed();
  }

  public double getElevatorPosition() {
    return this.elevatorMaster.getSelectedSensorPosition(PID_SLOT);
  }

  public double getElevatorVelocity() {
    return this.elevatorMaster.getSelectedSensorVelocity(PID_SLOT);
  }

  public double convertStickValue(double value) {
    return value;
  }

  // endregion

  // region Overridden

  @Override
  public void autoInit() {

    this.resetEncoders();

  }

  @Override
  public void shuffleboard() {

    SmartDashboard.putBoolean("Elevator: Elevator Forward", this.getForwardSwitch());
    SmartDashboard.putBoolean("Elevator: Elevator Reverse", this.getReverseSwitch());
    //SmartDashboard.putNumber("Elevator: Elevator Position", this.getElevatorPosition());
    //SmartDashboard.putNumber("Elevator: Elevator Velocity", this.getElevatorVelocity());

  }

  @Override
  public void teleopInit() {

    this.setElevatorControlMode(ControlMode.PercentOutput);

  }

  @Override
  public void teleopUpdate(LogitechF310 driver, LogitechF310 operator) {

    if (Math.abs(convertStickValue(-operator.getAxis(LogitechAxis.LY))) >= .1) {
      this.setElevatorSetPoint(-operator.getAxis(LogitechAxis.LY));
    } else {
      this.setElevatorSetPoint(.0625);
    }

  }

  @Override
  public void update() {

    if (this.getElevatorSetPoint() > 0 && this.elevatorMaster.getSensorCollection().isFwdLimitSwitchClosed()) {
      this.setElevatorSetPoint(.0625);
    } else if (this.getElevatorSetPoint() < 0 && this.elevatorMaster.getSensorCollection().isRevLimitSwitchClosed()) {
      this.setElevatorSetPoint(.0625);
    }
    this.elevatorMaster.set(this.getElevatorControlMode(), this.getElevatorSetPoint());
    this.elevatorSlave.follow(this.elevatorMaster);

  }

  // endregion

  // region Getters & Setters

  public ControlMode getElevatorControlMode() {
    return this.elevatorControlMode;
  }

  public void setElevatorControlMode(ControlMode elevatorControlMode) {
    this.elevatorControlMode = elevatorControlMode;
  }

  public double getElevatorSetPoint() {
    return this.elevatorSetPoint;
  }

  public void setElevatorSetPoint(double elevatorSetPoint) {
    this.elevatorSetPoint = elevatorSetPoint;
  }

  // endregion

  public void goUp(double speed) {
    this.setElevatorControlMode(ControlMode.PercentOutput);
    this.setElevatorSetPoint(speed);
  }

  public void goDown(double speed) {
    this.setElevatorControlMode(ControlMode.PercentOutput);
    this.setElevatorSetPoint(-speed);
  }

  public void stop() {
    this.setElevatorControlMode(ControlMode.PercentOutput);
    this.setElevatorSetPoint(.075);
  }

  public boolean goTop(double speed) {
    if (this.getForwardSwitch()) {
      this.stop();
      return true;
    }
    this.goUp(speed);
    return false;
  }

  public boolean goTop() {
    return this.goTop(1);
  }

  public boolean goBot() {
    return this.goBot(1);
  }

  public boolean goBot(double speed) {
    if (this.getReverseSwitch()) {
      this.stop();
      return true;
    }
    this.goDown(speed);
    return false;
  }

}
