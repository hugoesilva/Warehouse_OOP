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
class DoChangePartnerAddress extends Command<WarehouseManager> {

  DoChangePartnerAddress(WarehouseManager receiver) {
    super(Label.CHANGE_PARTNER_ADDRESS, receiver);
    //FIXME add command fields
    addStringField("key", Prompt.partnerKey());
    addStringField("address", Prompt.partnerAddress());
  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    try {
      _receiver.changePartnerAddress(stringField("key"), stringField("address"));
    } catch (UnknownPartnerException e) {
      throw new UnknownPartnerKeyException(e.getUnknownKey());
    }
  }

}