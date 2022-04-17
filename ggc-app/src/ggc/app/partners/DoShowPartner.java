package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.*;
//FIXME import classes
import ggc.exceptions.UnknownPartnerException;

/**
 * Show partner.
 */
class DoShowPartner extends Command<WarehouseManager> {

  DoShowPartner(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER, receiver);
    //FIXME add command fields
    addStringField("key", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    try {
      _display.popup(_receiver.showPartner(stringField("key")));
    } catch (UnknownPartnerException e) {
      throw new UnknownPartnerKeyException(e.getUnknownKey());
    }
  }

}
