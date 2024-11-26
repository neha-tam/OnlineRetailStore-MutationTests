package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.Exception;
import java.text.SimpleDateFormat;

public class DAO_Main {
    
    public static DAO_Factory daoFactory;
	public static void main(String[] args) {
		try{
			daoFactory = new DAO_Factory();
            Integer choice = 1;  //for the first input (shop or exit)
            Boolean first = true;
            while(true)
            {
                System.out.println("");
                if(!first)
                {
                    System.out.println("Do you want to 1)Enter/Stay in the store or 2)Exit.\nPlease enter the option number.");
                    choice = inputclass.in.nextInt();
                }
                if(choice == 1)
                {
                    System.out.println("Welcome to our store!");
                    customerInterface();
                    first = false;
                }
                else
                {
                    System.out.println("Thanks for visiting! :)");
                    break;
                }

            }


		}
        catch(InputMismatchException m)
        {
            System.out.println("Exiting the store.");
        }
        catch(Exception e){
				//Handle errors for Class.forName
				e.printStackTrace();
		}
	}



    public static void customerInterface()
    {

        try
        {
            daoFactory.activateConnection();
            //CustPortalDAO_JDBC cpdj;
            CustomerPortalDAO cpd = daoFactory.getCustPortalDAO();  // getting a CustomerPortalDAO object from a daoFactory function
            Integer custID = 0;
            Integer transID = cpd.generateTransactionID();
            Integer transNo = cpd.getNextTransNo();
            // getCustPortalDAO() 
            System.out.println("");
            while(true)   //while loop just to ensure that the right input is taken. The loop is broken when a valid input is given.
            {
                System.out.println("Have you been to this store before? (Yes or No)");
                String ans = inputclass.in.next();

                if(ans.equals("Yes") || ans.equals("yes"))
                {
                    System.out.println("Please enter your Customer ID:");
                    custID = inputclass.in.nextInt();
                    if(!(cpd.getCustomer(custID).getCustomerName() == null))
                    {
                        break;
                    }
                    else
                    {
                        System.out.println("Not a registered customer ID. Please try again!");
                    }
                    

                }
                else if(ans.equals("No") || ans.equals("no"))
                {
                    // Generate s = new Generate(Connection dbConnection);
                    System.out.println("Enter your name");
                    String name = inputclass.in.next();
                    custID = cpd.getNewCustomerID(name); //calling the function defined in CustomerPortalDAO_JDBC
                    System.out.println("Your new customer ID: "+custID.toString());  // custID needs to be an Integer to call toString on it
                    System.out.println("(Do keep note of your ID, you'll need it to shop!)");
                    break;
                }
                else
                {
                    System.out.println("Invalid input.Try again!");
                }
            }

            // This is temporary
            Customer customer = new Customer(cpd.getCustomer(custID).getCustomerID(),cpd.getCustomer(custID).getCustomerName());
            System.out.println("");
            System.out.println("Welcome "+customer.getCustomerName()+"!\n");
            

            while(true)
            {
                System.out.print("What would you like to do today? Type one of the following numbers (type in the number):\n1)Check Inventory\n2)Order items by price\n3)Buy products\n4)Generate Receipt\n5)Exit\n");
                String command = inputclass.in.next();
                if(command.equals("1"))
                {
                    cpd.checkInventory();   // called using a CustomerPortalDAO object, defined before this
                }
                else if(command.equals("2"))
                {
                    cpd.OrderItemsByPrice();   // called using a CustomerPortalDAO object, defined before this
                }
                else if(command.equals("3"))
                {
                    System.out.println("Enter the productID and then the quantity you wish to buy");
                    Integer prodID = inputclass.in.nextInt();  //entered product name
                    int quan = inputclass.in.nextInt();  //entered quantity bought
                    cpd.buyItems(transNo,customer, transID, prodID, quan);
                    transNo = transNo + 1;
                }
                else if(command.equals("4"))
                {
                    cpd.generateReceipt(customer,transID);
                    transID = transID +1;
                }
                else if(command.equals("5"))
                {
                    transID = transID +1;
                    break;
                }
                else
                {
                    System.out.println("\nPlease enter a valid option.\n");
                }
            }
            daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT);
            
        }
        catch(InputMismatchException m)
        {
            System.out.println("Wrong values of input.");
        }
        catch(Exception e)
        {
            // do we need to deactivate the connection?
    		daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
            e.printStackTrace();
        }
    }



}

class inputclass
{
    static Scanner in = new Scanner(System.in);
}

