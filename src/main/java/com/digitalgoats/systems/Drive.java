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

  private final long transmissionDelay = 500;
  private final int slotIdx = 0;
  private final int pidIdx = 0;
  private final int timeoutMs = 10;
  private final double rightKu = 500;
  private final double rightTu = 2;

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
    this.midRight.setInverted(true);
    this.frontRight.setInverted(true);
    this.backRight.setInverted(true);

    /*this.midLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, slotIdx, timeoutMs);
    this.midLeft.setSensorPhase(true);
    this.midRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, slotIdx, timeoutMs);
    this.midRight.setSensorPhase(true);

    this.midLeft.configNominalOutputForward(0, timeoutMs);
    this.midRight.configNominalOutputForward(0, timeoutMs);
    this.midLeft.configNominalOutputReverse(0, timeoutMs);
    this.midRight.configNominalOutputReverse(0, timeoutMs);
    this.midLeft.configPeakOutputForward(.15, timeoutMs);
    this.midRight.configPeakOutputForward(.15, timeoutMs);
    this.midLeft.configPeakOutputReverse(-.15, timeoutMs);
    this.midRight.configPeakOutputReverse(-.15, timeoutMs);

    this.midLeft.config_kP(slotIdx, 0, timeoutMs);
    this.midLeft.config_kI(slotIdx, 0, timeoutMs);
    this.midLeft.config_kD(slotIdx, 0, timeoutMs);
    this.midLeft.config_kF(slotIdx, 0, timeoutMs);
    this.midRight.config_kP(slotIdx, 0, timeoutMs);
    this.midRight.config_kI(slotIdx, 0, timeoutMs);
    this.midRight.config_kD(slotIdx, 0, timeoutMs);
    this.midRight.config_kF(slotIdx, 0, timeoutMs);*/

  }

  // endregion

  // region Autonomous Methods

  // endregion

  // region Update Methods

  /**
   * Update drive based on internal left and right speed variables
   */
  public void updateDrive() {
    this.midLeft.set(this.getControlMode(), this.getLeftSpeed());
    this.frontLeft.follow(this.midLeft);
    this.backLeft.follow(this.midLeft);
    this.midRight.set(this.getControlMode(), this.getRightSpeed());
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

  public ControlMode getControlMode() {
    return this.controlMode;
  }
  public void setControlMode(ControlMode controlMode) {
    this.controlMode = controlMode;
  }

  public double getLeftVelocity() {
    return this.midLeft.getSelectedSensorVelocity(slotIdx);
  }
  public double getRightVelocity() {
    return this.midRight.getSelectedSensorVelocity(slotIdx);
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

    this.setControlMode(ControlMode.PercentOutput);
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
    SmartDashboard.putNumber("Drive: Left Velocity", this.midLeft.getSelectedSensorVelocity(slotIdx));
    SmartDashboard.putNumber("Drive: Right Velocity", this.midRight.getSelectedSensorVelocity(slotIdx));
    SmartDashboard.putNumber("Drive: Left Position", this.midLeft.getSelectedSensorPosition(slotIdx));
    SmartDashboard.putNumber("Drive: Right Position", this.midRight.getSelectedSensorPosition(slotIdx));
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
