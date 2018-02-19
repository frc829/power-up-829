package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.framework.ISystem;
import com.digitalgoats.robot.SystemMap;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechAxis;
import com.digitalgoats.util.LogitechF310.LogitechButton;
import edu.wpi.first.wpilibj.Solenoid;

public class Drive implements ISystem {

  public static final long TRANS_DELAY = 500;

  public static final double INCH_VALUE = 45.28301886792453;
  public static final double FOOT_VALUE = INCH_VALUE * 12;
  public static final double METER_VALUE = FOOT_VALUE * 0.3048;

  public static final int PIDF_SLOT = 0;
  public static final int PIDF_TIMEOUT = 10;
  public static final double PIDF_P = 1.5;
  public static final double PIDF_I = 0;
  public static final double PIDF_D = 0;
  public static final double PIDF_F = 8;

  private boolean transmissionStatus;
  private ControlMode controlMode;
  private double rightSetPoint, leftSetPoint;
  private long transmissionTime;

  private Solenoid transmission;
  private TalonSRX frontRight, midRight, backRight;
  private TalonSRX frontLeft, midLeft, backLeft;

  public Drive() {

    this.setTransmissionStatus(false);
    this.setControlMode(ControlMode.Disabled);
    this.setRightSetPoint(0);
    this.setLeftSetPoint(0);
    this.setTransmissionTime(System.currentTimeMillis());

    this.transmission = new Solenoid(SystemMap.Drive.TRANS_PCM, SystemMap.Drive.TRANS_PORT);

    this.frontRight = new TalonSRX(SystemMap.Drive.FRONT_RIGHT);
    this.midRight = new TalonSRX(SystemMap.Drive.MID_RIGHT);
    this.backRight = new TalonSRX(SystemMap.Drive.BACK_RIGHT);
    this.midRight.setInverted(true);

    this.frontLeft = new TalonSRX(SystemMap.Drive.FRONT_LEFT);
    this.midLeft = new TalonSRX(SystemMap.Drive.MID_LEFT);
    this.backLeft = new TalonSRX(SystemMap.Drive.BACK_LEFT);
    this.backLeft.setInverted(true);

    this.setupPIDF();

  }

  public void setupPIDF() {

    this.backRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PIDF_SLOT, PIDF_TIMEOUT);
    this.backRight.setSensorPhase(true);
    this.backRight.configNominalOutputForward(0, PIDF_TIMEOUT);
    this.backRight.configNominalOutputReverse(0, PIDF_TIMEOUT);
    this.backRight.configPeakOutputForward(1, PIDF_TIMEOUT);
    this.backRight.configPeakOutputReverse(-1, PIDF_TIMEOUT);
    this.backRight.config_kP(PIDF_SLOT, PIDF_P, PIDF_TIMEOUT);
    this.backRight.config_kI(PIDF_SLOT, PIDF_I, PIDF_TIMEOUT);
    this.backRight.config_kD(PIDF_SLOT, PIDF_D, PIDF_TIMEOUT);
    this.backRight.config_kF(PIDF_SLOT, PIDF_F, PIDF_TIMEOUT);
    this.backRight.configMotionCruiseVelocity((int)((5 * METER_VALUE)/10), PIDF_TIMEOUT);
    this.backRight.configMotionAcceleration((int)((2.5 * METER_VALUE)/10), PIDF_TIMEOUT);

