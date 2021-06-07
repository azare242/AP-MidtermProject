package zare.alireza.GameLogic.phases;

import zare.alireza.GameLogic.ManageGame.Game;

public abstract class Night implements Phase{

    @Override
    public abstract void phaseLoop(Game game);
}
