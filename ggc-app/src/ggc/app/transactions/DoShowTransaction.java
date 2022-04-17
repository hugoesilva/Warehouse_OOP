package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.*;
import ggc.exceptions.UnknownTransactionException;
//FIXME import classes

/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    //FIXME maybe add command fields
    addIntegerField("transactionKey", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    try {
      _display.popup(_receiver.showTransaction(integerField("transactionKey")));
    } catch (UnknownTransactionException e) {
      throw new UnknownTransactionKeyException(e.getKey());
    }
  }

}
