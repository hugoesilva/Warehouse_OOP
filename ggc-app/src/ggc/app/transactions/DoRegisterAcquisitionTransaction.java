package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.ProductNotRegisteredException;
import pt.tecnico.uilib.forms.Form;
import ggc.exceptions.UnknownPartnerException;
import ggc.app.exceptions.*;
//FIXME import classes

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    //FIXME maybe add command fields
    addStringField("partnerKey", Prompt.partnerKey());
    addStringField("productKey", Prompt.productKey());
    addRealField("price", Prompt.price());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    try {
      _receiver.registerAcquisitionTransaction(stringField("partnerKey"), stringField("productKey"), realField("price"), integerField("amount"));
    } catch (ProductNotRegisteredException e) {
      boolean answer = Form.confirm(Prompt.addRecipe());
      if (!answer) {
        _receiver.AcquisitionTransactionSimpleProduct(stringField("partnerKey"), stringField("productKey"), realField("price"), integerField("amount"));
      }
      else {
        String recipe = "";
        int numberOfComponents = Form.requestInteger(Prompt.numberOfComponents());
        double alpha = Form.requestReal(Prompt.alpha());
        for (int i = 0; i < numberOfComponents; i++) {
          String productKey = Form.requestString(Prompt.productKey());
          int amount = Form.requestInteger(Prompt.amount());
          if (i != numberOfComponents - 1) {
            recipe += productKey + ":" + amount + "#";
          }
          else {
            recipe += productKey + ":" + amount;
          }
        }
        _receiver.AcquisitionTransactionComplexProduct(stringField("partnerKey"), stringField("productKey"),
         realField("price"), integerField("amount"), alpha, recipe);
      }
    } catch (UnknownPartnerException e) {
      throw new UnknownPartnerKeyException(e.getUnknownKey());
    }
  }

}
