package com.digitalgoats.auto;

import com.digitalgoats.systems.SystemsGroup;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

/**
 * Simple Do Nothing Auto
 * @author Blake
 */
public class DoNothingAuto extends Auto {

  public DoNothingAuto(SystemsGroup systemsGroup) {
    super("Do Nothing", systemsGroup);
  }

  @Override
  public void execute() {

    // Print the owned side for alliance's switch
    System.out.println(MatchData.getOwnedSide(GameFeature.SWITCH_NEAR));

  }

}
