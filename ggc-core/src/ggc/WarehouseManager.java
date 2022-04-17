package ggc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import ggc.exceptions.*;

//FIXME import classes (cannot import from pt.tecnico or ggc.app)

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename = "";

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  //FIXME define other attributes
  //FIXME define constructor(s)
  //FIXME define other methods


  // PRATICAR PARA O TESTE PRATICO



  public void changePartnerName(String partnerKey, String name) throws UnknownPartnerException {
    _warehouse.changePartnerName(partnerKey, name);
  }

  public String showLowestPriceBatch(String productKey) throws ProductNotRegisteredException {
    return _warehouse.showLowestPriceBatch(productKey);
  }

  public String showHighestPriceBatch(String productKey) throws ProductNotRegisteredException, UnavailableProductExceptionC {
    return _warehouse.showHighestPriceBatch(productKey);
  }

  public String showPartnerWithMostPoints() {
    return _warehouse.showPartnerWithMostPoints();
  }

  public void deletePartner(String partnerKey) throws UnknownPartnerException {
    _warehouse.deletePartner(partnerKey);
  }

  public void changePartnerAddress(String partnerKey, String address) throws UnknownPartnerException {
    _warehouse.changePartnerAddress(partnerKey, address);
  }


  public String productsByPartner(String partnerKey) throws UnknownPartnerException {
    return _warehouse.productsByPartner(partnerKey);
  }

  public String showPaidLate(int delay) {
    return _warehouse.showPaidLate(delay);
  }

  public String showPartnersByProduct(String productKey) throws ProductNotRegisteredException {
    return _warehouse.showPartnersByProduct(productKey);
  }





 // ----------------------------------------------------------------------------------------------------


  public double getAvailableBalance() {
    return _warehouse.getAvailableBalance();
  }

  public double getAccountingBalance() {
    return _warehouse.getAccountingBalance();
  }

  public String showAllProducts() {
    return _warehouse.showAllProducts();
  }

  public String showAvailableBatches() {
    return _warehouse.showAvailableBatches();
  }

  public String showBatchesByPartner(String partnerKey) throws UnknownPartnerException {
    return _warehouse.showBatchesByPartner(partnerKey);
  }

  public String showBatchesByProduct(String productKey) throws ProductNotRegisteredException {
    return _warehouse.showBatchesByProduct(productKey);
  }

  public void registerPartner(String key, String name, String address) throws DuplicatePartnerException {
    _warehouse.registerPartner(key, name, address);
  }

  public String showAllPartners() {
    return _warehouse.showAllPartners();
  }

  public String showPartner(String partnerKey) throws UnknownPartnerException {
    return _warehouse.showPartner(partnerKey);
  }

  public int showDate() {
    return _warehouse.showDate();
  }

  public void advanceDate(int days) throws InvalidDateExceptionC {
    _warehouse.advanceDate(days);
  }

  public String showTransaction(int transactionKey) throws UnknownTransactionException {
    return _warehouse.showTransaction(transactionKey);
  }

  public String getFileName() {
    return _filename;
  }

  public void setFileName(String filename) {
    _filename = filename;
  }

  public void registerAcquisitionTransaction(String partnerKey, String productKey, double price, int amount) throws ProductNotRegisteredException, UnknownPartnerException {
    _warehouse.registerAcquisitionTransaction(partnerKey, productKey, price, amount);
  }

  public void AcquisitionTransactionComplexProduct(String partnerKey, String productKey, double price, int amount, double alpha, String recipeString) {
    _warehouse.AcquisitionTransactionComplexProduct(partnerKey, productKey, price, amount, alpha, recipeString);
  }

  public void AcquisitionTransactionSimpleProduct(String partnerKey, String productKey, double price, int amount) {
    _warehouse.AcquisitionTransactionSimpleProduct(partnerKey, productKey, price, amount);
  }

  public void registerSaleTransaction(String partnerKey, int deadline, String productKey, int amount) throws UnknownPartnerException, ProductNotRegisteredException, UnavailableProductExceptionC {
    _warehouse.registerSaleTransaction(partnerKey, deadline, productKey, amount);
  }

  public void registerBreakdownTransaction(String partnerKey, String productKey, int amount) throws UnknownPartnerException, ProductNotRegisteredException, UnavailableProductExceptionC {
    _warehouse.registerBreakdownTransaction(partnerKey, productKey, amount);
  }

  public void receivePayment(int transactionKey) throws UnknownTransactionException {
    _warehouse.receivePayment(transactionKey);
  }

  public String paymentsByPartner(String partnerKey) throws UnknownPartnerException {
    return _warehouse.paymentsByPartner(partnerKey);
  }

  public String showBatchesUnderGivenPrice(double price) {
    return _warehouse.showBatchesUnderGivenPrice(price);
  }

  public String showAcquisitionsWithPartner(String partnerKey) throws UnknownPartnerException {
    return _warehouse.showAcquisitionsWithPartner(partnerKey);
  }

  public String showSalesAndBreakdownsWithPartner(String partnerKey) throws UnknownPartnerException {
    return _warehouse.showSalesAndBreakdownsWithPartner(partnerKey);
  }

  public void toggleProductNotifications(String partnerKey, String productKey) throws UnknownPartnerException, ProductNotRegisteredException {
    _warehouse.toggleProductNotifications(partnerKey, productKey);
  }

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    //FIXME implement serialization method
    if (!_filename.equals("")) {
      ObjectOutputStream out = new ObjectOutputStream(
        new BufferedOutputStream(new FileOutputStream(_filename)));
      out.writeObject(_warehouse);
      out.close();
    }
    else {
      throw new MissingFileAssociationException();
    }
  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    _filename = filename;
    save();
  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException, IOException, ClassNotFoundException, FileNotFoundException {
    //FIXME implement serialization method
    ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
    _warehouse = (Warehouse) in.readObject();
    in.close();
    _filename = filename;
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
	    _warehouse.importFile(textfile);
    } catch (IOException | BadEntryException | DuplicatePartnerException /* FIXME maybe other exceptions */ e) {
	    throw new ImportFileException(textfile);
    }
  }

}