    this.backLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PIDF_SLOT, PIDF_TIMEOUT);
    this.backLeft.setSensorPhase(true);
    this.backLeft.configNominalOutputForward(0, PIDF_TIMEOUT);
    this.backLeft.configNominalOutputReverse(0, PIDF_TIMEOUT);
    this.backLeft.configPeakOutputForward(1, PIDF_TIMEOUT);
    this.backLeft.configPeakOutputReverse(-1, PIDF_TIMEOUT);
    this.backLeft.config_kP(PIDF_SLOT, PIDF_P, PIDF_TIMEOUT);
    this.backLeft.config_kI(PIDF_SLOT, PIDF_I, PIDF_TIMEOUT);
    this.backLeft.config_kD(PIDF_SLOT, PIDF_D, PIDF_TIMEOUT);
    this.backLeft.config_kF(PIDF_SLOT, PIDF_F, PIDF_TIMEOUT);
    this.backLeft.configMotionCruiseVelocity((int)((5 * METER_VALUE)/10), PIDF_TIMEOUT);
    this.backLeft.configMotionAcceleration((int)((2.5 * METER_VALUE)/10), PIDF_TIMEOUT);

    this.resetEncoders();

  }

  // region Autonomous Functions

  public void resetEncoders() {

    this.backRight.setSelectedSensorPosition(0, PIDF_SLOT, PIDF_TIMEOUT);
    this.backLeft.setSelectedSensorPosition(0, PIDF_SLOT, PIDF_TIMEOUT);

  }

  public double getRightVelocity() {
    return this.backRight.getSelectedSensorVelocity(PIDF_SLOT);
  }

  public double getLeftVelocity() {
    return this.backLeft.getSelectedSensorVelocity(PIDF_SLOT);
  }

  public double getRightPosition() {
    return this.backRight.getSelectedSensorPosition(PIDF_SLOT);
  }

  public double getLeftPosition() {
    return this.backLeft.getSelectedSensorPosition(PIDF_SLOT);
  }

  // endregion

  // region System Functions

  @Override
  public void systemUpdate() {

    this.transmission.set(this.isTransmissionStatus());

    this.backRight.set(this.getControlMode(), this.getRightSetPoint());
    this.frontRight.follow(this.backRight);
    this.midRight.follow(this.backRight);

    this.backLeft.set(this.getControlMode(), this.getLeftSetPoint());
    this.frontLeft.follow(this.backLeft);
    this.midLeft.follow(this.backLeft);

  }

  @Override
  public void execTeleop(LogitechF310 driver, LogitechF310 operator) {

    this.setControlMode(ControlMode.PercentOutput);
    this.setRightSetPoint(-driver.getAxisValue(LogitechAxis.RIGHT_Y));
    this.setLeftSetPoint(-driver.getAxisValue(LogitechAxis.LEFT_Y));

    if (driver.getButtonValue(LogitechButton.BUT_BACK)) {
      if (System.currentTimeMillis() - this.getTransmissionTime() >= TRANS_DELAY) {
        this.setTransmissionTime(System.currentTimeMillis());
        this.setTransmissionStatus(!this.isTransmissionStatus());
      }
    }

  }

  @Override
  public void execDisabled() {

    this.setControlMode(ControlMode.Disabled);
    this.setRightSetPoint(0);
    this.setLeftSetPoint(0);

  }

  @Override
  public String getSystemName() {
    return "Drive";
  }

  // endregion

  // region Getters & Setters

  public boolean isTransmissionStatus() {
    return this.transmissionStatus;
  }

  public void setTransmissionStatus(boolean transmissionStatus) {
    this.transmissionStatus = transmissionStatus;
  }

  public ControlMode getControlMode() {
    return this.controlMode;
  }

  public void setControlMode(ControlMode controlMode) {
    this.controlMode = controlMode;
  }

  public double getRightSetPoint() {
    return this.rightSetPoint;
  }

  public void setRightSetPoint(double rightSetPoint) {
    this.rightSetPoint = rightSetPoint;
  }

  public double getLeftSetPoint() {
    return this.leftSetPoint;
  }

  public void setLeftSetPoint(double leftSetPoint) {
    this.leftSetPoint = leftSetPoint;
  }

  public long getTransmissionTime() {
    return this.transmissionTime;
  }

  public void setTransmissionTime(long transmissionTime) {
    this.transmissionTime = transmissionTime;
  }

  // endregion

}
