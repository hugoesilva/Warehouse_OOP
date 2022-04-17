package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownPartnerException;
import ggc.exceptions.ProductNotRegisteredException;
import ggc.app.exceptions.*;
//FIXME import classes

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
    addStringField("productKey", Prompt.productKey());
    //FIXME add command fields
  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    try {
      _receiver.toggleProductNotifications(stringField("partnerKey"), stringField("productKey"));
    } catch (UnknownPartnerException e) {
      throw new UnknownPartnerKeyException(e.getUnknownKey());
    } catch (ProductNotRegisteredException e) {
      throw new UnknownProductKeyException(e.getKey());
    }
  }

}
