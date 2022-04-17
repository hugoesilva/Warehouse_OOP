package ggc.app.lookups;

/**
 * Menu entries.
 */
interface Label {

  /** Menu title. */
  String TITLE = "Consultas";

  /** List trips from location. */
  String PRODUCTS_BY_PARTNER = "Produtos comprados por parceiro";
  //DONE//

  /** List trips to location. */
  String PRODUCTS_UNDER_PRICE = "Produtos com Preço Abaixo de Limite";
  //USED//

  /** List of available trips from location. */
  String PAID_BY_PARTNER = "Facturas pagas por parceiro";
  //USED//

  /** List of available trips to location. */
  String PAID_LATE = "Facturas Pagas com Atraso";
  //DONE//

  /** List trips from location. */
  String PARTNERS_BY_PRODUCT = "Parceiros que Compram um Produto";

}