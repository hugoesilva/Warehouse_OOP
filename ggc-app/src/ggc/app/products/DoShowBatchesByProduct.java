package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.*;
import ggc.exceptions.ProductNotRegisteredException;
//FIXME import classes

/**
 * Show all products.
 */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  DoShowBatchesByProduct(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    //FIXME maybe add command fields
    addStringField("productKey", Prompt.productKey());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    try {
      _display.popup(_receiver.showBatchesByProduct(stringField("productKey")));
    } catch (ProductNotRegisteredException e) {
      throw new UnknownProductKeyException(e.getKey());
    }
  }

}
