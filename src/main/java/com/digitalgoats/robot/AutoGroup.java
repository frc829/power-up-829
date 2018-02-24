package com.digitalgoats.robot;

import com.digitalgoats.framework.Auto;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList;

public class AutoGroup {

  private ArrayList<Auto> autos;
  private SendableChooser<String> autoChooser;

  public AutoGroup(SystemGroup systems) {
    this.setAutos(new ArrayList<Auto>());
  }

  public void addToShuffleboard() {
    for (int i = 0; i < this.getAutos().size(); i++) {
      this.getAutos().get(i).addToDashboard(this.getAutoChooser(), i == 0);
    }
    SmartDashboard.putData("autochooser", this.getAutoChooser());
  }

  public ArrayList<Auto> getAutos() {
    return this.autos;
  }

  public void setAutos(ArrayList<Auto> autos) {
    this.autos = autos;
  }

  public SendableChooser<String> getAutoChooser() {
    return this.autoChooser;
  }

  public void setAutoChooser(SendableChooser<String> autoChooser) {
    this.autoChooser = autoChooser;
  }

}
