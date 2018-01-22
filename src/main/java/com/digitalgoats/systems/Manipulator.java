package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechAxis;
import com.digitalgoats.util.LogitechF310.LogitechButton;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * The system for controlling the Manipulator
 * @author Blake
 */
public class Manipulator implements IGoatSystem {

  // region Constants

  private final double stallSpeed = .125;
  private final long gripSolenoidDelay = 250, pivotSolenoidDelay = 250;

  // endregion

  // region Fields

  private boolean gripSolenoidStatus, pivotSolenoidStatus;
  private double leftSpeed, rightSpeed;
  private long gripSolenoidTime, pivotSolenoidTime;

  // endregion

  // region Objects

  private DoubleSolenoid gripSolenoid, pivotSolenoid;
  private TalonSRX leftWheel, rightWheel;

  // endregion

  // region Constructor

  /** Create instance of Manipulator */
  public Manipulator() {

    // Setup Fields
    this.setGripSolenoidStatus(false);
    this.setLeftSpeed(0);
    this.setRightSpeed(0);
    this.setGripSolenoidTime(0);

    // Setup Objects
    gripSolenoid = new DoubleSolenoid(
        SystemMap.MAN_PCM.getValue(),
        SystemMap.MAN_GRIPSOLENOID_FORWARD.getValue(),
        SystemMap.MAN_GRIPSOLENOID_BACKWARD.getValue()
    );
    /*pivotSolenoid = new DoubleSolenoid(
        SystemMap.MAN_PCM.getValue(),
        SystemMap.MAN_PIVOTSOLENOID_FORWARD.getValue(),
        SystemMap.MAN_PIVOTSOLENOID_BACKWARD.getValue()
    );*/
    leftWheel = new TalonSRX(SystemMap.MAN_LEFT_TALON.getValue());
    rightWheel = new TalonSRX(SystemMap.MAN_RIGHT_TALON.getValue());
    rightWheel.setInverted(true);

  }

  // endregion

  // region Autonomous Methods

  // endregion

  // region Update Methods

  /**
   * Update grip solenoid based on status
   */
  public void updateGripSolenoid() {
    this.gripSolenoid.set(this.getGripSolenoidStatus() ? Value.kForward : Value.kReverse);
  }

  /**
   * Update pivot solenoid based on status
   */
  public void updatePivotSolenoid() {
    this.pivotSolenoid.set(this.getPivotSolenoidStatus() ? Value.kForward : Value.kReverse);
  }

  /**
   * Update wheels based on speed
   */
  public void updateWheel() {
    this.leftWheel.set(ControlMode.PercentOutput, this.getLeftSpeed());
    this.rightWheel.set(ControlMode.PercentOutput, this.getRightSpeed());
  }

  // endregion

  // region Getters & Setters

  /**
   * Set wheel speed for left and right side
   * @param left
   *  The percent output for the left wheel
   * @param right
   *  The percent output for the right wheel
   */
  public void setWheelSpeed(double left, double right) {
    this.setLeftSpeed(left);
    this.setRightSpeed(right);
  }

  /** Get grip solenoid status */
  public boolean getGripSolenoidStatus() {
    return this.gripSolenoidStatus;
  }
  /** Set grip solenoid status */
  public void setGripSolenoidStatus(boolean gripSolenoidStatus) {
    this.gripSolenoidStatus = gripSolenoidStatus;
  }

  /** Get pivot solenoid status */
  public boolean getPivotSolenoidStatus() {
    return this.pivotSolenoidStatus;
  }
  /** Set pivot solenoid status */
  public void setPivotSolenoidStatus(boolean pivotSolenoidStatus) {
    this.pivotSolenoidStatus = pivotSolenoidStatus;
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

  /** Get grip solenoid time */
  public long getGripSolenoidTime() {
    return this.gripSolenoidTime;
  }
  /** Set grip solenoid time */
  public void setGripSolenoidTime(long gripSolenoidTime) {
    this.gripSolenoidTime = gripSolenoidTime;
  }

  /** Get pivot solenoid time */
  public long getPivotSolenoidTime() {
    return this.pivotSolenoidTime;
  }
  /** Set pivot solenoid time */
  public void setPivotSolenoidTime(long pivotSolenoidTime) {
    this.pivotSolenoidTime = pivotSolenoidTime;
  }

  // endregion

  // region Overridden

  @Override
  public void disabledUpdateSystem() {
    this.updateWheel();
    this.updateGripSolenoid();
    //this.updatePivotSolenoid();
  }

  @Override
  public void autonomousUpdateSystem() {
    this.updateWheel();
    this.updateGripSolenoid();
    //this.updatePivotSolenoid();
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {

    if (operator.getButtonValue(LogitechButton.BUMPER_LEFT)) {
      if (System.currentTimeMillis() - this.getGripSolenoidTime() >= gripSolenoidDelay) {
        this.setGripSolenoidStatus(!this.getGripSolenoidStatus());
        this.setGripSolenoidTime(System.currentTimeMillis());
      }
    }

    if (operator.getAxisValue(LogitechAxis.LEFT_TRIGGER) >= .5) {
      this.setWheelSpeed(operator.getAxisValue(LogitechAxis.LEFT_TRIGGER), operator.getAxisValue(LogitechAxis.LEFT_TRIGGER));
    } else if (operator.getAxisValue(LogitechAxis.RIGHT_TRIGGER) >= .5) {
      this.setWheelSpeed(-operator.getAxisValue(LogitechAxis.RIGHT_TRIGGER), -operator.getAxisValue(LogitechAxis.RIGHT_TRIGGER));
    } else {
      this.setWheelSpeed(stallSpeed, stallSpeed);
    }

    this.updateWheel();
    this.updateGripSolenoid();
    //this.updatePivotSolenoid();

  }

  @Override
  public void updateSmartDashboard() {
  }

  @Override
  public String getSystemName() {
    return "Manipulator";
  }

  // endregion

}
