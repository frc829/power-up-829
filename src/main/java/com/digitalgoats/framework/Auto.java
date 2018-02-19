package com.digitalgoats.framework;

import com.digitalgoats.robot.SystemGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public abstract class Auto {

  private int currentStep;
  private String name;

  private SystemGroup systems;

  public Auto(String name, SystemGroup systems) {

    this.setCurrentStep(0);
    this.setName(name);
    this.setSystems(systems);

  }

  // region Abstracts

  public abstract void execute();

  // endregion

  // region General Auto Methods

  public void addToChooser(SendableChooser<String> chooser, boolean isDefault) {
    if (isDefault) {
      chooser.addDefault(this.getName(), this.getName());
    } else {
      chooser.addObject(this.getName(), this.getName());
    }
  }

  public void nextStep() {
    this.setCurrentStep(this.getCurrentStep() + 1);
  }

  // endregion

  // region Getters & Setters

  public void setCurrentStep(int currentStep) {
    this.currentStep = currentStep;
  }

  public int getCurrentStep() {
    return this.currentStep;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setSystems(SystemGroup systems) {
    this.systems = systems;
  }

  public SystemGroup getSystems() {
    return this.systems;
  }

  // endregion

}
