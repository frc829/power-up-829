package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechAxis;
import com.digitalgoats.util.LogitechF310.LogitechButton;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The system for controlling the Manipulator
 * @author Blake
 */
public class Manipulator implements IGoatSystem {

  // Constants
  private final double stallSpeed = .125;
  private final long solenoidDelay = 250;

  // Fields
  private boolean solenoidStatus;
  private double leftSpeed, rightSpeed;
  private long solenoidTime;

  // Objects
  private DoubleSolenoid solenoid;
  private TalonSRX leftWheel, rightWheel;

  /** Create instance of Manipulator */
  public Manipulator() {

    // Setup Fields
    this.setSolenoidStatus(false);
    this.setLeftSpeed(0);
    this.setRightSpeed(0);
    this.setSolenoidTime(0);

    // Setup Objects
    solenoid = new DoubleSolenoid(
        SystemMap.MAN_PCM.getValue(),
        SystemMap.MAN_SOLENOID_FORWARD.getValue(),
        SystemMap.MAN_SOLENOID_BACKWARD.getValue()
    );
    leftWheel = new TalonSRX(SystemMap.MAN_LEFT_TALON.getValue());
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
    this.solenoid.set(this.getSolenoidStatus() ? Value.kForward : Value.kReverse);
  }

  public void updateWheel() {
    this.leftWheel.set(ControlMode.PercentOutput, this.getLeftSpeed());
    this.rightWheel.set(ControlMode.PercentOutput, this.getRightSpeed());
  }

  /** Get solenoid status */
  public boolean getSolenoidStatus() {
    return this.solenoidStatus;
  }
  /** Set solenoid status */
  public void setSolenoidStatus(boolean solenoidStatus) {
    this.solenoidStatus = solenoidStatus;
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
  public long getSolenoidTime() {
    return this.solenoidTime;
  }
  /** Set solenoid time */
  public void setSolenoidTime(long solenoidTime) {
    this.solenoidTime = solenoidTime;
  }

  public void toggleSolenoidStatus() {
    if (System.currentTimeMillis() - this.getSolenoidTime() >= solenoidDelay) {
      this.setSolenoidStatus(!this.getSolenoidStatus());
      this.setSolenoidTime(System.currentTimeMillis());
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
      this.toggleSolenoidStatus();
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
    SmartDashboard.putBoolean(this.getSystemName() + " SOLENOID", this.getSolenoidStatus());
    SmartDashboard.putNumber(this.getSystemName() + " LEFT", this.getLeftSpeed());
    SmartDashboard.putNumber(this.getSystemName() + " RIGHT", this.getRightSpeed());
  }

  @Override
  public String getSystemName() {
    return "Manipulator";
  }

}
