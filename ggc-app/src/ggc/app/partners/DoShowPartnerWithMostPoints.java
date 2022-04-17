package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.*;
//FIXME import classes
import ggc.exceptions.*;

/**
 * Show partner.
 */
class DoShowPartnerWithMostPoints extends Command<WarehouseManager> {

  DoShowPartnerWithMostPoints(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_WITH_MOST_POINTS, receiver);
    //FIXME add command fields
  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    _display.popup(_receiver.showPartnerWithMostPoints());
  }

}
