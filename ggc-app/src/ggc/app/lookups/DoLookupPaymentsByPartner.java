package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.*;
//FIXME import classes

/**
 * Lookup payments by given partner.
 */
public class DoLookupPaymentsByPartner extends Command<WarehouseManager> {

  public DoLookupPaymentsByPartner(WarehouseManager receiver) {
    super(Label.PAID_BY_PARTNER, receiver);
    //FIXME add command fields
    addStringField("key", Prompt.partnerKey());

  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    try {
      _display.popup(_receiver.paymentsByPartner(stringField("key")));
    } catch (UnknownPartnerException e) {
      throw new UnknownPartnerKeyException(e.getUnknownKey());
    }
  }

}
