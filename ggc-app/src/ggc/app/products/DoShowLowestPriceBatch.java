package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.*;
import ggc.exceptions.ProductNotRegisteredException;


class DoShowLowestPriceBatch extends Command<WarehouseManager> {

    DoShowLowestPriceBatch(WarehouseManager receiver) {
      super(Label.SHOW_LOWEST_PRICE_BATCH, receiver);
      addStringField("productKey", Prompt.productKey());
    }
  
    @Override
    public final void execute() throws CommandException {
      try {
        _display.popup(_receiver.showLowestPriceBatch(stringField("productKey")));
      } catch (ProductNotRegisteredException e) {
        throw new UnknownProductKeyException(e.getKey());
      }
    }
}
