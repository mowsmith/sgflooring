package com.tsguild.flooringmastery.ops;

import com.tsguild.flooringmastery.dao.OrderDao;
import com.tsguild.flooringmastery.dao.ProductDao;
import com.tsguild.flooringmastery.dao.StateTaxDao;
import com.tsguild.flooringmastery.dto.Order;
import com.tsguild.flooringmastery.dto.Product;
import com.tsguild.flooringmastery.dto.StateTax;
import com.tsguild.flooringmastery.ui.ConsoleIO;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author Morgan Smith
 */
public class FlooringController {

    private ConsoleIO console;
    private OrderDao orderDao;
    private ProductDao productDao;
    private StateTaxDao stateTaxDao;
    private String testMode;
    private int nextOrderNumber;

    public FlooringController(ConsoleIO console, OrderDao orderDao, ProductDao productDao, StateTaxDao stateTaxDao) {
        this.console = console;
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.stateTaxDao = stateTaxDao;
    }

    public void run() {
        
        console.print("\n*** THE SOFTWARE GUILD FLOORING CORP ***");
        console.print("***          ORDER MANAGER          ***");

        try {
            orderDao.loadFromFile();
            productDao.loadFromFile();
            stateTaxDao.loadFromFile();
            loadConfFile();
        } catch (IOException ex) {
            console.print("Unable to access file needed for records.");
            console.print("Please contact your system administrator.");
        }

        boolean keepRunning = true;
        while (keepRunning) {

            printMenu();

            int userChoice = console.readInt("> ", 1, 6);

            switch (userChoice) {
                case 1:
                    // display order for certain date
                    displayOrders();
                    break;
                case 2:
                    // add an order
                    addAnOrder();
                    break;
                case 3:
                    // edit an order
                    editAnOrder();
                    break;
                case 4:
                    // remove an order
                    removeOrder();
                    break;
                case 5:
                    // save if not in test mode
                    save();
                    break;
                default:
                    // TODO add warning if quiting without saving? 
                    keepRunning = exit();
                    console.print("\n\nHave a nice day!");
                    break;
            }
        }
    }

    private void printMenu() {

        console.print("\n1. Display Orders");
        console.print("2. Add an Order");
        console.print("3. Edit an Order");
        console.print("4. Remove an Order");
        console.print("5. Save");
        console.print("6. Exit");
    }

    private void displayOrders() {

        // TODO hit enter to view all order dates
        console.print("\n*** DISPLAY ORDERS ***");
        while (true) {
            String date = getDate("What date would you like to display orders for (MM/DD/YYYY)?\n> ");
            if (orderDao.getOrdersByDate(date) != null && orderDao.getOrdersByDate(date).size() >= 1) {
                for (Order o : orderDao.getOrdersByDate(date)) {
                    console.print(o.toString());
                }

                break;
            } else {
                console.print("No orders recorded for " + date + ".");
                datesInRecord(date);
                String choice = getYesNo("\nTry another Date (y/n): ");
                if (choice.equalsIgnoreCase("n")) {
                    break;
                }
            }
        }
    }

