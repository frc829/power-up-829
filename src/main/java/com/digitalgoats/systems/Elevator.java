package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.framework.ISystem;
import com.digitalgoats.robot.SystemMap;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechAxis;

public class Elevator implements ISystem {

  public static final double INCH_VALUE = 45.28301886792453;
  public static final double FOOT_VALUE = INCH_VALUE * 12;
  public static final double METER_VALUE = FOOT_VALUE * 0.3048;

  public static final int PIDF_SLOT = 0;
  public static final int PIDF_TIMEOUT = 10;
  public static final double PIDF_P = 0;
  public static final double PIDF_I = 0;
  public static final double PIDF_D = 0;
  public static final double PIDF_F = 0;

  private ControlMode controlMode;
  private double setpoint;

  private TalonSRX stageMaster, stageSlave;

  public Elevator() {

    this.setControlMode(ControlMode.Disabled);
    this.setSetpoint(0);

    this.stageMaster = new TalonSRX(SystemMap.Elevator.STAGE_MASTER);
    this.stageSlave = new TalonSRX(SystemMap.Elevator.STAGE_SLAVE);

  }

  public void setupPIDF() {

    this.stageMaster
        .configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PIDF_SLOT, PIDF_TIMEOUT);
    this.resetEncoders();
    this.stageMaster.setSensorPhase(true);
    this.stageMaster.configNominalOutputForward(0, PIDF_TIMEOUT);
    this.stageMaster.configNominalOutputReverse(0, PIDF_TIMEOUT);
    this.stageMaster.configPeakOutputForward(1, PIDF_TIMEOUT);
    this.stageMaster.configPeakOutputReverse(-1, PIDF_TIMEOUT);
    this.stageMaster.config_kP(PIDF_SLOT, PIDF_P, PIDF_TIMEOUT);
    this.stageMaster.config_kI(PIDF_SLOT, PIDF_I, PIDF_TIMEOUT);
    this.stageMaster.config_kD(PIDF_SLOT, PIDF_D, PIDF_TIMEOUT);
    this.stageMaster.config_kF(PIDF_SLOT, PIDF_F, PIDF_TIMEOUT);
    this.stageMaster.configMotionCruiseVelocity((int)((5 * METER_VALUE)/10), PIDF_TIMEOUT);
    this.stageMaster.configMotionAcceleration((int)((2.5 * METER_VALUE)/10), PIDF_TIMEOUT);

  }

  // region Autonomous Functions

  public void resetEncoders() {
    this.stageMaster.setSelectedSensorPosition(0, PIDF_SLOT, PIDF_TIMEOUT);
  }

  public double getVelocity() {
    return this.stageMaster.getSelectedSensorVelocity(PIDF_SLOT);
  }

  public double getPosition() {
    return this.stageMaster.getSelectedSensorPosition(PIDF_SLOT);
  }

  // endregion

  // region System Functions

  @Override
  public void systemUpdate() {

    this.stageMaster.set(this.getControlMode(), this.getSetpoint());
    this.stageSlave.follow(this.stageMaster);

  }

  @Override
  public void execTeleop(LogitechF310 driver, LogitechF310 operator) {

    double speed = -operator.getAxisValue(LogitechAxis.LEFT_Y);
    if ((speed < 0 && !this.stageMaster.getSensorCollection().isRevLimitSwitchClosed()) || (speed > 0 && !this.stageMaster
        .getSensorCollection().isFwdLimitSwitchClosed())) {
      this.setControlMode(ControlMode.PercentOutput);
      this.setSetpoint(speed);
    } else {
      this.setControlMode(ControlMode.PercentOutput);
      this.setSetpoint(.0625);
    }

  }

  @Override
  public void execDisabled() {

    this.setControlMode(ControlMode.Disabled);
    this.setSetpoint(0);

  }

  @Override
  public String getSystemName() {
    return "Elevator";
  }

  // endregion

  // region Getters & Setters

  public ControlMode getControlMode() {
    return controlMode;
  }

  public void setControlMode(ControlMode controlMode) {
    this.controlMode = controlMode;
  }

  public double getSetpoint() {
    return setpoint;
  }

  public void setSetpoint(double setpoint) {
    this.setpoint = setpoint;
  }

  // endregion

}
