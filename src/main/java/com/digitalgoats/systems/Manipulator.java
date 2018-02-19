package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.framework.ISystem;
import com.digitalgoats.objects.QuadSolenoid;
import com.digitalgoats.objects.QuadSolenoid.Value;
import com.digitalgoats.robot.SystemMap;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechButton;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Manipulator implements ISystem {

  public static final long GRIP_DELAY = 500;
  public static final long PIVOT_DELAY = 500;

  private double setpoint;
  private DoubleSolenoid.Value gripPositon;
  private long gripTime, pivotTime;
  private QuadSolenoid.Value pivotPosition;

  private DoubleSolenoid grip;
  private TalonSRX wheelMaster, wheelSlave;
  private QuadSolenoid pivot;

  public Manipulator() {

    this.setSetpoint(0);
    this.setGripPositon(DoubleSolenoid.Value.kReverse);
    this.setGripTime(System.currentTimeMillis());
    this.setPivotTime(System.currentTimeMillis());
    this.setPivotPosition(Value.ForwardForward);

    this.grip = new DoubleSolenoid(
        SystemMap.Manipulator.PIVOT_PCM,
        SystemMap.Manipulator.GRIP_FORWARD, SystemMap.Manipulator.GRIP_REVERSE
    );
    this.wheelMaster = new TalonSRX(SystemMap.Manipulator.WHEEL_MASTER);
    this.wheelSlave = new TalonSRX(SystemMap.Manipulator.WHEEL_SLAVE);
    this.pivot = new QuadSolenoid(
        SystemMap.Manipulator.PIVOT_PCM,
        SystemMap.Manipulator.PIVOT_A_F, SystemMap.Manipulator.PIVOT_A_R,
        SystemMap.Manipulator.PIVOT_B_F, SystemMap.Manipulator.PIVOT_B_R
    );

  }

  // region Autonomous Functions

  // endregion

  // region System Functions

  @Override
  public void systemUpdate() {

    this.grip.set(this.getGripPositon());

    this.wheelMaster.set(ControlMode.PercentOutput, this.getSetpoint());
    this.wheelSlave.follow(this.wheelMaster);

    this.pivot.set(this.getPivotPosition());

  }

  @Override
  public void execTeleop(LogitechF310 driver, LogitechF310 operator) {

    if (operator.getButtonValue(LogitechButton.BUT_A)) {
      this.setSetpoint(1);
    } else if (operator.getButtonValue(LogitechButton.BUT_B)) {
      this.setSetpoint(-1);
    } else {
      this.setSetpoint(0);
    }

    if (operator.getButtonValue(LogitechButton.BUT_X)) {
      if (System.currentTimeMillis() - this.getGripTime() >= GRIP_DELAY) {
        this.setGripTime(System.currentTimeMillis());
        this.setGripPositon(
            this.getGripPositon() == DoubleSolenoid.Value.kForward ?
                DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward
        );
      }
    }

    if (operator.getButtonValue(LogitechButton.BUT_BACK) && operator.getButtonValue(LogitechButton.BUT_START)) {
      this.setPivotPosition(Value.ForwardForward);
    }

    if (operator.getButtonValue(LogitechButton.BUT_Y)) {
      if (System.currentTimeMillis() - this.getPivotTime() >= PIVOT_DELAY) {
        this.setPivotTime(System.currentTimeMillis());
        this.setPivotPosition(
            this.getPivotPosition() == Value.ForwardReverse ?
                Value.ReverseReverse : Value.ForwardReverse
        );
      }
    }

  }

  @Override
  public void execDisabled() {
    this.setSetpoint(0);
  }

  @Override
  public String getSystemName() {
    return "Manipulator";
  }

  // endregion

  // region Getters & Setters

  public double getSetpoint() {
    return this.setpoint;
  }

  public void setSetpoint(double setpoint) {
    this.setpoint = setpoint;
  }

  public DoubleSolenoid.Value getGripPositon() {
    return gripPositon;
  }

  public void setGripPositon(DoubleSolenoid.Value gripPositon) {
    this.gripPositon = gripPositon;
  }

  public long getGripTime() {
    return gripTime;
  }

  public void setGripTime(long gripTime) {
    this.gripTime = gripTime;
  }

  public long getPivotTime() {
    return this.pivotTime;
  }

  public void setPivotTime(long pivotTime) {
    this.pivotTime = pivotTime;
  }

  public QuadSolenoid.Value getPivotPosition() {
    return this.pivotPosition;
  }

  public void setPivotPosition(QuadSolenoid.Value pivotPosition) {
    this.pivotPosition = pivotPosition;
  }

  // endregion

}
