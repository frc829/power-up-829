package com.digitalgoats.systems;

import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechAxis;
import com.digitalgoats.util.LogitechF310.LogitechButton;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The system for controlling the Drive
 * @author Blake
 */
public class Drive implements IGoatSystem {

  // region Constants

  // PRACTICE BOT
  public static final double INCH_COUNT_L = 1934/43.5;
  public static final double INCH_COUNT_R = 1919/43.5;

  // COMPETITION BOT
  //public static final double INCH_COUNT = 32.35955056179775;

  public static final double FOOT_COUNT_L = INCH_COUNT_L * 12;
  public static final double FOOT_COUNT_R = INCH_COUNT_R * 12;
  public static final double FOOT_COUNT = (FOOT_COUNT_L + FOOT_COUNT_R)/2;
  private final long transmissionDelay = 500;
  private final int slotIdx = 0;
  private final int timeoutMs = 10;

  // endregion

  // region Fields

  private boolean transmissionStatus;
  private ControlMode controlMode;
  private double leftSpeed, rightSpeed;
  private double startAngle;
  private long transmissionTime;

  // endregion

  // region Objects

  private AHRS navx;
  private Solenoid transmission;
  private TalonSRX frontLeft, midLeft, backLeft, frontRight, midRight, backRight;

  // endregion

  // region Constructor

