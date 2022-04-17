package ggc;
import java.io.BufferedReader;
import java.util.*;
import java.io.Serializable;

import ggc.Transaction;
import ggc.exceptions.*;
import java.io.*;
import java.text.Collator;
import java.util.Comparator;
import java.util.Iterator;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  //------------------------------------------------------------------------------------

  /**
   * Class CollatorWrapper implements a collator that can be serialized.
   */

  class CollatorWrapper implements Comparator<String>, Serializable {
    private static final long serialVersionUID = 202110251850L;

    private transient Collator _collator = Collator.getInstance(Locale.getDefault());

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
      ois.defaultReadObject();
      _collator = Collator.getInstance(Locale.getDefault());
    }

    @Override
    public int compare(String s1, String s2) { return _collator.compare(s1, s2); }
  }

  //------------------------------------------------------------------------------------
  /** Current date */
  private int _date;
  /** Number of transactions registered */
  private int _transactionID = 0;
  /** Partners */
  private TreeMap<String, Partner> _partners = new TreeMap<String, Partner>(new CollatorWrapper());
  /** Products */
  private TreeMap<String, Product> _products = new TreeMap<String, Product>(new CollatorWrapper());
  /** Batches of products */
  private TreeSet<Batch> _batches = new TreeSet<Batch>();
  /** Registered Transactions */
  private TreeMap<Integer, Transaction> _transactionHistory = new TreeMap<Integer, Transaction>();
  /** Current available balance */
  private double _availableBalance;



// xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

  // code added of our own iniciative, in order to pratice of an exam


  public void changePartnerName(String partnerKey, String name) throws UnknownPartnerException {
    Partner p = _partners.get(partnerKey.toUpperCase());
    if (p == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    p.setName(name);
  }

  public String showLowestPriceBatch(String productKey) throws ProductNotRegisteredException {
    Product p = _products.get(productKey);
    if (p == null) {
      throw new ProductNotRegisteredException(productKey);
    }
    Batch b = getLowestPriceBatch(productKey);
    return b.showBatch();
  }


  public String showHighestPriceBatch(String productKey) throws ProductNotRegisteredException, UnavailableProductExceptionC {
    Product p = _products.get(productKey);
    if (p == null) {
      throw new ProductNotRegisteredException(productKey);
    }
    Batch b = getHighestPriceBatch(productKey);
    if (b == null) {
      throw new UnavailableProductExceptionC(productKey, 0, 0);
    }
    return b.showBatch();
  }


  public Batch getHighestPriceBatch(String productKey) {
    Batch highest = null;
    Iterator<Batch> iterator = _batches.iterator();
    while(iterator.hasNext()) {
      Batch b = iterator.next();
      if (productKey.equals(b.getProductKey())) {
        if (highest == null) {
          highest = b;
        }
        else {
          if (b.getPricePerUnit() > highest.getPricePerUnit()) {
            highest = b;
          }
        }
      }
    }
    return highest;
  }


  public String showPartnerWithMostPoints() {
    Partner p = null;
    int points = 0;
    for (Map.Entry<String, Partner> entry : _partners.entrySet()) {
      if (p == null) {
        p = entry.getValue();
      }
      if (entry.getValue().getPoints() > points) {
        p = entry.getValue();
        points = p.getPoints();
      }
    }
    return p.showPartner();
  }

  public void deletePartner(String partnerKey) throws UnknownPartnerException {
    Partner p = _partners.get(partnerKey.toUpperCase());
    if (p == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    else {
      _partners.remove(partnerKey.toUpperCase());
    }
  }

  public void changePartnerAddress(String partnerKey, String address) throws UnknownPartnerException {
    Partner p = _partners.get(partnerKey.toUpperCase());
    if (p == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    else {
      p.setAddress(address);
    }
  }


  public String productsByPartner(String partnerKey) throws UnknownPartnerException {
    Partner p = _partners.get(partnerKey.toUpperCase());
    if (p == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    return p.showProductsBoughtByPartner();
  }

  public String showPaidLate(int delay) {
    String out = "";
        for (Map.Entry<Integer, Transaction> entry : _transactionHistory.entrySet()) {
          Transaction t = entry.getValue();
          if (t.isPaidByPartner()) {
            if (t.getPayDay() - t.getDeadLine() > delay)
              out += t.showTransaction() + "\n";
          }
        }
        int index = out.length() - 1;
        if (index >= 0) {
            return out.substring(0, index);
        }
        else {
            return out;
        }
  }

  public String showPartnersByProduct(String productKey) throws ProductNotRegisteredException {
    Product p = _products.get(productKey);
    if (p == null) {
      throw new ProductNotRegisteredException(productKey);
    }
    String out = "";
        for (Map.Entry<Integer, Transaction> entry : _transactionHistory.entrySet()) {
          Transaction t = entry.getValue();
          if (t.getProductKey().equals(productKey) && t.isPaidByPartner())
            out += t.getPartner().showPartner() + "\n";
        }
        int index = out.length() - 1;
        if (index >= 0) {
            return out.substring(0, index);
        }
        else {
            return out;
        }
  }

// xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx


  /**
   * Shows available balance
   * @return available balance
   */
  public double getAvailableBalance() {
    return _availableBalance;
  }

  /**
   * Calculates accounting balance on the present date
   * @return accounting balance
   */
  public double getAccountingBalance() {
    double pendingSalesValue = 0;
    for (Map.Entry<Integer, Transaction> entry : _transactionHistory.entrySet()) {
      if (entry.getValue().getPayDay() < 0) {
        // if payday is negative then its a pending sale
        pendingSalesValue += calculateSaleTransaction(entry.getValue());
      }
    }
    return _availableBalance + pendingSalesValue;

  }

  //------------------------------------------------------------------------------------


    /**
     * calculates the price of a sale transaction
     * @param t transaction
     * @return sale value depending on the deadline and the present date
     */
  public double calculateSaleTransaction(Transaction t) {
    Sale sale = (Sale) t;
    Partner partner = sale.getPartner();
    double saleValue = sale.getPrice();
    int deadline = sale.getDeadLine();
    Product product = sale.getProduct();
    // dateModifier eh o numero N de dias de um produto (simples ou derivado) a partir do qual
    // o preco da venda eh calculado
    return partner.calculateSale(saleValue, deadline, _date, product.getDateModifier());
  }

  /**
   * Receives pending sales payments
   * @param transactionKey pending sale's key
   * @throws UnknownTransactionException
   */
  public void receivePayment(int transactionKey) throws UnknownTransactionException {
    Transaction t = _transactionHistory.get(transactionKey);
    if (t == null) {
      throw new UnknownTransactionException(transactionKey);
    }
    else if (t.getPayDay() >= 0) {
      return;
    }
    double paid = calculateSaleTransaction(t);
    _availableBalance += paid;
    Partner partner = t.getPartner();
    Sale s = (Sale) t;
    partner.updatePoints(_date, s.getDeadLine(), paid);
    partner.setPaidSalesByWarehouseValue(paid);
    s.setAddedTaxPrice(paid);
    t.setPayDay(_date);
  }


  /**
   * shows transactions that have been paid by a certain partner
   * @param partnerKey partner's key
   * @return a string that contains all transactions paid by a certain partner
   * @throws UnknownPartnerException
   */
  public String paymentsByPartner(String partnerKey) throws UnknownPartnerException {
    if (_partners.get(partnerKey.toUpperCase()) == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    String out = "";
        for (Map.Entry<Integer, Transaction> entry : _transactionHistory.entrySet()) {
          Transaction t = entry.getValue();
          if (t.getPartner().getKey().equals(partnerKey) && t.getPayDay() >= 0 && t.isPaidByPartner())
            out += t.showTransaction() + "\n";
        }
        int index = out.length() - 1;
        if (index >= 0) {
            return out.substring(0, index);
        }
        else {
            return out;
        }
  }

  //------------------------------------------------------------------------------------

  /**
   * Adds a product to the warehouse.
   * @param productKey product's key
   * @param price product's price
   * @param amount number of product units
   */

  public void addProduct(String productKey, double price, int amount) {
    Product p = _products.get(productKey);
    if (p == null) {
      p = new Product(productKey);
      _products.put(productKey, p);
    }
    activateProductNotifications(productKey);
    notifyAllPartners(new First(p, price));
    updateProductMaxPriceAndStock(p, price, amount);
  }

  /**
   * Updates a product's max price and its current stock
   * @param p product
   * @param price product's price
   * @param amount number of product units
   */

  public void updateProductMaxPriceAndStock(Product p, double price, int amount) {
    if (p.getMaxPrice() < price) {
      p.setMaxPrice(price);
    }
    p.updateStock(amount);
  }

  /**
   * Obtains a complex product recipe.
   * @param recipeString Recipe's components and respective quantities
   * @param alphaFactor Complex product price multiplier
   * @return a recipe
   */

  public Recipe createRecipe(String recipeString, double alphaFactor) {
    String[] components = recipeString.split("#");

    Recipe r = new Recipe(components.length, alphaFactor);

    for (int i = 0; i < components.length; i++) {
      String[] componentAndAmount = components[i].split(":");
      r.setComponentAndAmount(i, componentAndAmount[0], Integer.parseInt(componentAndAmount[1]));
    }
    return r;
  }

  /**
   * Adds a complex product to the warehouse.
   * @param productKey product's name
   * @param price product's price
   * @param amount number of product units
   * @param alphaFactor Complex product price multiplier
   * @param recipeString Recipe's components and respective quantities
   */

  public boolean addComplexProduct(String productKey, double price, int amount, double alphaFactor, String recipeString) {
    Product p = _products.get(productKey);
    if (p == null) {
      Recipe recipe = createRecipe(recipeString, alphaFactor);
      for (int i = 0; i < recipe.getComponents().length; i++) {
        String component = recipe.getComponent(i);
        if (_products.get(component) == null) {
          //maybe throw exception
          return false;
        }
      }
      p = new ComplexProduct(productKey, recipe);
      _products.put(productKey, p);
    }
    activateProductNotifications(productKey);
    notifyAllPartners(new First(p, price));
    updateProductMaxPriceAndStock(p, price, amount);
    return true;
  }

  /**
   * Shows all products registered in the warehouse.
   * @return a string that contains all products registered
   */

  public String showAllProducts() {
    String out = "";
    for (Map.Entry<String, Product> entry : _products.entrySet()) {
      out += entry.getValue().showProduct() + "\n";
    }
    int index = out.length() - 1;
    if (index >= 0) {
      return out.substring(0, index);
    }
    else {
      return out;
    }
  }

  /**
   * activates a certain product's notifications
   * @param productKey product's key
   */

  public void activateProductNotifications(String productKey) {
    for (Map.Entry<String, Partner> entry : _partners.entrySet()) {
      entry.getValue().toggleProductNotifications(productKey);
    }
  }

  //------------------------------------------------------------------------------------

  /**
   * Adds a batch of products to the warehouse.
   * @param b batch
   */

  public void addBatch(Batch b) {
    _batches.add(b);
  }

  /**
   * Shows all available batches in the warehouse.
   * @return a string that contains all available batches
   */

  public String showAvailableBatches() {
    Iterator<Batch> iterator = _batches.iterator();
    String out = "";
    while(iterator.hasNext()) {
      Batch b = iterator.next();
      out += b.showBatch() + "\n";
    }
    int index = out.length() - 1;
    if (index >= 0) {
      return out.substring(0, index);
    }
    else {
      return out;
    }
  }

  /**
   * Shows all the batches provided by a certain given partner.
   * @param partnerKey
   * @return a string that contains all the batches provided by a certain given partner.
   */

  public String showBatchesByPartner(String partnerKey) throws UnknownPartnerException {
    if (_partners.get(partnerKey.toUpperCase()) == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    Iterator<Batch> iterator = _batches.iterator();
    String out = "";
    while(iterator.hasNext()) {
      Batch b = iterator.next();
      if (b.getBatchPartnerKey().equals(partnerKey)) {
        out += b.showBatch() + "\n";
      }
    }
    int index = out.length() - 1;
    if (index >= 0) {
      return out.substring(0, index);
    }
    else {
      return out;
    }
  }

  /**
   * Shows all batches that contain a certain given product.
   * @param productKey
   * @return a string that contains all batches that contain a certain given product.
   */

  public String showBatchesByProduct(String productKey) throws ProductNotRegisteredException {
    if (_products.get(productKey) == null) {
      throw new ProductNotRegisteredException(productKey);
    }
    Iterator<Batch> iterator = _batches.iterator();
    String out = "";
    while(iterator.hasNext()) {
      Batch b = iterator.next();
      if (b.getProductKey().equals(productKey)) {
        out += b.showBatch() + "\n";
      }
    }
    int index = out.length() - 1;
    if (index >= 0) {
      return out.substring(0, index);
    }
    else {
      return out;
    }
  }

  /**
   * shows all batches available in the warehouse under a certain price
   * @param price price upper limit
   * @return a string that contains all bacthes under the given price limit
   */

  public String showBatchesUnderGivenPrice(double price) {
    Iterator<Batch> iterator = _batches.iterator();
    String out = "";
    while(iterator.hasNext()) {
      Batch b = iterator.next();
      if (b.getPricePerUnit() < price) {
        out += b.showBatch() + "\n";
      }
    }
    int index = out.length() - 1;
    if (index >= 0) {
      return out.substring(0, index);
    }
    else {
      return out;
    }
  }

  //------------------------------------------------------------------------------------

  /**
   * Registers a partner to the warehouse.
   * @param key partner's key
   * @param name partner's name
   * @param address partner's adress
   * @throws DuplicatePartnerException
   */

  public void registerPartner(String key, String name, String address) throws DuplicatePartnerException {
    String keyToCaps = key.toUpperCase();
    Partner p = _partners.get(keyToCaps);
    if (p == null) {
      p = new Partner(key, name, address);
      _partners.put(keyToCaps, p);
    }
    else {
      throw new DuplicatePartnerException(key);
    }
  }

  /**
   * Shows all partners registered in the warehouse.
   * @return a string that contains all partners registered in the warehouse.
   */

  public String showAllPartners() {
    String out = "";
    for (Map.Entry<String, Partner> entry : _partners.entrySet()) {
      out += entry.getValue().showPartner() + "\n";
    }
    int index = out.length() - 1;
    if (index >= 0) {
      return out.substring(0, index);
    }
    else {
      return out;
    }
  }

  /**
   * Shows a single partner registered in the warehouse given a key.
   * @param partnerKey
   * @return a string that contains a single partner registered in the warehouse given a key.
   * @throws UnknownPartnerException
   */

  public String showPartner(String partnerKey) throws UnknownPartnerException {
    Partner p = _partners.get(partnerKey.toUpperCase());
    if (p == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    else {
      DeliveryMethod d = new OmissionDelivery();
      return p.showPartner() +  p.showPartnerNotifications(d);
    }
  }


  //------------------------------------------------------------------------------------

  /**
   * registers an acquisition transaction
   * @param partnerKey partner's key
   * @param productKey product's key
   * @param price product's price per unit
   * @param amount product's amount
   * @throws ProductNotRegisteredException
   * @throws UnknownPartnerException
   */

  public void registerAcquisitionTransaction(String partnerKey, String productKey, double price, int amount) throws ProductNotRegisteredException, UnknownPartnerException {
    Partner partner = _partners.get(partnerKey.toUpperCase());
    if (partner == null) {
      throw new UnknownPartnerException(partnerKey);
    }

    Product p = _products.get(productKey);

    if (p == null) {
      throw new ProductNotRegisteredException(productKey);
    }

    createNotification(productKey, price);

    addBatch(new Batch(productKey, partnerKey, amount, price));
    partner.setAcquisitionsByWarehouseValue(price * amount);

    updateProductMaxPriceAndStock(p, price, amount);
    // diminuir o preco pois foi feita uma compra
    updateBalance(-price, amount);
    Acquisition a = new Acquisition(_transactionID, _date, partner, _products.get(productKey), amount, price * amount);
    _transactionHistory.put(_transactionID , a);
    partner.addAcquisition(a);
    _transactionID++;
  }

  /**
   * Registers an acquistion transaction of a new complex product
   * @param partnerKey partner's key
   * @param productKey product's key
   * @param price product's price per unit
   * @param amount product's amount
   * @param alpha Complex product price multiplier
   * @param recipeString Recipe's components and respective quantities
   */

  public void AcquisitionTransactionComplexProduct(String partnerKey, String productKey, double price, int amount, double alpha, String recipeString) {
    Partner partner = _partners.get(partnerKey.toUpperCase());

    // addComplexProduct returns true if all the components in the given recipe have been registered
    // in the warehouse, otherwise returns false and no transaction is registered
    boolean productAdded = addComplexProduct(productKey, price, amount, alpha, recipeString);
    if (!productAdded) {
      return; 
    }

    createNotification(productKey, price);
    Product product = _products.get(productKey);

    addBatch(new Batch(productKey, partnerKey, amount, price));
    partner.setAcquisitionsByWarehouseValue(price * amount);

    // diminuir o preco pois foi feita uma compra
    updateBalance(-price, amount);
    Acquisition a = new Acquisition(_transactionID, _date, partner, product, amount, price * amount);
    _transactionHistory.put(_transactionID , a);
    partner.addAcquisition(a);
    _transactionID++;
  }

  /**
   * Registers an acquistion transaction of a new simple product
   * @param partnerKey partner's key
   * @param productKey product's key
   * @param price product's price per unit
   * @param amount product's amount
   */

  public void AcquisitionTransactionSimpleProduct(String partnerKey, String productKey, double price, int amount) {
    Partner partner = _partners.get(partnerKey.toUpperCase());
    addProduct(productKey, price, amount);
    createNotification(productKey, price);
    
    Product product = _products.get(productKey);

    addBatch(new Batch(productKey, partnerKey, amount, price));
    partner.setAcquisitionsByWarehouseValue(price * amount);

    // diminuir o preco pois foi feita uma compra
    updateBalance(-price, amount);
    Acquisition a = new Acquisition(_transactionID, _date, partner, product, amount, price * amount);
    _transactionHistory.put(_transactionID , a);
    partner.addAcquisition(a);
    _transactionID++;
  }

  /**
   * Registers a new notification for a certain product
   * @param productKey product's key
   * @param price product's price
   */

  public void createNotification(String productKey, double price) {
    Batch b = getLowestPriceBatch(productKey);
    Product p = _products.get(productKey);

    if (b != null && b.getPricePerUnit() > price) {
      notifyAllPartners(new Bargain(p, price));
    }
    else if (p != null && p.getStock() == 0) {
      notifyAllPartners(new New(p, price));
    }
  }

  /**
   * notifies partners interested in a certain product given a notification
   * @param n product notification
   */

  public void notifyAllPartners(Notification n) {
    for (Map.Entry<String, Partner> entry : _partners.entrySet()) {
      entry.getValue().addNotification(n);
    }
  }

  //------------------------------------------------------------------------------------


  /**
   * registers a sale transaction
   * @param partnerKey partner's key
   * @param deadline date limit
   * @param productKey product's key
   * @param amount product's amount
   * @throws UnknownPartnerException
   * @throws ProductNotRegisteredException
   * @throws UnavailableProductExceptionC
   */

  public void registerSaleTransaction(String partnerKey, int deadline, String productKey, int amount) throws UnknownPartnerException, ProductNotRegisteredException, UnavailableProductExceptionC {
    Partner partner = _partners.get(partnerKey.toUpperCase());
    if (partner == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    Product product = _products.get(productKey);
    if (product == null) {
      throw new ProductNotRegisteredException(productKey);
    }

    double price = 0;
    // se nao existir stock do produto original suficiente vamos tentar construir
    if (product.getStock() < amount) {
      Recipe recipe = product.getRecipe();
      if (recipe != null) {
        price = buildComplexProduct(partner, productKey, deadline, recipe, amount);
      }
      else {
        // nao eh um produto derivado logo nao pode ser construido
        throw new UnavailableProductExceptionC(productKey, amount, product.getStock());
      }
    }
    else {
      price += processSale(amount, productKey);
    }

    Sale s = new Sale(_transactionID, -1, partner, product, amount, price, deadline);
    _transactionHistory.put(_transactionID, s);
    partner.addSale(s);
    _transactionID++;
    partner.setSalesByWarehouseValue(price);
  }

  /**
   * gets the lowest price batch of a certain product available in the warehouse
   * @param productKey product's key
   * @return the lowest price batch of the given product
   */

  public Batch getLowestPriceBatch(String productKey) {
    Batch lowest = null;
    Iterator<Batch> iterator = _batches.iterator();
    while(iterator.hasNext()) {
      Batch b = iterator.next();
      if (productKey.equals(b.getProductKey())) {
        if (lowest == null) {
          lowest = b;
        }
        else {
          if (b.getPricePerUnit() < lowest.getPricePerUnit()) {
            lowest = b;
          }
        }
      }
    }
    return lowest;
  }


  /**
   * builds a complex product when the partner's purchase requires 
   * more quantity than the product's available stock in the warehouse
   * @param partner partner
   * @param productKey product's key
   * @param deadline date limit
   * @param recipe product's recipe
   * @param amount product's desired amount
   * @return the price of the built product
   * @throws UnavailableProductExceptionC
   */

  public double buildComplexProduct(Partner partner, String productKey, int deadline, Recipe recipe, int amount) throws UnavailableProductExceptionC {
    double price = 0;
    // passar pelos componentes da receita e verificar se eh possivel construir o numero de produtos derivados desejados
    Product product = _products.get(productKey);
    verifyRecipeComponentsExistence(productKey, recipe, amount - product.getStock());

    // ja verificado que eh possivel construir os produtos derivados iremos proceder a sua construcao
    price += processSale(amount, productKey);
    return price;
  }

  /**
   * Verifies if all components in a certain complex product exist before building it
   * Recursion is called if the product that is being built is made of other complex products that requiring building
   * @param productKey product's key
   * @param recipe product's recipe
   * @param amount product's amount
   * @throws UnavailableProductExceptionC
   */
  public void verifyRecipeComponentsExistence(String productKey, Recipe recipe, int amount) throws UnavailableProductExceptionC {
    for (int i = 0; i < recipe.getComponents().length; i++) {
      String component = recipe.getComponent(i);
      int componentAmount = recipe.getAmount(i);
      Product product = _products.get(component);
      if (product.getStock() < componentAmount * amount) {
        if (product.getRecipe() == null) {
          // nao eh possivel construir o componente por ser um produto simples
          throw new UnavailableProductExceptionC(component, componentAmount * amount, product.getStock());
        }
        else {
          // utiliza-se recursao para verificar se eh possivel construir o componente em falta
          verifyRecipeComponentsExistence(component, product.getRecipe(), (componentAmount * amount) - product.getStock());
        }
      }
    }
  }
  
  /**
   * process a sale given the product's requested amount
   * @param amount product's amount
   * @param productKey product's key
   * @return the price of the sale
   */

  public double processSale(int amount, String productKey) {
    Product product = _products.get(productKey);
    double price = 0;
    double buildPrice = 0;
    // enquanto ainda nao foi satisfeita a quantidade de produto desejada
    while (amount > 0) {
      Batch lowestPriceBatch = getLowestPriceBatch(productKey);
      // quando ja foram usados todos os produtos deste tipo de todos os batches (quantiade disponivel = 0)
      if (lowestPriceBatch == null) {
        // nao existe quantidade de produto disponivel no warehouse, logo vamos proceder ah construcao
        Recipe recipe = product.getRecipe();
        for (int i = 0; i < recipe.getComponents().length; i++) {
          // chamado recursivamente o processSale para construir todos os componentes em falta
          // o preco eh multiplicado constantemente pelo agravamento do respetivo produto derivado
          buildPrice += processSale(recipe.getAmount(i) * amount, recipe.getComponent(i)) * (1 + recipe.getAlpha());
        }
        double pricePerUnitAfterAgregation = buildPrice / amount;
        if (pricePerUnitAfterAgregation > product.getMaxPrice()) {
          product.setMaxPrice(pricePerUnitAfterAgregation);
        }
        // o produto ja foi totalmente processado e ja temos quantidade suficiente dele logo saimos do loop
        break;
      }
      // caso em que o numero de unidades do lote eh menor ou igual ao do produto desejado
      if (lowestPriceBatch.getBatchUnits() <= amount) {
        // update da quantidade de produto que ainda eh necessario
        amount -= lowestPriceBatch.getBatchUnits();
        // update da quantidade de produto global ainda existente
        product.updateStock(-lowestPriceBatch.getBatchUnits());
        price += lowestPriceBatch.getBatchUnits() * lowestPriceBatch.getPricePerUnit(); 
        // o lote passa a estar vazio logo eh removido
        _batches.remove(lowestPriceBatch);
      }
      // caso em que o numero de unidades do lote eh superior ao do produto desejado
      else {
        price += amount * lowestPriceBatch.getPricePerUnit(); 
        product.updateStock(-amount);
        // update da quantidade de unidades do lote
        lowestPriceBatch.updateUnits(amount);
        amount = 0;
      }
    }
    return price + buildPrice;
  }

  /**
   * shows a certain transaction given its key
   * @param transactionKey transaction's key
   * @return a string that contains a certain transaction given its key
   * @throws UnknownTransactionException
   */

  public String showTransaction(int transactionKey) throws UnknownTransactionException {
    Transaction t = _transactionHistory.get(transactionKey);
    if (t != null) {
      if (t.getPayDay() < 0) {
        Sale s = (Sale) t;
        s.setAddedTaxPrice(calculateSaleTransaction(s));
      }
      return t.showTransaction();
    }
    else {
      throw new UnknownTransactionException(transactionKey);
    }
  }

  //------------------------------------------------------------------------------------

  /**
   * registers a breakdown transaction
   * @param partnerKey partner's key
   * @param productKey product's key
   * @param amount product's amount
   * @throws UnknownPartnerException
   * @throws ProductNotRegisteredException
   * @throws UnavailableProductExceptionC
   */

  public void registerBreakdownTransaction(String partnerKey, String productKey, int amount) throws UnknownPartnerException, ProductNotRegisteredException, UnavailableProductExceptionC {
    Partner partner = _partners.get(partnerKey.toUpperCase());
    if (partner == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    Product product = _products.get(productKey);
    if (product == null) {
      throw new ProductNotRegisteredException(productKey);
    }

    Recipe recipe = product.getRecipe();
    // se nao existir receita eh um produto simples logo nao pode desagregar
    if (recipe == null) {
      return;
    }

    // se nao existir quantidade suficiente do produto a desagregar
    if (product.getStock() < amount) {
      throw new UnavailableProductExceptionC(productKey, amount, product.getStock());
    }

    // processa a venda do produto complexo a desagregar e obtem o preco
    double salesPrice = processSale(amount, productKey);

    double acquisitionsPrice = 0;

    // eh criada a string da receita que sera exibida quando uma transacao de desagregacao for mostrada
    String recipeString = "";
    for (int i = 0; i < recipe.getComponents().length; i++) {
      String component = recipe.getComponent(i);
      // pricePerUnit sera o preco maximo do produto caso nao exista nenhuma quantidade desse produto disponivel
      // ou no caso de existir quantidade disponivel, sera igual ao preco do lote com preco mais baixo
      // o stock do produto e updated
      double pricePerUnit = buyComponent(component, amount * recipe.getAmount(i), partner);
      acquisitionsPrice += pricePerUnit * amount * recipe.getAmount(i);
      recipeString += component + ":" + amount * recipe.getAmount(i) + ":" 
      + Math.round(pricePerUnit * amount * recipe.getAmount(i)) + "#";
    }
    int index = recipeString.length() - 1;
    recipeString = recipeString.substring(0, index);

    // diferenca entre o preco da venda do produto complexo e o preco da compra dos seus subcomponentes
    double deltaPrice = salesPrice - acquisitionsPrice;
    Breakdown b;
    if (deltaPrice > 0) {
      // o entreposto ficou a perder dinheiro porque tinha um objeto que valia mais que os seus componentes juntos
      updateBalance(deltaPrice, 1);
      partner.updatePoints(_date, _date, deltaPrice);
      b = new Breakdown(_transactionID, _date, partner, product, amount, deltaPrice, deltaPrice, recipeString);
    }
    else {
      // o entreposto ficou neutro ou a beneficiar com a desagregacao porque ficou com componentes mais valiosos que o produto complexo
      b = new Breakdown(_transactionID, _date, partner, product, amount, 0, deltaPrice, recipeString);
    }
    _transactionHistory.put(_transactionID, b);
    partner.addBreakdown(b);
    _transactionID++;
  }

  /**
   * receives a product's key and its amount, registering a new batch
   * @param productKey product's key
   * @param amount product's amount
   * @param partner partner
   * @return price per product unit
   */

  public double buyComponent(String productKey, int amount, Partner partner) {
    Product product = _products.get(productKey);
    Batch batch = getLowestPriceBatch(product.getProductKey());
    double price;
    if (batch == null) {
      price = product.getMaxPrice();
    }
    else {
      price = batch.getPricePerUnit();
    }

    product.updateStock(amount);
    // adiciona um novo batch ao warehouse do subcomponente do produto complexo a desagregar
    _batches.add(new Batch(product.getProductKey(), partner.getKey(), amount, price));

    return price;
  }

  //------------------------------------------------------------------------------------

  /**
   * toggles a product's notifications in a specific partner
   * @param partnerKey partner's key
   * @param productKey product's key
   * @throws UnknownPartnerException
   * @throws ProductNotRegisteredException
   */

  public void toggleProductNotifications(String partnerKey, String productKey) throws UnknownPartnerException, ProductNotRegisteredException {
    Partner partner = _partners.get(partnerKey.toUpperCase());
    Product product = _products.get(productKey);
    if (partner == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    else if(product == null) {
      throw new ProductNotRegisteredException(productKey);
    }
    partner.toggleProductNotifications(productKey);
  }

  //------------------------------------------------------------------------------------

  /**
   * shows all acquisitions with a certain partner
   * @param partnerKey partner's key
   * @return a string that contains all acquisitions with a certain partner
   * @throws UnknownPartnerException
   */

  public String showAcquisitionsWithPartner(String partnerKey) throws UnknownPartnerException {
    Partner partner = _partners.get(partnerKey.toUpperCase());
    if (partner == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    return partner.showAcquisitionsWithPartner();
  }

  /**
   * shows all sales and breakdowns with a certain partner
   * @param partnerKey partner's key
   * @return a string that contains all sales and breakdowns with a certain partner
   * @throws UnknownPartnerException
   */

  public String showSalesAndBreakdownsWithPartner(String partnerKey) throws UnknownPartnerException {
    Partner partner = _partners.get(partnerKey.toUpperCase());
    if (partner == null) {
      throw new UnknownPartnerException(partnerKey);
    }
    return partner.showSalesAndBreakdownsWithPartner(_date);
  }


  //------------------------------------------------------------------------------------

  /**
   * updates warehouse balance
   * @param price a product's price (positive if sold and negative if bought)
   * @param amount a product's amount
   */

  public void updateBalance(double price, int amount) {
    _availableBalance += price * amount;
  }


  /**
   * Shows the current date.
   * @return the current date.
   */

  public int showDate() {
    return _date;
  }

  /**
   * Advances the date a desired number of days.
   * @param days number of days to advance
   * @throws InvalidDateExceptionC
   */

  public void advanceDate(int days) throws InvalidDateExceptionC {
    if (days > 0) {
      _date += days;
    }
    else {
      throw new InvalidDateExceptionC(days);
    }
  }

  //------------------------------------------------------------------------------------

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  void importFile(String txtfile) throws IOException, BadEntryException, DuplicatePartnerException /* FIXME maybe other exceptions */ {
    //FIXME implement method
    try (BufferedReader reader = new BufferedReader(new FileReader(txtfile))) {
      String textLine;

      while ((textLine = reader.readLine()) != null) {
        String[] inputs = textLine.split("\\|");

        if (inputs[0].equals("PARTNER")) {
          registerPartner(inputs[1], inputs[2], inputs[3]);
        }
        else if (inputs[0].equals("BATCH_S")) {
          _batches.add(new Batch(inputs[1], inputs[2], Integer.parseInt(inputs[4]), Math.round(Double.parseDouble(inputs[3]))));
          addProduct(inputs[1], Math.round(Double.parseDouble(inputs[3])), Integer.parseInt(inputs[4]));
        }
        else if (inputs[0].equals("BATCH_M")) {
          _batches.add(new Batch(inputs[1], inputs[2], Integer.parseInt(inputs[4]), Math.round(Double.parseDouble(inputs[3]))));
          addComplexProduct(inputs[1], Math.round(Double.parseDouble(inputs[3])), Integer.parseInt(inputs[4]), Double.parseDouble(inputs[5]), inputs[6]);
        }
        else {
          throw new BadEntryException(inputs[0]);
        }
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch (DuplicatePartnerException e) {
      e.printStackTrace();
    } 
    catch (BadEntryException e) {
      e.printStackTrace();
    }
  }
}
