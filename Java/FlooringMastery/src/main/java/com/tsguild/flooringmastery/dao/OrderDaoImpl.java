package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Order;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author Morgan Smith
 */
public class OrderDaoImpl implements OrderDao {

    private HashMap<String, HashMap<Integer, Order>> orders;
    private String fileName = "Data/Orders/Orders_12345678.txt";
    private final String DELIMITER = ",";

    public OrderDaoImpl() {
        orders = new HashMap<>();
    }

    @Override
    public void addOrder(String date, Order aOrder) {
        if (orders.get(date) == null) {
            orders.put(date, new HashMap<>());
        }

        orders.get(date).put(aOrder.getOrderNumber(), aOrder);
    }

    @Override
    public Order getOrder(String date, int number) {
        if (orders.get(date) != null) {
            return orders.get(date).get(number);
        }

        return null;
    }

    @Override
    public Collection<Order> getOrdersByDate(String date) {
        if (orders.get(date) != null) {
            return orders.get(date).values();
        }

        return null;
    }

    @Override
    public Set<String> getAllDates() {
        return orders.keySet();
    }

    @Override
    public void updateOrder(String date, Order orderToUpdate) {
        // TODO switch to lambda?
        orders.get(date).put(orderToUpdate.getOrderNumber(), orderToUpdate);
    }

    @Override
    public Order removeOrder(String date, int number) {
        return orders.get(date).remove(number);
    }

    @Override
    public void loadFromFile() throws IOException {

        // try-with-resources -- AutoCloseable
        try (Stream<Path> paths = Files.walk(Paths.get("Data/Orders"))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {

                    int orderNumber = 0;
                    String customerName = "";
                    String state = "";
                    double taxRate = 0;
                    String productType = "";
                    double area = 0;
                    double costSqFt = 0;
                    double laborSqFt = 0;
                    double materialCost = 0;
                    double laborCost = 0;
                    double tax = 0;
                    double total = 0;
                    HashMap<Integer, Order> ordersForYear = new HashMap<>();

                    try {
                        Scanner sc = new Scanner(new BufferedReader(new FileReader(filePath.toString())));

                        if (!sc.hasNextLine()) {
                            filePath.toFile().delete();
                        }

                        while (sc.hasNextLine()) {
                            String orderLine = sc.nextLine();
                            String[] orderProperties = orderLine.split(DELIMITER);

                            if (orderProperties.length != 12) {
                                continue;
                            }

                            customerName = orderProperties[1];
                            if (customerName.contains("/")) {
                                customerName = customerName.replace("/", ",");
//                                customerName = "" + customerName + "";
                            }
                            state = orderProperties[2];
                            if (state.contains("/")) {
                                state = state.replace("/", ",");
//                                state = "" + state + "";
                            }
                            productType = orderProperties[4];
                            if (productType.contains("/")) {
                                productType = productType.replace("/", ",");
//                                productType = "" + productType + "";
                            }

                            try {
                                orderNumber = Integer.parseInt(orderProperties[0]);
                                taxRate = Double.parseDouble(orderProperties[3]);
                                area = Double.parseDouble(orderProperties[5]);
                                costSqFt = Double.parseDouble(orderProperties[6]);
                                laborSqFt = Double.parseDouble(orderProperties[7]);
                                materialCost = Double.parseDouble(orderProperties[8]);
                                laborCost = Double.parseDouble(orderProperties[9]);
                                tax = Double.parseDouble(orderProperties[10]);
                                total = Double.parseDouble(orderProperties[11]);

                            } catch (NumberFormatException e) {
                                Logger.getLogger(OrderDaoImpl.class.getName()).log(Level.SEVERE, null, e);
                                continue;
                            }

                            Order o = new Order(orderNumber, customerName, state, taxRate, productType, area, costSqFt, laborSqFt, materialCost, laborCost, tax, total);
                            ordersForYear.put(orderNumber, o);
                        }

                        sc.close();

                        String tempFilePath = filePath.toString();
                        tempFilePath = tempFilePath.substring(tempFilePath.length() - 12, tempFilePath.length() - 4);
                        String year = tempFilePath.substring(0, 2) + "/" + tempFilePath.substring(2, 4) + "/" + tempFilePath.substring(4);
                        boolean yearValidation = Pattern.matches("\\d{8}", tempFilePath);

                        if (yearValidation) {
                            orders.put(year, ordersForYear);
                        }

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(OrderDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }

    @Override
    public void saveToFile() throws IOException {

        for (String year : orders.keySet()) {
            String yearReplace = year.replace("/", "");
            fileName = fileName.replaceAll("\\d{8}", yearReplace);
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));

            for (Order o : orders.get(year).values()) {

                if (o.getCustomerName().contains(",")) {
                    o.setCustomerName(o.getCustomerName().replace(",", "/"));
//                    o.setCustomerName('"' + o.getCustomerName() + '"');
                }
                if (o.getState().contains(",")) {
                    o.setState(o.getState().replace(",", "/"));
//                    o.setState('"' + o.getState() + '"');
                }
                if (o.getProductType().contains(",")) {
                    o.setProductType(o.getProductType().replace(",", "/"));
//                    o.setProductType('"' + o.getProductType() + '"');

                }

                writer.println(o.getOrderNumber() + DELIMITER
                        + o.getCustomerName() + DELIMITER
                        + o.getState() + DELIMITER
                        + o.getTaxRate() + DELIMITER
                        + o.getProductType() + DELIMITER
                        + o.getArea() + DELIMITER
                        + o.getCostSqFt() + DELIMITER
                        + o.getLaborSqFt() + DELIMITER
                        + o.getMaterialCost() + DELIMITER
                        + o.getLaborCost() + DELIMITER
                        + o.getTax() + DELIMITER
                        + o.getTotal()
                );
            }

            writer.flush();
            writer.close();
        }
    }
}
