package ggc;

import java.util.*;

public class EmailDelivery implements DeliveryMethod {

  public String deliver(ArrayList<Notification> notifications) {
    String out = "\nEMAIL || ";
    for (Notification n : notifications) {
      out += n.showNotification() + "\n";
    }
    int index = out.length() - 1;
    if (index >= 0) {
      return out.substring(0, index);
    }
    else {
      return out;
    }
  }
}
