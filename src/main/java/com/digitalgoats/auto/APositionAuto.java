package com.digitalgoats.auto;

import com.digitalgoats.systems.SystemsGroup;

public class APositionAuto extends Auto {

  public APositionAuto(SystemsGroup systemsGroup) {
    super("A Position", systemsGroup);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      case 0: {
        break;
      }

    }
  }

}