    private void addAnOrder() {

        console.print("\n*** ADD AN ORDER ***");

        String date = getDate("Date (MM/DD/YYYY): ");
        String name = getName("Name: ");

        String state = getState("State (AA): ").toUpperCase();
        while (stateTaxDao.getStateTax(state) == null) {
            state = getState("State must be two letter abbreviation.\nState (AA): ").toUpperCase();
        }
        StateTax stateTax = stateTaxDao.getStateTax(state);

        String productType = console.readString("Flooring Material (press Enter to view all options): ").toLowerCase();
        while (true) {

            int matches = 0;
            ArrayList<Product> productsMatching = new ArrayList();

            console.print("");
            for (Product p : productDao.getAllProducts()) {
                if (p.getProductType().toLowerCase().contains(productType)) {
//                    console.print(p.toString());
                    productsMatching.add(p);
                    matches++;
                }
            }

            if (!productType.equals("") && matches > 1) {
                console.print("Materials matching " + productType + ":");
            }

            if (matches > 1) {
                for (Product product : productsMatching) {
                    console.print(product.toString());
                }
            }

            if (matches > 1) {
                productType = console.readString("\nPlease enter full material type to continue: ").toLowerCase();
                for (Product p : productDao.getAllProducts()) {
                    if (p.getProductType().toLowerCase().contains(productType)) {
                        productsMatching = new ArrayList<>();
                        productsMatching.add(p);
                        matches++;
                    }
                }
                if (productDao.getProduct(productType) != null) {
                    break;
                } else if (!productType.equals("") && matches > 1) {
                    console.print("No material found matching: " + productType);
                }
            } else if (matches == 0) {
                for (Product p : productDao.getAllProducts()) {
                    if (p.getProductType().toLowerCase().contains(productType)) {
                        productsMatching = new ArrayList<>();
                        productsMatching.add(p);
                        matches++;
                    }
                }
                if (!productType.equals("") && matches > 1) {
                    console.print("No material found matching: " + productType);
                }
                productType = console.readString("Please enter material type to continue: ").toLowerCase();
            } else if (matches == 1) {
                console.print("Found:\n" + productsMatching.get(0));
                String choice = getYesNo("Confirm (y/n): ");
                if (choice.equalsIgnoreCase("y")) {
                    productType = productsMatching.get(0).getProductType().toLowerCase();
                    break;
                } else {
                    productType = console.readString("Please enter material type to continue: ").toLowerCase();
                }
            }
        }
        Product product = productDao.getProduct(productType);

        // TODO clarify min,max
        double area = console.readDouble("Area in Square Feet: ", 1, Double.MAX_VALUE);

        Order order = new Order(nextOrderNumber, name, stateTax, product, area);
        console.print(order.toString());

        String add = console.readString("\nAdd order to date (y/n): ");
        while (!add.equalsIgnoreCase("y") && !add.equalsIgnoreCase("n")) {
            add = console.readString("y / n: ");
        }

        if (add.equalsIgnoreCase("y")) {
            orderDao.addOrder(date, order);
            console.print("\nOrder added.");
            nextOrderNumber++;
        } else {
            console.print("\nOrder not added.");
        }
    }

