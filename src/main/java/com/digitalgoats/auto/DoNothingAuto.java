package com.digitalgoats.auto;

import com.digitalgoats.systems.SystemsGroup;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;

public class DoNothingAuto extends Auto {

  public DoNothingAuto(SystemsGroup systemsGroup) {
    super("Do Nothing", systemsGroup);
  }

  @Override
  public void execute() {
    System.out.println(MatchData.getOwnedSide(GameFeature.SWITCH_NEAR).toString());
  }

}
