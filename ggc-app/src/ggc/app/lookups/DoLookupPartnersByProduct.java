package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.*;
import ggc.app.exceptions.*;
//FIXME import classes

/**
 * Lookup payments by given partner.
 */
public class DoLookupPartnersByProduct extends Command<WarehouseManager> {

  public DoLookupPartnersByProduct(WarehouseManager receiver) {
    super(Label.PARTNERS_BY_PRODUCT, receiver);
    //FIXME add command fields
    addStringField("productKey", Prompt.productKey());

  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    try {
      _display.popup(_receiver.showPartnersByProduct(stringField("productKey")));
    } catch (ProductNotRegisteredException e) {
      throw new UnknownProductKeyException(e.getKey());
    }
  }

}
