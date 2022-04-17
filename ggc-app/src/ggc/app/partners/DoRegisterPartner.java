package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
//FIXME import classes
import ggc.app.exceptions.DuplicatePartnerKeyException;
import ggc.exceptions.DuplicatePartnerException;

/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    super(Label.REGISTER_PARTNER, receiver);
    //FIXME add command fields
    addStringField("key", Prompt.partnerKey());
    addStringField("name", Prompt.partnerName());
    addStringField("address", Prompt.partnerAddress());
  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    try {
      _receiver.registerPartner(stringField("key"), stringField("name"), stringField("address"));
    } catch (DuplicatePartnerException e) {
      throw new DuplicatePartnerKeyException(e.getKey());
    }
  }

}
