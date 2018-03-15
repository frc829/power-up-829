package com.digitalgoats.framework;

import com.digitalgoats.robot.SystemGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * The super class for Autonomous modes
 */
public abstract class Auto {

  /** The current step for autonomous */
  private int step;
  /** The name for the autonomous */
  private String name = "";
  /** The systems available during autonomous */
  private SystemGroup systems;

  /**
   * Create an autonomous
   * @param name
   *  The name of the autonomous
   * @param systems
   *  The systems available
   */
  public Auto(String name, SystemGroup systems) {
    this.setStep(0);
    this.setName(name);
    this.setSystems(systems);
  }

  /**
   * Add the auto to the chooser for Shuffleboard
   * @param chooser
   *  The SendableChooser for auto selection
   */
  public void addToDashboard(SendableChooser<String> chooser, boolean isDefault) {
    if (isDefault) {
      chooser.addDefault(this.getName(), this.getName());
      return;
    }
    chooser.addObject(this.getName(), this.getName());
    return;
  }

  /**
   * Go to the next step
   */
  public void nextStep() {
    this.setStep(this.getStep() + 1);
  }

  /**
   * Execute the selected autonomous
   */
  public abstract void execute();

  /** Get the current step */
  public int getStep() {
    return this.step;
  }

  /** Set the current step */
  public void setStep(int step) {
    this.step = step;
  }

  /** Get the autonomous's name */
  public String getName() {
    return this.name;
  }

  /** Set the autonomous's name */
  public void setName(String name) {
    this.name = name;
  }

  /** Get the group of systems */
  public SystemGroup getSystems() {
    return this.systems;
  }

  /** Set the group of systems */
  public void setSystems(SystemGroup systems) {
    this.systems = systems;
  }

}
