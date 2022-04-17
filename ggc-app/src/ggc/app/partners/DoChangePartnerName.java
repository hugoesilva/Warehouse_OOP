package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownPartnerException;

class DoChangePartnerName extends Command<WarehouseManager> {


    DoChangePartnerName(WarehouseManager receiver) {
        super(Label.CHANGE_PARTNER_NAME, receiver);
        //FIXME add command fields
        addStringField("key", Prompt.partnerKey());
        addStringField("name", Prompt.partnerName());

    }

@Override
  public void execute() throws CommandException {
    //FIXME implement command
    try {
      _receiver.changePartnerName(stringField("key"), stringField("name"));
    } catch (UnknownPartnerException e) {
      throw new UnknownPartnerKeyException(e.getUnknownKey());
    }
  }
}