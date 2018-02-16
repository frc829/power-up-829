package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechAxis;
import com.digitalgoats.util.LogitechF310.LogitechButton;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The system for controlling the Manipulator
 * @author Blake
 */
public class Manipulator implements IGoatSystem {

  // region Constants

  private final double stallSpeed = 0;
  private final long gripSolenoidDelay = 250, pivotSolenoidDelay = 250;
  private final int PIVOT_MID = 0, PIVOT_UP = 1, PIVOT_LOW = 2;

  // endregion

  // region Fields

  private boolean gripSolenoidStatus;
  private int pivotSolenoidStatus;
  private double wheelSpeed;
  private long gripSolenoidTime, pivotSolenoidTime;

  // endregion

  // region Objects

  private DoubleSolenoid gripSolenoid, pivotSolenoid, pivotSolenoid2;
  private TalonSRX leftWheel, rightWheel;

  // endregion

  // region Constructor

  /** Create instance of Manipulator */
  public Manipulator() {

    // Setup Fields
    this.setGripSolenoidStatus(false);
    this.setPivotSolenoidStatus(PIVOT_UP);
    this.setWheelSpeed(stallSpeed);
    this.setGripSolenoidTime(0);

    // Setup Objects
    gripSolenoid = new DoubleSolenoid(
        SystemMap.MAN_PCM.getValue(),
        SystemMap.MAN_GRIPSOLENOID_FORWARD.getValue(),
        SystemMap.MAN_GRIPSOLENOID_BACKWARD.getValue()
    );
    pivotSolenoid = new DoubleSolenoid(
        SystemMap.MAN_PCM.getValue(),
        SystemMap.MAN_PIVOTSOLENOID_FORWARD.getValue(),
        SystemMap.MAN_PIVOTSOLENOID_BACKWARD.getValue()
    );
    pivotSolenoid2 = new DoubleSolenoid(
        SystemMap.MAN_PCM.getValue(),
        SystemMap.MAN_PIVOTSOLENOID_2_FORWARD.getValue(),
        SystemMap.MAN_PIVOTSOLENOID_2_BACKWARD.getValue()
    );
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
    switch (this.getPivotSolenoidStatus()) {
      case PIVOT_MID: {
        this.pivotSolenoid.set(Value.kForward);
        this.pivotSolenoid2.set(Value.kReverse);
        break;
      }
      case PIVOT_LOW: {
        this.pivotSolenoid.set(Value.kForward);
        this.pivotSolenoid2.set(Value.kForward);
        break;
      }
      case PIVOT_UP: {
        this.pivotSolenoid.set(Value.kReverse);
        this.pivotSolenoid2.set(Value.kReverse);
        break;
      }
    }
  }

  /**
   * Update wheels based on speed
   */
  public void updateWheel() {
    this.leftWheel.set(ControlMode.PercentOutput, this.getWheelSpeed());
    this.rightWheel.follow(this.leftWheel);
  }

  // endregion

  // region Getters & Setters

  /** Get grip solenoid status */
  public boolean getGripSolenoidStatus() {
    return this.gripSolenoidStatus;
  }
  /** Set grip solenoid status */
  public void setGripSolenoidStatus(boolean gripSolenoidStatus) {
    this.gripSolenoidStatus = gripSolenoidStatus;
  }

  /** Get pivot solenoid status */
  public int getPivotSolenoidStatus() {
    return this.pivotSolenoidStatus;
  }
  /** Set pivot solenoid status */
  public void setPivotSolenoidStatus(int pivotSolenoidStatus) {
    this.pivotSolenoidStatus = pivotSolenoidStatus;
  }

  /** Get wheel speed */
  public double getWheelSpeed() {
    return this.wheelSpeed;
  }
  /** Set wheel speed */
  public void setWheelSpeed(double wheelSpeed) {
    this.wheelSpeed = wheelSpeed;
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
    this.updatePivotSolenoid();
  }

  @Override
  public void autonomousUpdateSystem() {
    this.updateWheel();
    this.updateGripSolenoid();
    this.updatePivotSolenoid();
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {

    if (operator.getButtonValue(LogitechButton.BUT_A)) {
      this.setWheelSpeed(1);
    } else if (operator.getButtonValue(LogitechButton.BUT_B)) {
      this.setWheelSpeed(-1);
    } else {
      this.setWheelSpeed(stallSpeed);
    }

    if (operator.getButtonValue(LogitechButton.BUT_X)) {
      if (System.currentTimeMillis() - this.gripSolenoidTime >= gripSolenoidDelay) {
        this.setGripSolenoidTime(System.currentTimeMillis());
        this.setGripSolenoidStatus(!this.getGripSolenoidStatus());
      }
    }

    if (operator.getButtonValue(LogitechButton.BUT_Y)) {
      if (System.currentTimeMillis() - this.pivotSolenoidTime >= pivotSolenoidDelay) {
        this.setPivotSolenoidTime(System.currentTimeMillis());
        if (this.getPivotSolenoidStatus() == PIVOT_MID) {
          this.setPivotSolenoidStatus(PIVOT_LOW);
        } else {
          this.setPivotSolenoidStatus(PIVOT_MID);
        }
      }
    }

    if (operator.getButtonValue(LogitechButton.BUT_START) && operator.getButtonValue(LogitechButton.BUT_BACK)) {
      this.setPivotSolenoidStatus(PIVOT_UP);
    }

    this.updateWheel();
    this.updateGripSolenoid();
    this.updatePivotSolenoid();

  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putString("Manipulator: Grip Status", this.getGripSolenoidStatus() ?
        "Gripping" : "Not Gripping");
    SmartDashboard.putNumber("Manipulator: Pivot Status", this.getPivotSolenoidStatus());
    SmartDashboard.putNumber("Manipulator: Wheel Speed", this.getWheelSpeed());
  }

  @Override
  public String getSystemName() {
    return "Manipulator";
  }

  // endregion

}
