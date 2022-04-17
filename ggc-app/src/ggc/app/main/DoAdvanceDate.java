package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exceptions.InvalidDateException;

import ggc.WarehouseManager;
//FIXME import classes
import ggc.exceptions.InvalidDateExceptionC;

/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    //FIXME add command fields
    addIntegerField("days", Prompt.daysToAdvance());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    try {
      _receiver.advanceDate(integerField("days"));
    } catch (InvalidDateExceptionC e) {
      throw new InvalidDateException(e.getDays());
    }
  }

}
