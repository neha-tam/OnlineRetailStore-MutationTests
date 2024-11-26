package org.example;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustPortalDAO_JDBC implements CustomerPortalDAO{

    Connection dbConnection;
    DatabaseHelper dbHelper;

    public CustPortalDAO_JDBC(Connection dbconn, DatabaseHelper databaseHelper)
	{
		dbConnection = dbconn;
        dbHelper = databaseHelper;
	}

    public void OrderItemsByPrice()
    {
        String sql;

        try{
            List<Object> args = new ArrayList<Object>();
            sql = "select count(*) from Inventory";   //checking the number of entries in the Inventory, if there are 0 rows, display a message to the customer
            ResultSet rs = dbHelper.executeQuery(sql,args);    
            
            rs.next();
            if(rs.getInt(1) == 0)
            {
                System.out.println("No items currently available.Sorry!");
            }
			else
            {
                sql = "select productID, productName, price, quantity from Inventory order by productName,price";
			    rs = dbHelper.executeQuery(sql,args);
    
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                System.out.println("ProductID"+"\t"+"Product"+"\t"+"Price"+"\t"+"Quantity");
                while (rs.next())   //printing out the inventory items in the order of price as in the result set
                {
                    for (int i = 1; i <= columnsNumber; i++)
                    {
                        if (i > 1)
                        {
                            System.out.print("\t");
                        }
                        if(i == 2)
                        {
                            System.out.print("\t");
                        }
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue);
                    }
                    System.out.println("");
                }
            }
            System.out.println("");
            
            if(!isTestEnvironment())
            {
                System.out.println("Press enter to continue.\n");
                inputclass.in.nextLine();
                inputclass.in.nextLine();
            }

            
		}
        catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
        
    }

    public void checkInventory()
    {
        String sql;

        try{
            sql = "select count(*) from Inventory";    //checking the number of entries in the Inventory, if there are 0 rows, display a message to the customer
            List<Object> args = new ArrayList<Object>();
            ResultSet rs = dbHelper.executeQuery(sql,args);

            rs.next();
            if(rs.getInt(1) == 0)
            {
                System.out.println("No items currently available.Sorry!");
            }
			else
            {
                sql = "select productID, productName, price, quantity from Inventory";
			    rs = dbHelper.executeQuery(sql,args);

			    ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                System.out.println("Inventory");
                System.out.println("ProductID"+"\t"+"Product"+"\t"+"Price"+"\t"+"Quantity");   //displaying the Inventory
                while (rs.next())
                {
                    for (int i = 1; i <= columnsNumber; i++)
                    {
                        if (i > 1)
                        {
                            System.out.print("\t");
                        }
                        if(i == 2)
                        {
                            System.out.print("\t");
                        }
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue);
                    }
                    System.out.println("");

                }
                
            }

            if(!isTestEnvironment()) {
                System.out.println("Press enter to continue.");
                inputclass.in.nextLine();
                inputclass.in.nextLine();   //still testing
            }

            
		}
        catch (SQLException ex)
        {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public void buyItems(Integer transNo, Customer cust,Integer transID, Integer prodID, Integer quantity)
    {

        String sql1, sql2, sql3, sql = null;

        try
        {

            Integer quan;

            sql = "SELECT quantity FROM Inventory WHERE productID = (?)";
            List<Object> args = new ArrayList<>();
            args.add(prodID);
            ResultSet rs = dbHelper.executeQuery(sql,args);
            if(rs.next())
            {
                quan = rs.getInt("quantity");
                if(quantity>quan || quantity < 1)
                {
                    System.out.println("Please check the inventory below and buy a valid number of products.");
                    checkInventory();
                }
                else
                {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String strDate = currentDate.format(formatter);


                    // sql statements
                    sql1 = "INSERT INTO Transaction (transactionNo, transactionID, customerID, dateOfPurchase, productID, quantityBought)VALUES (?, ?, ?, ?, ?, ?);";
                    sql2 = "DELETE FROM Product WHERE productID IN (SELECT productID FROM Inventory WHERE productID = (?)) LIMIT 1;";
                    sql3 = "UPDATE Inventory SET quantity = quantity - 1 WHERE quantity > 0 and productID = (?);";

                    List<Object> args1 = new ArrayList<Object>();
                    args1.add(transNo);
                    args1.add(transID);
                    args1.add(cust.getCustomerID());
                    args1.add(strDate);
                    args1.add(prodID);
                    args1.add(quantity);

                    dbHelper.executeUpdate(sql1,args1);

                    List<Object> args2 = new ArrayList<Object>();
                    args2.add(prodID);

                    List<Object> args3 = new ArrayList<Object>();
                    args3.add(prodID);

                    for(int i=0; i<quantity;i++)    //check whether the quantity required is available
                    {
                        // executing the statements
                        dbHelper.executeUpdate(sql2,args2);
//                        dbConnection.commit();
                        dbHelper.executeUpdate(sql3,args3);


                    }

                    if(!isTestEnvironment())
                    {
                        System.out.println("Press enter to continue.\n");
                        inputclass.in.nextLine();
                        inputclass.in.nextLine();
                    }


                    System.out.println("Item successfully bought!\n");

                }

            }
            else
            {
                System.out.println("Item not in stock. Please try again later!");
            }
        }
        catch (SQLException ex)
        {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        catch(InputMismatchException i)
        {
            System.out.println("Invalid input.");
        }


    }



public Integer getNewCustomerID(String name) throws SQLException {
    if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("Customer name cannot be null or empty");
    }

    Integer custID = 0;
    String sql, check, sql1;

    try {
        sql = "SELECT MAX(customerID) FROM Customer";
        check = "SELECT COUNT(*) FROM Customer";
        List<Object> args = new ArrayList<>();
        ResultSet rs = dbHelper.executeQuery(check, args);
        rs.next();
        int count = rs.getInt(1);

        if (count == 0) {
            custID = 1;
        } else {
            rs = dbHelper.executeQuery(sql, args);
            rs.next();
            int max = rs.getInt(1);
            custID = max + 1;
        }

        Customer cust = new Customer(custID, name);
        sql1 = "INSERT INTO Customer (customerID, customerName) VALUES (?, ?)";

        List<Object> insertArgs = new ArrayList<>();
        insertArgs.add(cust.getCustomerID());
        insertArgs.add(cust.getCustomerName());

        int result = dbHelper.executeUpdate(sql1, insertArgs);

        dbConnection.commit();

        if (result > 0) {
            System.out.println("Successfully registered!");
        } else {
            System.out.println("Couldn't register :(");
            throw new SQLException("Insert operation failed");
        }
    } catch (SQLException ex) {
        try {
            dbConnection.rollback();
        } catch (SQLException rollbackEx) {
            System.out.println("Rollback failed: " + rollbackEx.getMessage());
        }
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        throw ex;
    }

    return custID;
}




public void generateReceipt(Customer cust,Integer transID)
{
    String sql = null;
    Integer total = 0;

    try
    {
        sql = "select t.dateOfPurchase, t.productID, t.quantityBought, i.price * t.quantityBought as totalCost from Transaction as t join Inventory as i on i.productID = t.productID where t.transactionID = ?";
        List<Object> args = new ArrayList<>();
        args.add(transID);
        ResultSet rs = dbHelper.executeQuery(sql,args);
//        dbConnection.commit();

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        System.out.println("");
        System.out.println("Receipt:\n");
        System.out.println("Transaction ID: "+ transID.toString());
        System.out.println("Customer ID: "+cust.getCustomerID().toString());
        System.out.println("CustomerName: "+cust.getCustomerName());
        
        if(rs.next())   //to get the date from the first line, and print the rest of the attributes 
        {
            String date = rs.getString("dateOfPurchase");
            System.out.println("Date:" +date);
            System.out.println("Product"+"\t\t"+"Quantity"+"\t"+"Price");
            for(int i=2;i<=columnsNumber;i++)
            {
                if (i > 2)
                {
                    System.out.print("\t\t");
                }
                String columnValue = rs.getString(i);
                if(i == 2)
                {
                    System.out.print(getProductName(Integer.parseInt(columnValue)));
                    continue;
                }
                else if(i == 4)
                {
                    total = total + Integer.parseInt(columnValue);
                }
                System.out.print(columnValue);

            }
        }
        else
        {
            System.out.println("");
            System.out.println("No transactions to show.");
            System.out.println("");
        }

        System.out.println("");
    
        while (rs.next())
        {
            for (int i = 2; i <= columnsNumber; i++)
            {
                if (i > 2)
                {
                    System.out.print("\t\t");
                }
                String columnValue = rs.getString(i);
                if(i == 2)
                {
                    System.out.print(getProductName(Integer.parseInt(columnValue)));
                    continue;
                }
                else if(i == 4)
                {
                    total = total + Integer.parseInt(columnValue);
                }
                System.out.print(columnValue);
            }
            System.out.println("");
        }
        
        System.out.println("Total Cost = "+total+"/-\n");
        System.out.println("Thanks for shopping with us!\n");
        System.out.println("");
    }
    catch (SQLException ex)
    {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    
}




public Integer generateTransactionID()
{
    int transID = 0;
    String sql,check;

    try
    {
        sql = "select max(transactionID) from Transaction ";
        check = "select count(*) from Transaction";    //query used to check whether the transaction table is empty or not
        List<Object> args = new ArrayList<>();
		ResultSet rs = dbHelper.executeQuery(check,args);
        rs.next();

        int count = rs.getInt(1);
        if(count == 0)   //this means that the table is empty
        {
            transID = 100;   //first tansacion ID = 100
        }
        else
        {
            rs = dbHelper.executeQuery(sql,args);
            if(rs.next())
            {
                int max = rs.getInt(1);
                transID = max+1;  //the max transaction ID is incrementded by 1 to get a new transaction ID
            }

        }


        
    }
    catch (SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    return transID;
}

public Integer getNextTransNo()
{
    int transNo = 0;
    String sql,check;

    try
    {
        sql = "select max(transactionNo) from Transaction ";
        check = "select count(*) from Transaction";    //query used to check whether the transaction table is empty or not
        List<Object> args = new ArrayList<>();
		ResultSet rs = dbHelper.executeQuery(check,args);
        rs.next();
        int count = rs.getInt(1);
        if(count == 0)   //this means that the table is empty
        {
            transNo = 1;   //first tansacionNo = 1
        }
        else
        {
            rs = dbHelper.executeQuery(sql,args);
            if(rs.next())
            {
                int max = rs.getInt(1);
                transNo = max+1;  //the max transactionNo till now is incremented by 1 to get a next transactionNo
            }

        }
        
    }
    catch (SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    return transNo;
}




    public Customer getCustomer(Integer custID) {
        String sql = "select * from Customer where customerID = ?";
        Customer c = new Customer(custID, null);

        try {
            List<Object> args = new ArrayList<>();
            args.add(custID);  // Remove toString() - let the JDBC driver handle type conversion
            ResultSet rs = dbHelper.executeQuery(sql, args);

            if (rs.next()) {  // Changed while to if since customerID should be unique
                String custName = rs.getString("customerName");  // Use column name instead of index

                c.setCustomerName(custName);
            }
        } catch (SQLException ex) {
            // Log the error and possibly rethrow or handle appropriately
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return c;
    }

public String getProductName(Integer prodID)
{
    String sql = null;

    try
    {
        sql = "select productName from Inventory where productID = ?";
        List<Object> args = new ArrayList<>();
        args.add(prodID.toString());
        ResultSet rs = dbHelper.executeQuery(sql,args);

        if(rs.next())
        {
            return rs.getString("productName");
        }

    }
    catch (SQLException ex) {
	    // handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
    return null;
}

private boolean isTestEnvironment() {
    return "true".equals(System.getProperty("isTest"));
}

    
}
