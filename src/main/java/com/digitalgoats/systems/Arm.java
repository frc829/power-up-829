package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.util.LogitechF310;

/**
 * The system for controlling the arm
 * @author Blake
 */
public class Arm implements IGoatSystem {

  // region Constants

  // endregion

  // region Fields

  private double stageOneSpeed;

  // endregion

  // region Objects

  private TalonSRX stageOne;

  // endregion

  // region Constructor

  public Arm() {

    // Setup Fields
    this.setStageOneSpeed(0);

    // Setup Objects
    this.stageOne = new TalonSRX(SystemMap.ARM_STAGEONE_TALON.getValue());

  }

  // endregion

  // region Autonomous Methods

  // endregion

  // region Update Methods

  public void updateStages() {
    this.updateStageOne();
  }

  public void updateStageOne() {
    this.stageOne.set(ControlMode.PercentOutput, this.getStageOneSpeed());
  }

  // endregion

  // region Getters & Setters

  /**
   * Set the speed for both stages
   * @param stageOne
   *  The percent output for stage one
   * @param stageTwo
   *  The percent output for stage two
   */
  public void setStages(double stageOne, double stageTwo) {
    this.setStageOneSpeed(stageOne);
  }

  /** Get stage one's speed */
  public double getStageOneSpeed() {
    return this.stageOneSpeed;
  }
  /** Set stage one's speed */
  public void setStageOneSpeed(double stageOneSpeed) {
    this.stageOneSpeed = stageOneSpeed;
  }

  // endregion

  // region Overridden Methods

  @Override
  public void disabledUpdateSystem() {
    this.updateStages();
  }

  @Override
  public void autonomousUpdateSystem() {
    this.updateStages();
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {
    this.updateStages();
  }

  @Override
  public void updateSmartDashboard() {
  }

  @Override
  public String getSystemName() {
    return "Arm";
  }

  // endregion

}
