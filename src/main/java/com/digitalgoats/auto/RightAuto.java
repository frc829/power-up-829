package com.digitalgoats.auto;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.systems.SystemsGroup;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class RightAuto extends Auto {

  private OwnedSide switchSide, scaleSide;

  public RightAuto(SystemsGroup systemsGroup) {
    super("Right Auto", systemsGroup);
  }

  @Override
  public void execute() {
    switchSide = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR);
    scaleSide = MatchData.getOwnedSide(GameFeature.SCALE);
    if (switchSide == OwnedSide.RIGHT && scaleSide == switchSide) {
      this.bothOn();
    } else if (switchSide == OwnedSide.RIGHT) {
      this.switchOn();
    } else if (scaleSide == OwnedSide.RIGHT) {
      this.scaleOn();
    } else {
      this.noneOn();
    }
  }

  public void bothOn() {
    switch (this.getStep()) {

      case 0: {
        break;
      }

    }
  }

  public void switchOn() {
    switch (this.getStep()) {

      case 0: {
        break;
      }

    }
  }

  public void scaleOn() {
    switch (this.getStep()) {

      case 0: {
        break;
      }

    }
  }

  public void noneOn() {
    switch (this.getStep()) {

      case 0: {
        break;
      }

    }
  }

}
