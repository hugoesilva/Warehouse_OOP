package ggc.app.partners;

import ggc.WarehouseManager;

/** Partners menu. */
public class Menu extends pt.tecnico.uilib.menus.Menu {

  /** @param receiver command executor */
  public Menu(WarehouseManager receiver) {
    super(Label.TITLE, //
        new DoShowPartner(receiver), //
        new DoShowAllPartners(receiver), //
        new DoRegisterPartner(receiver), //
        new DoToggleProductNotifications(receiver), //
        new DoShowPartnerAcquisitions(receiver), //      
        new DoShowPartnerSales(receiver), //
        new DoChangePartnerName(receiver),
        new DoShowPartnerWithMostPoints(receiver),
        new DoDeletePartner(receiver),
        new DoChangePartnerAddress(receiver)
    );
  }

}
