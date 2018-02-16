package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechAxis;
import com.digitalgoats.util.LogitechF310.LogitechButton;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The system for controlling the arm
 * @author Blake
 */
public class Arm implements IGoatSystem {

  // region Constants

  private final int BOTTOM_POSITION = 0, SWITCH_POSITION = 1, SCALE_POSITION = 2, TOP_POSITION = 3;
  private final long transmissionDelay = 500;

  // endregion

  // region Fields

  private boolean transmissionStatus;
  private double stageSpeed;
  private int lastSwitch, targetSwitch;
  private long transmissionTime;

  // endregion

  // region Objects

  private DigitalInput scalePosition, switchPosition;
  private Solenoid transmission;
  private TalonSRX stageOne, stageTwo;

  // endregion

  // region Constructor

  public Arm() {

    // Setup Fields
    this.setStageSpeed(0);
    this.setLastSwitch(BOTTOM_POSITION);
    this.setTargetSwitch(99);

    // Setup Objects
    scalePosition = new DigitalInput(SystemMap.MAN_SCALE_POSITION.getValue());
    switchPosition = new DigitalInput(SystemMap.MAN_SWITCH_POSITION.getValue());
    this.transmission = new Solenoid(
        SystemMap.ARM_PCM.getValue(),
        SystemMap.ARM_TRANS_FORWARD.getValue()
    );
    this.stageOne = new TalonSRX(SystemMap.ARM_STAGEONE_TALON.getValue());
    this.stageTwo = new TalonSRX(SystemMap.ARM_STAGETWO_TALON.getValue());

  }

  // endregion

  // region Autonomous Methods

  // endregion

  // region Update Methods

  public void updateStages() {

    this.stageOne.set(ControlMode.PercentOutput, this.getStageSpeed());
    this.stageTwo.follow(this.stageOne);

  }

  public void updateTransmission() {
    this.transmission.set(this.getTransmissionStatus());
  }

  // endregion

  // region Getters & Setters

  /** Get transmission status */
  public boolean getTransmissionStatus() {
    return this.transmissionStatus;
  }
  /** Set transmission status */
  public void setTransmissionStatus(boolean transmissionStatus) {
    this.transmissionStatus = transmissionStatus;
  }

  public int getLastSwitch() {
    return this.lastSwitch;
  }
  public void setLastSwitch(int lastSwitch) {
    this.lastSwitch = lastSwitch;
  }

  public int getTargetSwitch() {
    return this.targetSwitch;
  }
  public void setTargetSwitch(int targetSwitch) {
    this.targetSwitch = targetSwitch;
  }

  /** Get stage speed */
  public double getStageSpeed() {
    return this.stageSpeed;
  }
  /** Set stage speed */
  public void setStageSpeed(double stageSpeed) {
    this.stageSpeed = stageSpeed;
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
    this.updateStages();
    this.updateTransmission();
  }

  @Override
  public void autonomousUpdateSystem() {
    this.updateStages();
    this.updateTransmission();
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {

    this.setLastSwitch(
        this.stageOne.getSensorCollection().isFwdLimitSwitchClosed() ? TOP_POSITION :
            this.stageOne.getSensorCollection().isRevLimitSwitchClosed() ? BOTTOM_POSITION :
                this.switchPosition.get() ? SWITCH_POSITION :
                    this.scalePosition.get() ? SCALE_POSITION : this.getLastSwitch()
    );

    if (operator.getButtonValue(LogitechButton.JOY_LEFT_BUT)) {
      if (System.currentTimeMillis() - this.getTransmissionTime() >= transmissionDelay) {
        this.setTransmissionTime(System.currentTimeMillis());
        this.setTransmissionStatus(!this.getTransmissionStatus());
      }
    }

    if (this.getTargetSwitch() == this.getLastSwitch() || this.getTargetSwitch() == 99) {
      this.setTargetSwitch(99);
    } else {
      this.setStageSpeed(this.getTargetSwitch() > this.getLastSwitch() ? .5 : -.5);
    }

    if (this.getTargetSwitch() == 99) {
      if (Math.abs(operator.getAxisValue(LogitechAxis.LEFT_Y)) > .1) {
        this.setStageSpeed(-operator.getAxisValue(LogitechAxis.LEFT_Y));
      } else {
        this.setStageSpeed(0.0625);
      }
    }

    this.updateStages();
    this.updateTransmission();

  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putString("Arm: Transmission Status", this.getTransmissionStatus() ? "High" : "Low");
    SmartDashboard.putNumber("Arm: Stage Speed", this.getStageSpeed());
  }

  @Override
  public String getSystemName() {
    return "Arm";
  }

  // endregion

}
