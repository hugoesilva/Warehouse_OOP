package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.*;
//FIXME import classes

/**
 * Show batches supplied by partner.
 */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  DoShowBatchesByPartner(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);
    //FIXME maybe add command fields
    addStringField("partnerKey", Prompt.partnerKey());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    try {
      _display.popup(_receiver.showBatchesByPartner(stringField("partnerKey")));
    } catch (UnknownPartnerException e) {
      throw new UnknownPartnerKeyException(e.getUnknownKey());
    }
  }

}
