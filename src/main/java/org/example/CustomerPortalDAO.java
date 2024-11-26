package org.example;

import java.sql.SQLException;

public interface CustomerPortalDAO {
    public void checkInventory();
    public void buyItems(Integer transNo, Customer cust,Integer transID, Integer prodID, Integer quantity);
    public void OrderItemsByPrice();
    public void generateReceipt(Customer cust,Integer transID);
    public Customer getCustomer(Integer custID);
    
    // The following functions were moved here
    public Integer getNewCustomerID(String name) throws SQLException;
    public Integer generateTransactionID();
    public Integer getNextTransNo();
    public String getProductName(Integer prodID);

}
