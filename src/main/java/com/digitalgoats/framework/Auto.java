package com.digitalgoats.framework;

import com.digitalgoats.robot.SystemGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public abstract class Auto {

  private int step;
  private String name;
  private SystemGroup systems;

  public Auto(String name, SystemGroup systems) {
    this.setStep(0);
    this.setName(name);
    this.setSystems(systems);
  }

  public void addToDashboard(SendableChooser<String> chooser, boolean isDefault) {
    if (isDefault) {
      chooser.addDefault(this.getName(), this.getName());
      return;
    }
    chooser.addObject(this.getName(), this.getName());
    return;
  }

  public abstract void execute();

  public int getStep() {
    return this.step;
  }

  public void setStep(int step) {
    this.step = step;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SystemGroup getSystems() {
    return this.systems;
  }

  public void setSystems(SystemGroup systems) {
    this.systems = systems;
  }

}
