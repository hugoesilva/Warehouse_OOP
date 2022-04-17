package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnavailableProductExceptionC;
import ggc.exceptions.UnknownPartnerException;
import ggc.exceptions.ProductNotRegisteredException;
import ggc.app.exceptions.*;
//FIXME import classes

/**
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    //FIXME maybe add command fields
    addStringField("partnerKey", Prompt.partnerKey());
    addIntegerField("deadline", Prompt.paymentDeadline());
    addStringField("productKey", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    try {
      _receiver.registerSaleTransaction(stringField("partnerKey"), integerField("deadline"),
       stringField("productKey"), integerField("amount"));
    } catch (UnknownPartnerException e) {
      throw new UnknownPartnerKeyException(e.getUnknownKey());
    } catch (ProductNotRegisteredException e) {
      throw new UnknownProductKeyException(e.getKey());
    } catch (UnavailableProductExceptionC e) {
      throw new UnavailableProductException(e.getKey(), e.getRequested(), e.getAvailable());
    }
  }

}
