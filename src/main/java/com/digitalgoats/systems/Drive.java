package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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

  private final double straightModifier = 1.20;
  private final double encoderThreshold = 40;
  private final double turnThreshold = 8.29;
  private final long transmissionDelay = 500;
  private final int slotIdx = 0;
  private final int pidIdx = 0;
  private final int timeoutMs = 10;
  private final double rightKu = 7*2.25;
  private final double rightTu = .25*(2.25*2.25);

  // endregion

  // region Fields

  private boolean transmissionStatus;
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

    this.midLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, slotIdx, timeoutMs);
    this.midLeft.setSensorPhase(true);
    this.midRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, slotIdx, timeoutMs);
    this.midRight.setSensorPhase(true);

    this.midLeft.configNominalOutputForward(0, timeoutMs);
    this.midRight.configNominalOutputForward(0, timeoutMs);
    this.midLeft.configNominalOutputReverse(0, timeoutMs);
    this.midRight.configNominalOutputReverse(0, timeoutMs);
    this.midLeft.configPeakOutputForward(.25, timeoutMs);
    this.midRight.configPeakOutputForward(.25, timeoutMs);
    this.midLeft.configPeakOutputReverse(-.25, timeoutMs);
    this.midRight.configPeakOutputReverse(-.25, timeoutMs);

    this.midLeft.config_kP(slotIdx, 0, timeoutMs);
    this.midLeft.config_kI(slotIdx, 0, timeoutMs);
    this.midLeft.config_kD(slotIdx, 0, timeoutMs);
    this.midRight.config_kP(slotIdx, 0, timeoutMs);
    this.midRight.config_kI(slotIdx, 0, timeoutMs);
    this.midRight.config_kD(slotIdx, 0, timeoutMs);

  }

  // endregion

  // region Autonomous Methods

  /**
   * Drive straight using NavX control
   * @param speed
   *  The desired percent output speed
   */
  public void driveStraightNavX(double speed) {

    double deltaAngle = this.navx.getAngle() - this.getStartAngle();
    double left = (deltaAngle > 0) ? speed : speed * straightModifier;
    double right = (deltaAngle < 0) ? speed * straightModifier : speed;
    this.setDriveSpeed(left, right);

  }

  /**
   * Check that robot is at specified angle
   * @param angle
   *  Desired robot angle
   * @return
   *  true if robot is at desired angle, otherwise false
   */
  public boolean atAngle(double angle) {
    return Math.abs(this.navx.getAngle() - this.getStartAngle()) <= turnThreshold;
  }

  /**
   * Turn toward specified angle
   * @param angle
   *  Angle to turn toward
   * @param speed
   *  The desired percent output speed
   */
  public void turnTowardAngle(double angle, double speed) {

    double deltaAngle = this.navx.getAngle() - this.getStartAngle();
    double left = (deltaAngle > turnThreshold) ? 0 : speed;
    double right = (deltaAngle < -turnThreshold) ? speed : 0;
    this.setDriveSpeed(left, right);

  }

  /**
   * Turn toward specified angle
   * @param angle
   *  Angle to turn toward
   */
  public void turnTowardAngle(double angle) {
    turnTowardAngle(angle, 1);
  }

  // endregion

  // region Update Methods

  /**
   * Update drive based on internal left and right speed variables
   */
  public void updateDrive() {
    this.frontLeft.set(ControlMode.PercentOutput, this.getLeftSpeed());
    this.midLeft.set(ControlMode.PercentOutput, this.getLeftSpeed());
    this.backLeft.set(ControlMode.PercentOutput, this.getLeftSpeed());
    this.frontRight.set(ControlMode.PercentOutput, -this.getRightSpeed());
    this.midRight.set(ControlMode.PercentOutput, -this.getRightSpeed());
    this.backRight.set(ControlMode.PercentOutput, -this.getRightSpeed());
  }

  public void updateDriveVelocity() {
    this.midLeft.set(ControlMode.Velocity, this.getLeftSpeed());
    this.frontLeft.follow(this.midLeft);
    this.backLeft.follow(this.midLeft);
    this.midRight.set(ControlMode.Velocity, this.getRightSpeed());
    this.frontRight.follow(this.midRight);
    this.backRight.follow(this.midRight);
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
    this.updateDriveVelocity();
    this.updateTransmission();
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {

    this.setDriveSpeed(
        -driver.getAxisValue(LogitechAxis.RIGHT_Y),
        -driver.getAxisValue(LogitechAxis.LEFT_Y)
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
    SmartDashboard.putNumber("Drive: Left Velocity", this.midLeft.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("Drive: Right Velocity", this.midRight.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("Drive: Left Position", this.midLeft.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Drive: Right Position", this.midRight.getSelectedSensorPosition(0));
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
