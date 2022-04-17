package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
//FIXME import classes
import ggc.app.exceptions.*;
import ggc.exceptions.*;

/**
 * Register new partner.
 */
class DoDeletePartner extends Command<WarehouseManager> {

  DoDeletePartner(WarehouseManager receiver) {
    super(Label.DELETE_PARTNER, receiver);
    //FIXME add command fields
    addStringField("key", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    try {
      _receiver.deletePartner(stringField("key"));
    } catch (UnknownPartnerException e) {
      throw new UnknownPartnerKeyException(e.getUnknownKey());
    }
  }

}