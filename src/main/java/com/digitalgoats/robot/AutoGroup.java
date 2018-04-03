package com.digitalgoats.robot;

import com.digitalgoats.autos.DriveForwardAuto;
import com.digitalgoats.framework.Auto;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList;

public class AutoGroup {

  private ArrayList<Auto> autos;
  private SendableChooser<String> autoChooser;
  private String selectedAuto;

  public AutoGroup(SystemGroup systems) {
    this.setAutos(new ArrayList<Auto>());
    this.setAutoChooser(new SendableChooser<String>());
    this.getAutos().add(new DriveForwardAuto(systems));
  }

  public void autoInit() {
    this.updateSelectedAuto();
    this.getAuto().setStep(0);
  }

  public void execute() {
    this.updateSelectedAuto();
    this.getAuto().execute();
  }

  public void addToShuffleboard() {
    for (int i = 0; i < this.getAutos().size(); i++) {
      this.getAutos().get(i).addToDashboard(this.getAutoChooser(), i == 0);
    }
    SmartDashboard.putData("autochooser", this.getAutoChooser());
  }

  public void updateSelectedAuto() {
    this.setSelectedAuto(this.getAutoChooser().getSelected());
  }

  public Auto getAuto() {
    for (Auto auto : this.getAutos()) {
      if (auto.getName().equals(this.getSelectedAuto())) {
        return auto;
      }
    }
    return null;
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

  public String getSelectedAuto() {
    return this.selectedAuto;
  }

  public void setSelectedAuto(String selectedAuto) {
    this.selectedAuto = selectedAuto;
  }

}