    private void editAnOrder() {

        console.print("\n*** EDIT AN ORDER ***");
        String date = "";
        int orderNumberToEdit = 0;
        boolean exitEdit = false;

        while (true) {
            date = getDate("Date (MM/DD/YYYY): ");
            if (orderDao.getOrdersByDate(date) != null && orderDao.getOrdersByDate(date).size() >= 1) {
                for (Order o : orderDao.getOrdersByDate(date)) {
                    console.print(o.toString());
                }

                break;
            } else {
                console.print("No orders recorded for " + date + ".");
                datesInRecord(date);
                String choice = getYesNo("Try another Date (y/n): ");
                if (choice.equalsIgnoreCase("n")) {
                    exitEdit = true;
                    break;
                }
            }
        }

        while (!exitEdit) {
            orderNumberToEdit = console.readInt("Order Number: ");
            if (orderDao.getOrder(date, orderNumberToEdit) != null) {
                console.print(orderDao.getOrder(date, orderNumberToEdit).toString());
                String choice = getYesNo("\nEdit record (y/n): ");
                if (choice.equalsIgnoreCase("y")) {

                    Order orderToEdit = orderDao.getOrder(date, orderNumberToEdit);

                    console.print("\nUpdating Order (press Enter to keep original value): ");
                    String orderNumber = console.readString("Order Number [" + orderToEdit.getOrderNumber() +"]: ");
                    if (!orderNumber.equals("")) {
                        try {
                            int orderNumberParsed = Integer.parseInt(orderNumber);
                            ArrayList<Integer> usedOrderNumbers = new ArrayList<>();

                            for (String year : orderDao.getAllDates()) {
                                for (Order order : orderDao.getOrdersByDate(year)) {
                                    usedOrderNumbers.add(order.getOrderNumber());
                                }
                            }

                            while (usedOrderNumbers.contains(orderNumberParsed)) {
                                orderNumberParsed = console.readInt("Order " + orderNumberParsed + " already exists."
                                        + "\nPlease enter a different number: ");
                            }

                            orderToEdit.setOrderNumber(orderNumberParsed);
                        } catch (NumberFormatException e) {
                            // idk?
                            Logger.getLogger(FlooringController.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }

                    String customerName = console.readString("Customer Name[" + orderToEdit.getCustomerName()+"]: ");
                    if (!customerName.equals("")) {
                        orderToEdit.setCustomerName(customerName);
                    }
                    String state = console.readString("State[" + orderToEdit.getState()+"]: ");
                    if (!state.equals("")) {
                        orderToEdit.setState(state);
                    }

                    String taxRate = getDouble("Tax Rate[" + orderToEdit.getTaxRate()+"]: ");
                    if (!taxRate.equals("")) {
                        try {
                            orderToEdit.setTaxRate(Double.parseDouble(taxRate));
                        } catch (NumberFormatException e) {
                            Logger.getLogger(FlooringController.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }

                    String productType = console.readString("Product Type[" + orderToEdit.getProductType()+"]: ");
                    if (!productType.equals("")) {
                        orderToEdit.setProductType(productType);
                    }

                    String area = getDouble("Area in Square Feet[" + orderToEdit.getArea()+"]: ");
                    if (!area.equals("")) {
                        try {
                            orderToEdit.setArea(Double.parseDouble(area));
                        } catch (NumberFormatException e) {
                            Logger.getLogger(FlooringController.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    String costSqFt = getDouble("Product Cost per Square Foot[" + orderToEdit.getCostSqFt()+"]: ");
                    if (!costSqFt.equals("")) {
                        try {
                            orderToEdit.setCostSqFt(Double.parseDouble(costSqFt));
                        } catch (NumberFormatException e) {
                            Logger.getLogger(FlooringController.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    String laborSqFt = getDouble("Labor Cost per Square Foot[" + orderToEdit.getLaborSqFt()+"]: ");
                    if (!laborSqFt.equals("")) {
                        try {
                            orderToEdit.setLaborSqFt(Double.parseDouble(laborSqFt));
                        } catch (NumberFormatException e) {
                            Logger.getLogger(FlooringController.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    String materialCost = getDouble("Totatl Material Cost[" + orderToEdit.getMaterialCost()+"]: ");
                    if (!materialCost.equals("")) {
                        try {
                            orderToEdit.setMaterialCost(Double.parseDouble(materialCost));
                        } catch (NumberFormatException e) {
                            Logger.getLogger(FlooringController.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    String laborCost = getDouble("Total Labor Cost[" + orderToEdit.getLaborCost()+"]: ");
                    if (!laborCost.equals("")) {
                        try {
                            orderToEdit.setLaborCost(Double.parseDouble(laborCost));
                        } catch (NumberFormatException e) {
                            Logger.getLogger(FlooringController.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    String tax = getDouble("Tax[" + orderToEdit.getTax()+"]: ");
                    if (!tax.equals("")) {
                        try {
                            orderToEdit.setTax(Double.parseDouble(tax));
                        } catch (NumberFormatException e) {
                            Logger.getLogger(FlooringController.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    String total = getDouble("Total[" + orderToEdit.getTotal()+"]: ");
                    if (!total.equals("")) {
                        try {
                            orderToEdit.setTotal(Double.parseDouble(total));
                        } catch (NumberFormatException e) {
                            Logger.getLogger(FlooringController.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }

                    // move to new date? 
                    String newDate = console.readString("Date (MM/DD/YYYY)[" + date +"]: ");
                    if (!validateDate(newDate)) {
                        newDate = date;
                    }
                    orderDao.removeOrder(date, orderNumberToEdit);
                    orderDao.addOrder(newDate, orderToEdit);

                    console.print("\nOrder " + orderNumberToEdit + " has been updated:");
                    console.print(orderToEdit.toString());
                    break;
                } else {
                    break;
                }
            } else {
                console.print("Order Number did not many any in record.");
                String choice = getYesNo("Try another Order Number (y/n): ");
                if (choice.equalsIgnoreCase("n")) {
                    break;
                }
            }
        }
    }

    private void removeOrder() {

        console.print("\n*** REMOVE AN ORDER ***");
        String date = "";
        boolean exitRemove = false;

        while (true) {
            date = getDate("Date (MM/DD/YYYY): ");
            if (orderDao.getOrdersByDate(date) != null && orderDao.getOrdersByDate(date).size() >= 1) {
                for (Order o : orderDao.getOrdersByDate(date)) {
                    console.print(o.toString());
                }

                break;
            } else {
                console.print("No orders recorded for " + date + ".");
                datesInRecord(date);
                String choice = getYesNo("Try another Date (y/n): ");
                if (choice.equalsIgnoreCase("n")) {
                    exitRemove = true;
                    break;
                }
            }
        }

        while (!exitRemove) {
            int orderNumber = console.readInt("Order Number: ");
            if (orderDao.getOrder(date, orderNumber) != null) {
                console.print(orderDao.getOrder(date, orderNumber).toString());
                String choice = getYesNo("\nRemove record (y/n): ");
                if (choice.equalsIgnoreCase("y")) {
                    orderDao.removeOrder(date, orderNumber);
                    console.print("Order " + orderNumber + " has been removed.");
                    break;
                } else {
                    break;
                }
            } else {
                console.print("Order Number did not many any in record.");
                String choice = getYesNo("Try another Order Number (y/n): ");
                if (choice.equalsIgnoreCase("n")) {
                    break;
                }
            }
        }
    }

    private void save() {
        if (testMode.equals("test")) {
            console.print("Saving is not available in test mode.");
        } else {
            try {
                orderDao.saveToFile();
                saveConfToFile();

                console.print("\nOrders saved to file.");
            } catch (IOException ex) {
                console.print("\nUnable to save to file.");
            }
        }
    }

    private boolean exit() {
        return false;
    }

    private void loadConfFile() throws IOException {
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader("Data/Config.txt")));

            while (sc.hasNextLine()) {
                String confLine = sc.nextLine();
                String[] confProperties = confLine.split(",");

                if (confProperties.length != 2) {
                    continue;
                }

                testMode = confProperties[0];

                try {
                    nextOrderNumber = Integer.parseInt(confProperties[1]);
                } catch (NumberFormatException e) {
                    Logger.getLogger(FlooringController.class.getName()).log(Level.SEVERE, null, e);
                    continue;
                }
            }

            sc.close();

        } catch (FileNotFoundException ex) {
            new FileWriter("Data/Config.txt");
        }
    }

    private void saveConfToFile() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("Data/Config.txt"));
        writer.println(testMode + ","
                + nextOrderNumber);

        writer.flush();
        writer.close();
    }

    private String getDate(String prompt) {

        String date = "";
        boolean dateValidation = false;
        do {
            date = console.readString(prompt);
            dateValidation = validateDate(date);
        } while (!dateValidation);

        return date;
    }

    private String getName(String prompt) {

        // TODO ask about name validation
        String name = "";
        name = console.readString(prompt);

        return name;
    }

    private String getState(String prompt) {

        String state = "";
        boolean stateValidation = false;
        state = console.readString(prompt);
        stateValidation = Pattern.matches("[a-zA-Z]{2}", state);

        while (!stateValidation) {
            state = console.readString("State must be two letter abbreviation.\nState (AA): ");
            stateValidation = Pattern.matches("[a-zA-Z]{2}", state);
        }

        return state;
    }

    private String getYesNo(String prompt) {

        String answer = "";
        boolean answerValidation = false;
        answer = console.readString(prompt);
        answerValidation = Pattern.matches("[yYnN]", answer);

        while (!answerValidation) {
            answer = console.readString("y / n: ");
            answerValidation = Pattern.matches("[yYnN]", answer);
        }

        return answer;
    }

    private String getDouble(String prompt) {

        String answer = "";
        boolean answerValidation = false;
        answer = console.readString(prompt);
        answerValidation = Pattern.matches("((\\d+).(\\d+))|(\\d+)", answer);

        if (!answerValidation) {
            answer = "";
        }

        return answer;
    }

    private void datesInRecord(String date) {

        // TODO date passed in to potentially check for dates in range
        // date must be split into ints
        console.print("\nDates in record:");
        for (String year : orderDao.getAllDates()) {
            if (orderDao.getOrdersByDate(year) != null && orderDao.getOrdersByDate(year).size() >= 1) {
                console.print(year);
            }
        }
    }

    private boolean validateDate(String date) {
        boolean dateValidation;
        String split[] = date.split("/");
        String tester = "";
        if (split.length == 3) {
            tester = split[1] + split[0] + split[2];
        }
        // regex pattern matches valid dates including leap years and days in the month
        // it's very long, and i'm not sure exactly how it works, but i've tested it, and
        // it does work. it matches dates in the format DDMMYYYY so some conversion is needed
        // to work with the day dates are handled in this program
        dateValidation = Pattern.matches("^(?:(?:(?:0[1-9]|1\\d|2[0-8])(?:0[1-9]|1[0-2])|(?:29|30)(?:0[13-9]|1[0-2])|31(?:0[13578]|1[02]))[1-9]\\d{3}|2902(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00))$", tester);
        
        return dateValidation;
    }
}
