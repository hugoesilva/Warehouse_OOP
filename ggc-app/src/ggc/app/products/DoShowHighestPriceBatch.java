package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.*;
import ggc.exceptions.ProductNotRegisteredException;
import ggc.exceptions.*;
//FIXME import classes

/**
 * Show all products.
 */
class DoShowHighestPriceBatch extends Command<WarehouseManager> {

  DoShowHighestPriceBatch(WarehouseManager receiver) {
    super(Label.SHOW_HIGHEST_PRICE_BATCH, receiver);
    //FIXME maybe add command fields
    addStringField("productKey", Prompt.productKey());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    try {
      _display.popup(_receiver.showHighestPriceBatch(stringField("productKey")));
    } catch (ProductNotRegisteredException e) {
      throw new UnknownProductKeyException(e.getKey());
    } catch (UnavailableProductExceptionC e) {
        throw new UnavailableProductException(e.getKey(), 1, 0);
    }
  }

}
