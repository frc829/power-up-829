package com.digitalgoats.auto;

import com.digitalgoats.systems.SystemsGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList;

/**
 * This class combines all of the individual autos
 * @author Blake
 */
public class AutosGroup {

  // Objects
  public ArrayList<Auto> autosGroup;
  public String selectedKey;

  /**
   * Add autos to AutosGroup
   */
  public AutosGroup(SystemsGroup systemsGroup) {

    // Initialize AutoGroup
    autosGroup = new ArrayList<Auto>();
    selectedKey = null;

    autosGroup.add(new MiddleAuto(systemsGroup));
    autosGroup.add(new RightAuto(systemsGroup));
    autosGroup.add(new LeftAuto(systemsGroup));

  }

  /**
   * Set selected key based on chooser
   * @param chooser
   *  The SendableChooser for Auto
   */
  public void setSelectedAuto(SendableChooser<String> chooser) {
    this.selectedKey = chooser.getSelected();
    this.findAutoByName(this.selectedKey).setStep(0);
  }

  /**
   * Execute the selected Autonomous
   */
  public void executeAutonomous() {
    System.out.println(this.findAutoByName(this.selectedKey).getStep());
    this.findAutoByName(this.selectedKey).execute();
  }

  /**
   * Find an Auto based on the name provided
   * @param name
   *  The name of the desired auto
   * @return
   *  null if no auto found
   */
  public Auto findAutoByName(String name) {
    for (Auto auto : autosGroup) {
      if (auto.getName() == name) {
        return auto;
      }
    }
    return null;
  }

  /**
   * Create a chooser for selecting Autonomous
   * @return
   *  SendableChooser filled with Autonomous Modes
   */
  public SendableChooser<String> createSendableChooser() {

    SendableChooser<String> chooser = new SendableChooser<String>();
    for (int i = 0; i < this.autosGroup.size(); i++) {
      this.autosGroup.get(i).addToChooser(chooser, i == 0);
    }
    return chooser;

  }

}
