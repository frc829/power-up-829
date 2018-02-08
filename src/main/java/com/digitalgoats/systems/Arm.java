package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechButton;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The system for controlling the arm
 * @author Blake
 */
public class Arm implements IGoatSystem {

  // region Constants

  private final long transmissionDelay = 500;

  // endregion

  // region Fields

  private boolean transmissionStatus;
  private double stageSpeed;
  private long transmissionTime;

  // endregion

  // region Objects

  private DoubleSolenoid transmission;
  private TalonSRX stageOne, stageTwo;

  // endregion

  // region Constructor

  public Arm() {

    // Setup Fields
    this.setStageSpeed(0);

    // Setup Objects
    this.transmission = new DoubleSolenoid(
        SystemMap.ARM_PCM.getValue(),
        SystemMap.ARM_TRANS_FORWARD.getValue(),
        SystemMap.ARM_TRANS_BACKWARD.getValue()
    );
    this.stageOne = new TalonSRX(SystemMap.ARM_STAGEONE_TALON.getValue());
    this.stageTwo = new TalonSRX(SystemMap.ARM_STAGETWO_TALON.getValue());

  }

  // endregion

  // region Autonomous Methods

  // endregion

  // region Update Methods

  public void updateStages() {

    boolean goingUp = this.getStageSpeed() > 0;
    boolean goingDown = this.getStageSpeed() < 0;
    boolean topLimit = !this.stageOne.getSensorCollection().isFwdLimitSwitchClosed();
    boolean botLimit = !this.stageOne.getSensorCollection().isRevLimitSwitchClosed();

    if ((goingUp && topLimit) || (goingDown && botLimit)) {
      this.stageOne.set(ControlMode.PercentOutput, 0.0625);
      this.stageTwo.follow(this.stageOne);
    } else {
      this.stageOne.set(ControlMode.PercentOutput, this.getStageSpeed());
      this.stageTwo.follow(this.stageOne);
    }

  }

  public void updateTransmission() {
    this.transmission.set(this.getTransmissionStatus() ? Value.kForward : Value.kReverse);
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

    if (operator.getButtonValue(LogitechButton.BUMPER_LEFT) && operator.getButtonValue(LogitechButton.BUMPER_RIGHT)) {
      if (System.currentTimeMillis() - this.getTransmissionTime() >= transmissionDelay) {
        this.setTransmissionTime(System.currentTimeMillis());
        this.setTransmissionStatus(!this.getTransmissionStatus());
      }
    } else if (operator.getButtonValue(LogitechButton.BUMPER_LEFT)) {
      this.setStageSpeed(.5);
    } else if (operator.getButtonValue(LogitechButton.BUMPER_RIGHT)) {
      this.setStageSpeed(-.5);
    } else {
      this.setStageSpeed(0.0625);
    }

    this.updateStages();
    this.updateTransmission();

  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putBoolean("Arm Forward", this.stageOne.getSensorCollection().isFwdLimitSwitchClosed());
    SmartDashboard.putBoolean("Arm Reverse", this.stageOne.getSensorCollection().isRevLimitSwitchClosed());
  }

  @Override
  public String getSystemName() {
    return "Arm";
  }

  // endregion

}
