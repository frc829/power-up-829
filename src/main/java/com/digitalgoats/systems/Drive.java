package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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

  public boolean cross = false;

  public static final double COUNT_INCH_LOW = 43.64631578947368;
  public static final double COUNT_INCH_HIGH = 36.99705014749263;

  public static final int ENC_T = 10;
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

    this.frontLeft.setInverted(true); // P: 1 C: 1
    this.backLeft.setInverted(true); // P: 1 C: 1
    this.midLeft.setInverted(true); // P: 1 C: 1
    this.frontRight.setInverted(false); // P: 1 C: 0
    this.midRight.setInverted(false); // P: 0 C: 0
    this.backRight.setInverted(false); // P: 1 C: 0

    this.setupEncoders();
    this.resetEncoders();

  }

  // region Miscellaneous Functions

  public void setupEncoders() {

    this.backLeft.selectProfileSlot(PID_SLOT, PID_SLOT);
    this.backRight.selectProfileSlot(PID_SLOT, PID_SLOT);

    this.backLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_SLOT, PID_TIMEOUT);
    this.backRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_SLOT, PID_TIMEOUT);

    this.backLeft.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, ENC_T, PID_TIMEOUT);
    this.backRight.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, ENC_T, PID_TIMEOUT);

    this.backLeft.config_kP(PID_SLOT, 1.5, PID_TIMEOUT);
    this.backLeft.config_kI(PID_SLOT, 0, PID_TIMEOUT);
    this.backLeft.config_kD(PID_SLOT, 0, PID_TIMEOUT);
    this.backLeft.config_kF(PID_SLOT, 8, PID_TIMEOUT);
    this.backRight.config_kP(PID_SLOT, 1.5, PID_TIMEOUT);
    this.backRight.config_kI(PID_SLOT, 0, PID_TIMEOUT);
    this.backRight.config_kD(PID_SLOT, 0, PID_TIMEOUT);
    this.backRight.config_kF(PID_SLOT, 8, PID_TIMEOUT);

    this.backLeft.configNominalOutputForward(0, PID_TIMEOUT);
    this.backLeft.configNominalOutputReverse(0, PID_TIMEOUT);
    this.backLeft.configPeakOutputForward(1, PID_TIMEOUT);
    this.backLeft.configPeakOutputReverse(-1, PID_TIMEOUT);
    this.backRight.configNominalOutputForward(0, PID_TIMEOUT);
    this.backRight.configNominalOutputReverse(0, PID_TIMEOUT);
    this.backRight.configPeakOutputForward(1, PID_TIMEOUT);
    this.backRight.configPeakOutputReverse(-1, PID_TIMEOUT);

    // Changed 6000 to 10000
    this.backLeft.configMotionAcceleration(10000, PID_TIMEOUT);
    this.backLeft.configMotionCruiseVelocity(6000, PID_TIMEOUT);
    this.backRight.configMotionAcceleration(10000, PID_TIMEOUT);
    this.backRight.configMotionCruiseVelocity(6000, PID_TIMEOUT);

    this.backLeft.setSensorPhase(true);
    this.backRight.setSensorPhase(true);

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
    SmartDashboard.putBoolean("Drive: Transmission", !this.isTransmissionStatus());

  }

  @Override
  public void teleopInit() {

    this.setDriveControlMode(ControlMode.PercentOutput);
    this.setLeftSetPoint(0);
    this.setRightSetPoint(0);

  }

  @Override
  public void teleopUpdate(LogitechF310 driver, LogitechF310 operator) {

    this.changePeaks(1);
    this.coastMode();

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

  // region Pretty

  public void highTransmission() {
    this.setTransmissionStatus(false);
  }

  public void lowTransmission() {
    this.setTransmissionStatus(true);
  }

  public void drive(double speed) {
    this.drive(speed, speed);
  }

  public void drive(double leftSpeed, double rightSpeed) {
    this.setDriveControlMode(ControlMode.PercentOutput);
    this.setLeftSetPoint(leftSpeed);
    this.setRightSetPoint(rightSpeed);
  }

  public boolean turnAngle(double currentAngle, double desiredAngle, double turnSpeed) {
    return this.turnAngle(currentAngle, desiredAngle, turnSpeed, 1.5);
  }

  public boolean turnAngle(double currentAngle, double desiredAngle, double turnSpeed, double tolerance) {
    double deltaAngle = currentAngle - desiredAngle;
    if (Math.abs(deltaAngle) <= tolerance) {
      this.drive(0);
      return true;
    }
    if (deltaAngle > 0) {
      this.drive(-turnSpeed, turnSpeed);
    } else {
      this.drive(turnSpeed, -turnSpeed);
    }
    return false;
  }

  public boolean driveDistance(double goal, boolean feet) {
    return this.driveDistance(goal, feet, 3);
  }

  public boolean driveDistance(double goal, boolean feet, double tolerance) {
    double multiplier = this.isTransmissionStatus() ? Drive.COUNT_INCH_LOW : Drive.COUNT_INCH_HIGH;
    goal *= feet ? multiplier * 12 : multiplier;
    tolerance *= multiplier;
    double position = (this.getLeftPosition() + this.getRightPosition())/2;
    double deltaGoal = position - goal;
    if (Math.abs(deltaGoal) <= tolerance) {
      this.drive(0);
      return true;
    }
    this.setDriveControlMode(ControlMode.MotionMagic);
    this.setLeftSetPoint(goal);
    this.setRightSetPoint(goal);
    return false;
  }

  public void brakeMode() {
    this.frontLeft.setNeutralMode(NeutralMode.Brake);
    this.midLeft.setNeutralMode(NeutralMode.Brake);
    this.backLeft.setNeutralMode(NeutralMode.Brake);
    this.frontRight.setNeutralMode(NeutralMode.Brake);
    this.midRight.setNeutralMode(NeutralMode.Brake);
    this.backRight.setNeutralMode(NeutralMode.Brake);
  }

  public void coastMode() {
    this.frontLeft.setNeutralMode(NeutralMode.Coast);
    this.midLeft.setNeutralMode(NeutralMode.Coast);
    this.backLeft.setNeutralMode(NeutralMode.Coast);
    this.frontRight.setNeutralMode(NeutralMode.Coast);
    this.midRight.setNeutralMode(NeutralMode.Coast);
    this.backRight.setNeutralMode(NeutralMode.Coast);
  }

  public void changePeaks(double peak) {
    this.backLeft.configPeakOutputReverse(-peak, PID_TIMEOUT);
    this.backLeft.configPeakOutputForward(peak, PID_TIMEOUT);
    this.backRight.configPeakOutputReverse(-peak, PID_TIMEOUT);
    this.backRight.configPeakOutputForward(peak, PID_TIMEOUT);
  }

  // endregion

}
