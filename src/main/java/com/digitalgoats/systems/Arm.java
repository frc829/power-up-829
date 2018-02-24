package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
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

  public static final int BOTTOM_POSITION = 0, SWITCH_POSITION = 1, SCALE_POSITION = 2, TOP_POSITION = 3;

  // endregion

  // region Fields

  private double stageSpeed;
  private int lastSwitch, targetSwitch;

  // endregion

  // region Objects

  private DigitalInput scalePosition, switchPosition;
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
    this.stageOne = new TalonSRX(SystemMap.ARM_STAGEONE_TALON.getValue());
    this.stageOne.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
    this.stageOne.setSelectedSensorPosition(0,0,10);
    this.stageTwo = new TalonSRX(SystemMap.ARM_STAGETWO_TALON.getValue());

  }

  // endregion

  // region Autonomous Methods

  // endregion

  // region Update Methods

  public void updateStages() {

    System.out.println(this.stageOne.getSelectedSensorPosition(0) + " Position");
    System.out.println(this.stageOne.getSelectedSensorVelocity(0) + " Velocity");
    this.stageOne.set(ControlMode.PercentOutput, this.getStageSpeed());
    this.stageTwo.follow(this.stageOne);

  }

  // endregion

  // region Getters & Setters

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

  public boolean canGoDirection(double speed) {
    if (this.stageOne.getSensorCollection().isFwdLimitSwitchClosed() && speed > 0) {
      return false;
    } else if (this.stageOne.getSensorCollection().isRevLimitSwitchClosed() && speed < 0) {
      return false;
    }
    return true;
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {

    this.setLastSwitch(
        this.stageOne.getSensorCollection().isFwdLimitSwitchClosed() ? TOP_POSITION :
            this.stageOne.getSensorCollection().isRevLimitSwitchClosed() ? BOTTOM_POSITION :
                this.switchPosition.get() ? SWITCH_POSITION :
                    this.scalePosition.get() ? SCALE_POSITION : this.getLastSwitch()
    );

    //if (Math.abs(operator.getAxisValue(LogitechAxis.LEFT_Y)) > .1 && canGoDirection(-operator.getAxisValue(LogitechAxis.LEFT_Y))) {
      //if(this.stageOne.getSelectedSensorPosition(0) < )

      this.setStageSpeed(
          .65 * Math.pow(-operator.getAxisValue(LogitechAxis.LEFT_Y), 3) + ((1 - .65) * -operator
              .getAxisValue(LogitechAxis.LEFT_Y)));
    //} else {
      //this.setStageSpeed(0.0625);
      this.updateStages();

    }


    @Override
    public void updateSmartDashboard() {



    SmartDashboard.putNumber("Arm: Stage Speed", this.getStageSpeed());
    SmartDashboard.putNumber("Arm: Stage Velocity", this.stageOne.getSelectedSensorVelocity(0));
  }

  @Override
  public String getSystemName() {
    return "Arm";
  }

  // endregion

}
