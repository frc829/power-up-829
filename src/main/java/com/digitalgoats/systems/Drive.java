package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.framework.ISystem;
import com.digitalgoats.robot.SystemMap;
import com.digitalgoats.util.LogitechAxis;
import com.digitalgoats.util.LogitechButton;
import com.digitalgoats.util.LogitechF310;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive implements ISystem {

  // region Constants

  public static final double WHEEL_DIAMETER = 3;
  public static final int ENC_COUNTS = 1440;
  public static final int ENC_T = 50;
  public static final int MAX_V_L = 720;
  public static final int MAX_V_R = 720;
  public static final int PID_SLOT = 0;
  public static final int PID_TIMEOUT = 10;
  public static final long TRANS_DELAY = 500;

  // endregion

  // region Fields

  private boolean transmissionStatus;
  private ControlMode driveControlMode;
  private double leftSetPoint, rightSetPoint;
  private long transmissionTime;

  // endregion

  // region Objects

  private Solenoid transmission;
  private TalonSRX frontLeft, midLeft, backLeft;
  private TalonSRX frontRight, midRight, backRight;

  // endregion

  public Drive() {

    this.setTransmissionStatus(false);
    this.setDriveControlMode(ControlMode.PercentOutput);
    this.setLeftSetPoint(0);
    this.setRightSetPoint(0);
    this.setTransmissionTime(0);

    this.transmission = new Solenoid(SystemMap.Drive.PCM, SystemMap.Drive.TRANS_CHANNEL);
    this.frontLeft = new TalonSRX(SystemMap.Drive.FRONT_LEFT);
    this.midLeft = new TalonSRX(SystemMap.Drive.MID_LEFT);
    this.backLeft = new TalonSRX(SystemMap.Drive.BACK_LEFT);
    this.frontRight = new TalonSRX(SystemMap.Drive.FRONT_RIGHT);
    this.midRight = new TalonSRX(SystemMap.Drive.MID_RIGHT);
    this.backRight = new TalonSRX(SystemMap.Drive.BACK_RIGHT);

    this.frontLeft.setInverted(true);
    this.backLeft.setInverted(true);
    this.midLeft.setInverted(true);
    this.frontRight.setInverted(true);
    this.midRight.setInverted(false);
    this.backRight.setInverted(true);

    this.setupEncoders();
    this.resetEncoders();

  }

  // region Miscellaneous Functions

  public void setupEncoders() {

    this.backLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_SLOT, PID_TIMEOUT);
    this.backRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_SLOT, PID_TIMEOUT);

    this.backLeft.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, ENC_T, PID_TIMEOUT);
    this.backRight.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, ENC_T, PID_TIMEOUT);

    this.backLeft.setSensorPhase(true);
    this.backRight.setSensorPhase(false);

  }

  public void resetEncoders() {

    this.backLeft.setSelectedSensorPosition(0, PID_SLOT, PID_TIMEOUT);
    this.backRight.setSelectedSensorPosition(0, PID_SLOT, PID_TIMEOUT);

  }

  public double getLeftPosition() {
    return this.backLeft.getSelectedSensorPosition(PID_SLOT);
  }

  public double getLeftVelocity() {
    return this.backLeft.getSelectedSensorVelocity(PID_SLOT);
  }

  public double getRightPosition() {
    return this.backRight.getSelectedSensorPosition(PID_SLOT);
  }

  public double getRightVelocity() {
    return this.backRight.getSelectedSensorVelocity(PID_SLOT);
  }

  public double convertStickValue(double value) {
    return (.65*Math.pow(value, 3)) + (.35*value);
  }

  public boolean canShiftTransmission() {
    return System.currentTimeMillis() - this.getTransmissionTime() >= TRANS_DELAY;
  }

  // endregion

  // region Overridden

  @Override
  public void autoInit() {

    this.resetEncoders();

  }

  @Override
  public void shuffleboard() {

    SmartDashboard.putNumber("Drive: Left Position", this.getLeftPosition());
    SmartDashboard.putNumber("Drive: Left Velocity", this.getLeftVelocity());
    SmartDashboard.putNumber("Drive: Right Position", this.getRightPosition());
    SmartDashboard.putNumber("Drive: Right Velocity", this.getRightVelocity());
    SmartDashboard.putBoolean("Drive: Transmission", this.isTransmissionStatus());

  }

  @Override
  public void teleopInit() {

    this.setDriveControlMode(ControlMode.PercentOutput);
    this.setLeftSetPoint(0);
    this.setRightSetPoint(0);

  }

  @Override
  public void teleopUpdate(LogitechF310 driver, LogitechF310 operator) {

    this.setLeftSetPoint(convertStickValue(-driver.getAxis(LogitechAxis.LY)));
    this.setRightSetPoint(convertStickValue(-driver.getAxis(LogitechAxis.RY)));

    if (driver.getButton(LogitechButton.BACK) && this.canShiftTransmission()) {
      this.setTransmissionStatus(!this.isTransmissionStatus());
      this.setTransmissionTime(System.currentTimeMillis());
    }

  }

  @Override
  public void update() {

    this.transmission.set(this.isTransmissionStatus());

    this.backLeft.set(this.getDriveControlMode(), this.getLeftSetPoint());
    this.midLeft.follow(this.backLeft);
    this.frontLeft.follow(this.backLeft);

    this.backRight.set(this.getDriveControlMode(), this.getRightSetPoint());
    this.midRight.follow(this.backRight);
    this.frontRight.follow(this.backRight);

  }

  // endregion

  // region Getters & Setters

  public boolean isTransmissionStatus() {
    return this.transmissionStatus;
  }

  public void setTransmissionStatus(boolean transmissionStatus) {
    this.transmissionStatus = transmissionStatus;
  }

  public ControlMode getDriveControlMode() {
    return this.driveControlMode;
  }

  public void setDriveControlMode(ControlMode driveControlMode) {
    this.driveControlMode = driveControlMode;
  }

  public double getLeftSetPoint() {
    return this.leftSetPoint;
  }

  public void setLeftSetPoint(double leftSetPoint) {
    this.leftSetPoint = leftSetPoint;
  }

  public double getRightSetPoint() {
    return this.rightSetPoint;
  }

  public void setRightSetPoint(double rightSetPoint) {
    this.rightSetPoint = rightSetPoint;
  }

  public long getTransmissionTime() {
    return this.transmissionTime;
  }

  public void setTransmissionTime(long transmissionTime) {
    this.transmissionTime = transmissionTime;
  }

  public Solenoid getTransmission() {
    return this.transmission;
  }

  public void setTransmission(Solenoid transmission) {
    this.transmission = transmission;
  }

  // endregion

}
