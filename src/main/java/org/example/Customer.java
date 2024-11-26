package org.example;

public class Customer {
    private Integer customerID;
    private String customerName;
    
    public Customer(int custID, String custName)
    {
        this.customerID = custID;
        this.customerName = custName;
    }

    public Integer getCustomerID() {return customerID;}
    public String getCustomerName() {return customerName;}

    public void setCustomerID(Integer custID) {this.customerID = custID;}
    public void setCustomerName(String custName) {this.customerName = custName;}
}
