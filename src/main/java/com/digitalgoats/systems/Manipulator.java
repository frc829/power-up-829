package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.framework.ISystem;
import com.digitalgoats.robot.SystemMap;
import com.digitalgoats.util.LogitechAxis;
import com.digitalgoats.util.LogitechButton;
import com.digitalgoats.util.LogitechF310;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator implements ISystem {

  // region Constants

  public static final long GRIP_DELAY = 250;
  public static final long PIVOT_DELAY = 500;

  // endregion

  // region Fields

  private boolean gripStatus;
  private double intakeSetPoint;
  private long gripTime, pivotTime;
  private PivotPosition pivotPosition;

  // endregion

  // region Objects

  private Solenoid grip;
  private Solenoid pivotPrimary, pivotSecondary;
  private TalonSRX intakeMaster, intakeSlave;

  // endregion

  public Manipulator() {

    this.setGripStatus(true);
    this.setIntakeSetPoint(0);
    this.setGripTime(0);
    this.setPivotTime(0);
    this.setPivotPosition(PivotPosition.UP);

    this.grip = new Solenoid(SystemMap.Manipulator.PCM, SystemMap.Manipulator.GRIP_CHANNEL);
    this.pivotPrimary = new Solenoid(SystemMap.Manipulator.PCM, SystemMap.Manipulator.PIVOT_P_CHANNEL);
    this.pivotSecondary = new Solenoid(SystemMap.Manipulator.PCM, SystemMap.Manipulator.PIVOT_S_CHANNEL);
    this.intakeMaster = new TalonSRX(SystemMap.Manipulator.INTAKE_MASTER);
    this.intakeSlave = new TalonSRX(SystemMap.Manipulator.INTAKE_SLAVE);
    this.intakeSlave.setInverted(true);

  }

  public enum PivotPosition {
    UP,
    MID,
    DOWN
  }

  // region Miscellaneous Functions

  public boolean canShiftGrip() {
    return System.currentTimeMillis() - this.getGripTime() >= GRIP_DELAY;
  }

  public boolean canShiftPivot() {
    return System.currentTimeMillis() - this.getPivotTime() >= PIVOT_DELAY;
  }

  public double getIntakeCurrent() {
    return (Math.abs(this.intakeMaster.getOutputCurrent()) + Math.abs(this.intakeSlave.getOutputCurrent()))/2;
  }

  // endregion

  // region Overridden

  @Override
  public void shuffleboard() {

    SmartDashboard.putBoolean("Manipulator: Gripping", this.isGripStatus());

  }

  @Override
  public void teleopUpdate(LogitechF310 driver, LogitechF310 operator) {

    if (operator.getButton(LogitechButton.A)) {
      this.setIntakeSetPoint(-1);
    } else if (operator.getButton(LogitechButton.B)) {
      this.setIntakeSetPoint(1);
    } else {
      if (Math.abs(operator.getAxis(LogitechAxis.RY)) >= .15) {
        this.setIntakeSetPoint(-Math.abs(operator.getAxis(LogitechAxis.RY)));
      } else {
        this.setIntakeSetPoint(0);
      }
    }

    if (operator.getButton(LogitechButton.X) && this.canShiftGrip()) {
      this.setGripStatus(!this.isGripStatus());
      this.setGripTime(System.currentTimeMillis());
    }

    if (this.canShiftPivot()) {
      if (operator.getButton(LogitechButton.BACK) && operator.getButton(LogitechButton.START)) {
        this.setPivotPosition(PivotPosition.UP);
        this.setPivotTime(System.currentTimeMillis());
      } else if (operator.getButton(LogitechButton.Y)) {
        this.setPivotPosition(this.getPivotPosition() == PivotPosition.DOWN ? PivotPosition.MID : PivotPosition.DOWN);
        this.setPivotTime(System.currentTimeMillis());
      }
    }

  }

  @Override
  public void update() {

    this.grip.set(!this.isGripStatus());

    if (this.getPivotPosition() == PivotPosition.UP) {
      this.pivotPrimary.set(false);
      this.pivotSecondary.set(false);
    } else if (this.getPivotPosition() == PivotPosition.MID) {
      this.pivotPrimary.set(true);
      this.pivotSecondary.set(false);
    } else {
      this.pivotPrimary.set(true);
      this.pivotSecondary.set(true);
    }
    this.pivotPrimary.set(this.getPivotPosition() != PivotPosition.UP);
    this.pivotSecondary.set(this.getPivotPosition() == PivotPosition.DOWN);

    this.intakeMaster.set(ControlMode.PercentOutput, this.getIntakeSetPoint());
    this.intakeSlave.follow(this.intakeMaster);

  }

  // endregion

  // region Getters & Setters


  public boolean isGripStatus() {
    return this.gripStatus;
  }

  public void setGripStatus(boolean gripStatus) {
    this.gripStatus = gripStatus;
  }

  public double getIntakeSetPoint() {
    return this.intakeSetPoint;
  }

  public void setIntakeSetPoint(double intakeSetPoint) {
    this.intakeSetPoint = intakeSetPoint;
  }

  public long getGripTime() {
    return gripTime;
  }

  public void setGripTime(long gripTime) {
    this.gripTime = gripTime;
  }

  public long getPivotTime() {
    return pivotTime;
  }

  public void setPivotTime(long pivotTime) {
    this.pivotTime = pivotTime;
  }

  public PivotPosition getPivotPosition() {
    return pivotPosition;
  }

  public void setPivotPosition(PivotPosition pivotPosition) {
    this.pivotPosition = pivotPosition;
  }

  // endregion

}
