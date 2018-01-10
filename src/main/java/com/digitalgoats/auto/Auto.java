package com.digitalgoats.auto;

import com.digitalgoats.systems.SystemsGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * The general class for creating an Autonomous
 * @author Blake
 */
public abstract class Auto {

  // Fields
  public int step;
  public String name;
  public SystemsGroup systemsGroup;

  /**
   * Create an auto
   * @param name
   *  The autonomous name
   * @param systemsGroup
   *  The group of systems to be used
   */
  public Auto(String name, SystemsGroup systemsGroup) {
    this.setName(name);
    this.setSystemsGroup(systemsGroup);
  }

  /** Execute the selected auto */
  public abstract void execute();

  /**
   * Add Auto to SendableChooser
   * @param chooser
   *  The SendableChooser to be used
   * @param isDefault
   */
  public void addToChooser(SendableChooser<String> chooser, boolean isDefault) {
    if (isDefault) {
      chooser.addDefault(this.getName(), this.getName());
    } else {
      chooser.addObject(this.getName(), this.getName());
    }
  }

  /** Get step */
  public int getStep() {
    return this.step;
  }

  /** Set step */
  public void setStep(int step) {
    this.step = step;
  }

  /** Get name */
  public String getName() {
    return this.name;
  }

  /** Set name */
  public void setName(String name) {
    this.name = name;
  }

  /** Get group of systems */
  public SystemsGroup getSystemsGroup() {
    return this.systemsGroup;
  }

  /** Set group of systems */
  public void setSystemsGroup(SystemsGroup systemsGroup) {
    this.systemsGroup = systemsGroup;
  }

}
