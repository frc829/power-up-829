package com.digitalgoats.robot;

import com.digitalgoats.autos.DoNothingAuto;
import com.digitalgoats.autos.DriveForwardAuto;
import com.digitalgoats.framework.Auto;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList;

public class AutoGroup {

  public ArrayList<Auto> autos;
  public SendableChooser<Auto> autoSendableChooser;

  public AutoGroup(SystemGroup systems) {

    this.autos = new ArrayList<Auto>();
    this.autos.add(new DoNothingAuto(systems));
    this.autos.add(new DriveForwardAuto(systems));

    autoSendableChooser = new SendableChooser<Auto>();
    for (int i = 0; i < this.autos.size(); i++) {
      this.autos.get(i).addToChooser(autoSendableChooser, i == 0);
    }

  }

  // region AutoFunctions

  public void execute() {
    autoSendableChooser.getSelected().execute();
  }

  // endregion

}