  /** Create instance of Drive System */
  public Drive(AHRS navx) {

    // Setup fields
    this.setTransmissionStatus(false);
    this.setControlMode(ControlMode.PercentOutput);
    this.setLeftSpeed(0);
    this.setRightSpeed(0);
    this.setStartAngle(navx.getAngle());
    this.setTransmissionTime(0);

    // Setup Objects
    this.navx = navx;
    this.transmission = new Solenoid(
        SystemMap.DRIVE_PCM.getValue(),
        SystemMap.DRIVE_TRANS_FORWARD.getValue()
    );
    this.frontLeft = new TalonSRX(SystemMap.DRIVE_FRONTLEFT_TALON.getValue());
    this.midLeft = new TalonSRX(SystemMap.DRIVE_MIDLEFT_TALON.getValue());
    this.backLeft = new TalonSRX(SystemMap.DRIVE_BACKLEFT_TALON.getValue());
    this.frontRight = new TalonSRX(SystemMap.DRIVE_FRONTRIGHT_TALON.getValue());
    this.midRight = new TalonSRX(SystemMap.DRIVE_MIDRIGHT_TALON.getValue());
    this.backRight = new TalonSRX(SystemMap.DRIVE_BACKRIGHT_TALON.getValue());

    // PRACTICE BOT CONFIG
    this.frontRight.setInverted(true);
    this.midRight.setInverted(false);
    this.backRight.setInverted(true);
    this.frontLeft.setInverted(true);
    this.backLeft.setInverted(true);
    this.midLeft.setInverted(true);
    this.backRight.setSensorPhase(true);

    // COMPETITION BOT CONFIG
    /*this.midLeft.setInverted(true);
    this.frontLeft.setInverted(true);
    this.backLeft.setInverted(true);
    this.backLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, slotIdx, timeoutMs);
    this.backLeft.setSelectedSensorPosition(0, slotIdx, timeoutMs);
    this.backLeft.setSensorPhase(true);
    this.backRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, slotIdx, timeoutMs);
    this.backRight.setSelectedSensorPosition(0, slotIdx, timeoutMs);*/

    this.backLeft.selectProfileSlot(slotIdx, timeoutMs);
    this.backRight.selectProfileSlot(slotIdx, timeoutMs);
    this.backLeft.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, timeoutMs);
    this.backRight.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, timeoutMs);

    this.backLeft.configNominalOutputForward(0, timeoutMs);
    this.backRight.configNominalOutputForward(0, timeoutMs);
    this.backLeft.configNominalOutputReverse(0, timeoutMs);
    this.backRight.configNominalOutputReverse(0, timeoutMs);
    this.backLeft.configPeakOutputForward(1, timeoutMs);
    this.backRight.configPeakOutputForward(1, timeoutMs);
    this.backLeft.configPeakOutputReverse(-1, timeoutMs);
    this.backRight.configPeakOutputReverse(-1, timeoutMs);

    this.backLeft.config_kP(slotIdx, 1.5, timeoutMs);
    this.backLeft.config_kI(slotIdx, 0, timeoutMs);
    this.backLeft.config_kD(slotIdx, 0, timeoutMs);
    this.backLeft.config_kF(slotIdx, 8, timeoutMs);
    this.backRight.config_kP(slotIdx, 1.5, timeoutMs);
    this.backRight.config_kI(slotIdx, 0, timeoutMs);
    this.backRight.config_kD(slotIdx, 0, timeoutMs);
    this.backRight.config_kF(slotIdx, 8, timeoutMs);

    this.backLeft.configMotionCruiseVelocity(15000/*(int)(.8 * FOOT_COUNT)*/, timeoutMs);
    this.backRight.configMotionCruiseVelocity(15000/*(int)(.8 * FOOT_COUNT)*/, timeoutMs);
    this.backLeft.configMotionAcceleration(6000/*(int)(.3 * FOOT_COUNT)*/, timeoutMs);
    this.backRight.configMotionAcceleration(6000/*(int)(.3 * FOOT_COUNT)*/, timeoutMs);

  }

  // endregion

  // region Autonomous Methods

  public void resetSensors() {
    this.backLeft.setSelectedSensorPosition(0, slotIdx, timeoutMs);
    this.backRight.setSelectedSensorPosition(0, slotIdx, timeoutMs);
  }

  public boolean atTarget() {
    if (Math.abs(this.getLeftSpeed() - -this.getLeftPosition()) <= 50) {
      if (Math.abs(this.getRightSpeed() - -this.getRightPosition()) <= 50) {
        return true;
      }
    }
    return false;
  }

  public double getLeftPosition() {
    return this.backLeft.getSelectedSensorPosition(slotIdx);
  }

  public double getRightPosition() {
    return this.backRight.getSelectedSensorPosition(slotIdx);
  }

  public double getLeftVelocity() {
    return this.backLeft.getSelectedSensorVelocity(slotIdx);
  }

  public double getRightVelocity() {
    return this.backRight.getSelectedSensorVelocity(slotIdx);
  }

  // endregion

  // region Update Methods

  /**
   * Update drive based on internal left and right speed variables
   */
  public void updateDrive() {
    this.backLeft.set(this.getControlMode(), this.getLeftSpeed());
    this.frontLeft.follow(this.backLeft);
    this.midLeft.follow(this.backLeft);
    this.backRight.set(this.getControlMode(), this.getRightSpeed());
    this.frontRight.follow(this.backRight);
    this.midRight.follow(this.backRight);
  }

  /**
   * Update transmission based on internal transmission status
   */
  public void updateTransmission() {
    this.transmission.set(this.getTransmissionStatus());
  }

  // endregion

  // region Getters & Setters

  /**
   * Set speed for drive sides
   * @param left
   *  The percent output for left side of drive
   * @param right
   *  The percent output for right side of drive
   */
  public void setDriveSpeed(double left, double right) {
    this.setLeftSpeed(left);
    this.setRightSpeed(right);
  }

  /** Get transmission status */
  public boolean getTransmissionStatus() {
    return this.transmissionStatus;
  }
  /** Set transmission status */
  public void setTransmissionStatus(boolean transmissionStatus) {
    this.transmissionStatus = transmissionStatus;
  }

  public ControlMode getControlMode() {
    return this.controlMode;
  }
  public void setControlMode(ControlMode controlMode) {
    this.controlMode = controlMode;
  }

  /** Get left speed */
  public double getLeftSpeed() {
    return this.leftSpeed;
  }
  /** Set left speed */
  public void setLeftSpeed(double leftSpeed) {
    this.leftSpeed = leftSpeed;
  }

  /** Get right speed */
  public double getRightSpeed() {
    return this.rightSpeed;
  }
  /** Set right speed */
  public void setRightSpeed(double rightSpeed) {
    this.rightSpeed = rightSpeed;
  }

  /** Get start angle */
  public double getStartAngle() {
    return this.startAngle;
  }
  /** Set start angle */
  public void setStartAngle(double startAngle) {
    this.startAngle = startAngle;
  }

  /** Get transmission time */
  public long getTransmissionTime() {
    return this.transmissionTime;
  }
  /** Set transmission time */
  public void setTransmissionTime(long transmissionTime) {
    this.transmissionTime = transmissionTime;
  }

  // endregion

  // region Overridden Methods

  @Override
  public void disabledUpdateSystem() {
    this.setDriveSpeed(0, 0);
    this.updateDrive();
    this.updateTransmission();
  }

  @Override
  public void autonomousUpdateSystem() {
    this.updateDrive();
    this.updateTransmission();
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {

    double A = 0.65;
    double leftY = -driver.getAxisValue(LogitechAxis.LEFT_Y);
    double rightY = -driver.getAxisValue(LogitechAxis.RIGHT_Y);
    leftY = (A*Math.pow(leftY, 3)) + ((1-A)*leftY);
    rightY = (A*Math.pow(rightY, 3)) + ((1-A)*rightY);
    this.setControlMode(ControlMode.PercentOutput);
    this.setDriveSpeed(
        leftY,
        rightY
    );
    if (driver.getButtonValue(LogitechButton.BUT_BACK)) {
      if (System.currentTimeMillis() - this.getTransmissionTime() >= transmissionDelay) {
        this.setTransmissionTime(System.currentTimeMillis());
        this.setTransmissionStatus(!this.getTransmissionStatus());
      }
    }
    this.updateTransmission();
    this.updateDrive();

  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putString("Drive: Transmission Status", this.getTransmissionStatus() ? "High" : "Low");
    SmartDashboard.putNumber("Left Pos", this.backLeft.getSelectedSensorPosition(slotIdx));
    SmartDashboard.putNumber("Right Pos", this.backRight.getSelectedSensorPosition(slotIdx));
    SmartDashboard.putNumber("Drive: Left Velocity", this.getLeftVelocity());
    SmartDashboard.putNumber("Drive: Right Velocity", this.getRightVelocity());
    SmartDashboard.putNumber("NavX: Current Angle", this.navx.getAngle());
    SmartDashboard.putNumber("NavX: X Displacement", this.navx.getDisplacementX());
    SmartDashboard.putNumber("NavX: Y Displacement", this.navx.getDisplacementY());
    SmartDashboard.putNumber("NavX: Z Displacement", this.navx.getDisplacementZ());
  }

  @Override
  public String getSystemName() {
    return "Drive";
  }

  // endregion

}
