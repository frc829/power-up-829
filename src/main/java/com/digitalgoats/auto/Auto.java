package com.digitalgoats.auto;

import com.digitalgoats.systems.Systems;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public abstract class Auto {

  public int step;
  public String name;
  public Systems systems;

  public Auto(String name, Systems systems) {
    this.setName(name);
    this.setSystems(systems);
  }

  public abstract void execute();

  public void addToDashboard(SendableChooser<Auto> chooser, boolean isDefault) {
    if (isDefault) {
      chooser.addDefault(this.getName(), this);
    } else {
      chooser.addObject(this.getName(), this);
    }
  }

  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public Systems getSystems() {
    return this.systems;
  }
  public void setSystems(Systems systems) {
    this.systems = systems;
  }

}
