package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
//FIXME import classes

/**
 * Lookup products cheaper than a given price.
 */
public class DoLookupPaidLate extends Command<WarehouseManager> {

  public DoLookupPaidLate(WarehouseManager receiver) {
    super(Label.PAID_LATE, receiver);
    //FIXME add command fields
    addIntegerField("delay", Prompt.delay());
  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    _display.popup(_receiver.showPaidLate(integerField("delay")));
  }

}