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

  // Constants
  private final double stallSpeed = .125;
  private final long gripSolenoidDelay = 250;

  // Fields
  private boolean gripSolenoidStatus;
  private double leftSpeed, rightSpeed;
  private long gripSolenoidTime;

  // Objects
  private DoubleSolenoid gripSolenoid;
  private TalonSRX leftWheel, rightWheel;

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
    //leftWheel = new TalonSRX(SystemMap.MAN_LEFT_TALON.getValue());
    rightWheel = new TalonSRX(SystemMap.MAN_RIGHT_TALON.getValue());
    rightWheel.setInverted(true);

  }

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

  public void updateSolenoid() {
    this.gripSolenoid.set(this.getGripSolenoidStatus() ? Value.kForward : Value.kReverse);
  }

  public void updateWheel() {
    //this.leftWheel.set(ControlMode.PercentOutput, this.getLeftSpeed());
    this.rightWheel.set(ControlMode.PercentOutput, this.getRightSpeed());
  }

  /** Get solenoid status */
  public boolean getGripSolenoidStatus() {
    return this.gripSolenoidStatus;
  }
  /** Set solenoid status */
  public void setGripSolenoidStatus(boolean gripSolenoidStatus) {
    this.gripSolenoidStatus = gripSolenoidStatus;
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

  /** Get solenoid time */
  public long getGripSolenoidTime() {
    return this.gripSolenoidTime;
  }
  /** Set solenoid time */
  public void setGripSolenoidTime(long gripSolenoidTime) {
    this.gripSolenoidTime = gripSolenoidTime;
  }

  public void toggleGripSolenoidStatus() {
    if (System.currentTimeMillis() - this.getGripSolenoidTime() >= gripSolenoidDelay) {
      this.setGripSolenoidStatus(!this.getGripSolenoidStatus());
      this.setGripSolenoidTime(System.currentTimeMillis());
    }
  }

  @Override
  public void disabledUpdateSystem() {
    this.updateWheel();
    this.updateSolenoid();
  }

  @Override
  public void autonomousUpdateSystem() {
    this.updateWheel();
    this.updateSolenoid();
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {

    if (operator.getButtonValue(LogitechButton.BUMPER_LEFT)) {
      this.toggleGripSolenoidStatus();
    }

    if (operator.getAxisValue(LogitechAxis.LEFT_TRIGGER) >= .5) {
      this.setWheelSpeed(operator.getAxisValue(LogitechAxis.LEFT_TRIGGER), operator.getAxisValue(LogitechAxis.LEFT_TRIGGER));
    } else if (operator.getAxisValue(LogitechAxis.RIGHT_TRIGGER) >= .5) {
      this.setWheelSpeed(-operator.getAxisValue(LogitechAxis.RIGHT_TRIGGER), -operator.getAxisValue(LogitechAxis.RIGHT_TRIGGER));
    } else {
      this.setWheelSpeed(stallSpeed, stallSpeed);
    }

    this.updateWheel();
    this.updateSolenoid();

  }

  @Override
  public void updateSmartDashboard() {
  }

  @Override
  public String getSystemName() {
    return "Manipulator";
  }

}
