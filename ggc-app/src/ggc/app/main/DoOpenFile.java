package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.io.FileNotFoundException;
import java.io.IOException;

import ggc.WarehouseManager;
import ggc.app.exceptions.FileOpenFailedException;
//FIXME import classes
import ggc.exceptions.UnavailableFileException;

/**
 * Open existing saved state.
 */
class DoOpenFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoOpenFile(WarehouseManager receiver) {
    super(Label.OPEN, receiver);
    //FIXME maybe add command fields
    addStringField("filename", Prompt.openFile());
  }

  @Override
  public final void execute() throws CommandException {
    
    try {
      //FIXME implement command
      _receiver.load(stringField("filename"));
    } catch (UnavailableFileException | FileNotFoundException  ufe) {
      throw new FileOpenFailedException(stringField("filename"));
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

}
