package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechAxis;
import com.digitalgoats.util.LogitechF310.LogitechButton;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The system for controlling the Drive
 * @author Blake
 */
public class Drive implements IGoatSystem {

  // Constants
  private final double straightModifier = 1.20;
  private final long transmissionDelay = 500;

  // Fields
  private boolean transmissionStatus;
  private double leftSpeed, rightSpeed;
  private double startAngle;
  private long transmissionTime;

  // Objects
  private AHRS navx;
  private DoubleSolenoid transmission;
  private TalonSRX frontLeft, midLeft, backLeft, frontRight, midRight, backRight;

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
    this.transmission = new DoubleSolenoid(
        SystemMap.DRIVE_PCM.getValue(),
        SystemMap.DRIVE_TRANS_FORWARD.getValue(),
        SystemMap.DRIVE_TRANS_BACKWARD.getValue()
    );
    this.frontLeft = new TalonSRX(SystemMap.DRIVE_FRONTLEFT_TALON.getValue());
    this.midLeft = new TalonSRX(SystemMap.DRIVE_MIDLEFT_TALON.getValue());
    this.backLeft = new TalonSRX(SystemMap.DRIVE_BACKLEFT_TALON.getValue());
    this.frontRight = new TalonSRX(SystemMap.DRIVE_FRONTRIGHT_TALON.getValue());
    this.midRight = new TalonSRX(SystemMap.DRIVE_MIDRIGHT_TALON.getValue());
    this.backRight = new TalonSRX(SystemMap.DRIVE_BACKRIGHT_TALON.getValue());

  }

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

  /**
   * Update drive based on internal left and right speed variables
   */
  public void updateDrive() {
    this.frontLeft.set(ControlMode.PercentOutput, this.getLeftSpeed());
    this.backLeft.set(ControlMode.PercentOutput, this.getLeftSpeed());
    this.frontRight.set(ControlMode.PercentOutput, this.getRightSpeed());
    this.backRight.set(ControlMode.PercentOutput, this.getRightSpeed());
  }

  /**
   * Update transmission based on internal transmission status
   */
  public void updateTransmission() {
    this.transmission.set(this.getTransmissionStatus() ? Value.kForward : Value.kReverse);
  }

  /**
   * Drive straight using NavX control
   * @param speed
   *  The desired percent output speed
   */
  public void driveStraightNavX(double speed) {

    double deltaAngle = this.navx.getAngle() - this.getStartAngle();
    double left = (deltaAngle > 0) ? speed : speed * straightModifier;
    double right = (deltaAngle < 0) ? speed : speed * straightModifier;
    this.setDriveSpeed(left, right);

  }

  /** User toggle transmission status */
  public void userToggleTransmissionStatus() {
    if (System.currentTimeMillis() - this.getTransmissionTime() >= transmissionDelay) {
      this.setTransmissionTime(System.currentTimeMillis());
      this.setTransmissionStatus(!this.getTransmissionStatus());
    }
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

  @Override
  public void disabledUpdateSystem() {
    this.setDriveSpeed(0, 0);
    this.updateDrive();
  }

  @Override
  public void autonomousUpdateSystem() {
    this.updateDrive();
    this.updateTransmission();
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {

    this.setDriveSpeed(
        -driver.getAxisValue(LogitechAxis.LEFT_Y),
        -driver.getAxisValue(LogitechAxis.RIGHT_Y)
    );
    if (driver.getButtonValue(LogitechButton.BUT_BACK)) {
      this.userToggleTransmissionStatus();
    }
    this.updateTransmission();
    this.updateDrive();

  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putNumber(this.getSystemName() + " LEFT", this.getLeftSpeed());
    SmartDashboard.putNumber(this.getSystemName() + " RIGHT", this.getRightSpeed());
  }

  @Override
  public String getSystemName() {
    return "Drive";
  }

}
