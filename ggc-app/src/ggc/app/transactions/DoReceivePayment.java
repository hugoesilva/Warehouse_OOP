package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownTransactionException;
import ggc.app.exceptions.*;
//FIXME import classes

/**
 * Receive payment for sale transaction.
 */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    super(Label.RECEIVE_PAYMENT, receiver);
    //FIXME add command fields
    addIntegerField("key", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    try {
      _receiver.receivePayment(integerField("key"));
    } catch (UnknownTransactionException e) {
      throw new UnknownTransactionKeyException(e.getKey());
    }
  }

}
