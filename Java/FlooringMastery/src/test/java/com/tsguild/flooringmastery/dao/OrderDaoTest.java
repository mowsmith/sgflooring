/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Order;
import com.tsguild.flooringmastery.dto.Product;
import com.tsguild.flooringmastery.dto.StateTax;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author apprentice
 */
public class OrderDaoTest {

    OrderDao testDao;

    String[] year = {"06/11/1990", "06/23/1994", "02/13/2014", "01/31/2000"};

    Order[] ordersForTesting = {
        new Order(1, "Morgan", new StateTax("KY", 2), new Product("carpet", 3, 5), 2),
        new Order(2, "Theia", new StateTax("OH", 3), new Product("tile", 4, 6), 8),
        new Order(3, "Cronus", new StateTax("IN", 4), new Product("wood", 7, 22), 9),
        new Order(4, "Priam", new StateTax("MN", 5), new Product("glass", 1, 4), 10)
    };
    Order[] duplicateOrdersForTesting = {
        new Order(1, "Rachel", new StateTax("OI", 6), new Product("bananas", 11, 55), 22),
        new Order(2, "Bear", new StateTax("IO", 7), new Product("pickles", 73, 75), 23),
        new Order(3, "Plato", new StateTax("NM", 9), new Product("diamond", 233, 76), 24),
        new Order(4, "Pontos", new StateTax("MN", 3), new Product("bronze", 38, 11), 25)
    };
    Order[] similarOrdersForTesting = {
        new Order(5, "Morgan", new StateTax("KY", 2), new Product("carpet", 3, 5), 2),
        new Order(6, "Theia", new StateTax("OH", 3), new Product("tile", 4, 6), 8),
        new Order(7, "Cronus", new StateTax("IN", 4), new Product("wood", 7, 22), 9),
        new Order(8, "Priam", new StateTax("MN", 5), new Product("glass", 1, 4), 10)
    };

    public OrderDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        testDao = new OrderDaoImpl();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void addOneToEmptyDaoTest() {
        Order order = new Order(1, "Rachel", new StateTax("OI", 6), new Product("bananas", 11, 55), 22);
        testDao.addOrder(year[0], order);
        Order possibleorder = testDao.getOrder(year[0], 1);
        Assert.assertEquals("Order stored vs order retrieved does not match.", order, possibleorder);
    }

    @Test
    public void testAgainstEmptyDAO() {
        assertEquals("Empty DAO should have 0 orders", 0, testDao.getAllDates().size());
        assertNull("If there isn't a order it should be null.", testDao.getOrder("1/1/1111", 0));
        assertNotNull("The hashmap should be instantiated so it shouldn't be null.", testDao.getAllDates());
    }

    @Test
    public void testAddOneOrder() {
        Order orderToAdd = ordersForTesting[0];
        testDao.addOrder(year[0], orderToAdd);
        assertEquals("After adding 1 order, the size should be 1.", 1, testDao.getOrdersByDate(year[0]).size());
        assertEquals("Returned order doesn't match expected", orderToAdd, testDao.getOrder(year[0], orderToAdd.getOrderNumber()));
        assertNotNull("List of all orders shouldn't be null.", testDao.getOrdersByDate(year[0]));
        assertTrue("Returned order in getAllOrders does not match expected.", testDao.getOrdersByDate(year[0]).contains(orderToAdd));
    }

    @Test
    public void testReplaceOneOrder() {
        testDao.addOrder(year[0], ordersForTesting[0]);
        testDao.addOrder(year[0], duplicateOrdersForTesting[0]);
        assertEquals("Expected order count doesn't match after replacing one order.", 1, testDao.getOrdersByDate(year[0]).size());
        assertEquals("Replaced order get doesn't match expected.", duplicateOrdersForTesting[0], testDao.getOrder(year[0], duplicateOrdersForTesting[0].getOrderNumber()));
        assertNotNull("List of all orders shouldn't be null.", testDao.getOrdersByDate(year[0]));
        assertTrue("Returned order in getAllOrders does not match expected.", testDao.getOrdersByDate(year[0]).contains(duplicateOrdersForTesting[0]));
    }

    @Test
    public void testAddOneSimilarOrder() {
        testDao.addOrder(year[0], ordersForTesting[0]);
        testDao.addOrder(year[0], similarOrdersForTesting[0]);
        assertEquals("Expected order count doesn't match after adding 2 orders", 2, testDao.getOrdersByDate(year[0]).size());
        assertNotNull("List of all orders shouldn't be null.", testDao.getOrdersByDate(year[0]));
        assertEquals("Added order get doesn't match expected.", ordersForTesting[0], testDao.getOrder(year[0], ordersForTesting[0].getOrderNumber()));
        assertEquals("Similar order get doesn't match expected.", similarOrdersForTesting[0], testDao.getOrder(year[0], similarOrdersForTesting[0].getOrderNumber()));

    }

    @Test
    public void addMultipleOrders() {
        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
        }

        assertEquals("Expected order count does not match after adding multiple orders", ordersForTesting.length, testDao.getOrdersByDate(year[0]).size());
        assertNotNull("List of all orders should not be null", testDao.getOrdersByDate(year[0]));

        for (int i = 0; i < ordersForTesting.length; i++) {
            assertEquals("Returned order does not match expected", ordersForTesting[i], testDao.getOrder(year[0], ordersForTesting[i].getOrderNumber()));
        }
    }

    @Test
    public void testAddDuplicateOrders() {
        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
        }
        for (Order order : duplicateOrdersForTesting) {
            testDao.addOrder(year[0], order);
        }

        assertEquals("Expected order count does not match after replacing duplicate orders", ordersForTesting.length, testDao.getOrdersByDate(year[0]).size());
        assertNotNull("List of all orders should not be null", testDao.getOrdersByDate(year[0]));

        for (int i = 0; i < duplicateOrdersForTesting.length; i++) {
            assertEquals("Returned order does not match expected replaced order", duplicateOrdersForTesting[i], testDao.getOrder(year[0], duplicateOrdersForTesting[i].getOrderNumber()));
        }
    }

    @Test
    public void testAddSimilarOrders() {
        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
        }
        for (Order order : similarOrdersForTesting) {
            testDao.addOrder(year[0], order);
        }

        assertEquals("Expected order count doest not match after adding simliar orders", ordersForTesting.length + similarOrdersForTesting.length, testDao.getOrdersByDate(year[0]).size());
        assertNotNull("List of all orders should not be null", testDao.getOrdersByDate(year[0]));

        for (int i = 0; i < ordersForTesting.length; i++) {
            assertEquals("Returned order does not match expected", ordersForTesting[i], testDao.getOrder(year[0], ordersForTesting[i].getOrderNumber()));
        }

        for (int i = 0; i < similarOrdersForTesting.length; i++) {
            assertEquals("Returned order does not match expected", similarOrdersForTesting[i], testDao.getOrder(year[0], similarOrdersForTesting[i].getOrderNumber()));
        }
    }

    @Test
    public void testAddAndRemoveOneOrder() {
        testDao.addOrder(year[0], ordersForTesting[0]);
        Order removed = testDao.removeOrder(year[0], ordersForTesting[0].getOrderNumber());

        assertNotNull("Order should be returned on removal.", removed);
        assertEquals("Order should be returned on removal", ordersForTesting[0], removed);
        assertEquals("Expected 0 orders after adding/removing 1 order", 0, testDao.getOrdersByDate(year[0]).size());
        assertNull("Order should return null after being removed", testDao.getOrder(year[0], ordersForTesting[0].getOrderNumber()));
        assertNotNull("List of all orders should not be null", testDao.getOrdersByDate(year[0]));
        assertEquals("Expected order count should be 0 after adding/removing 1 order", 0, testDao.getOrdersByDate(year[0]).size());
    }

    @Test
    public void testAddAndRemoveOrderTwice() {
        testDao.addOrder(year[0], ordersForTesting[0]);
        testDao.removeOrder(year[0], ordersForTesting[0].getOrderNumber());
        Order removed = testDao.removeOrder(year[0], ordersForTesting[0].getOrderNumber());

        assertNull("Order should be removed first time, so second removal should be null.", removed);
        assertEquals("Expected 0 orders after adding one order and then removing twice.", 0, testDao.getOrdersByDate(year[0]).size());
        assertNull("Order should return null after being removed.", testDao.getOrder(year[0], ordersForTesting[0].getOrderNumber()));
        assertNotNull("List of all orders should not be null", testDao.getOrdersByDate(year[0]));
    }

    @Test
    public void testAddAndRemoveMultipleOrders() {
        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
        }

        int ordersAdded = ordersForTesting.length;
        for (int i = 0; i < ordersForTesting.length; i += 2) {
            Order removed = testDao.removeOrder(year[0], ordersForTesting[i].getOrderNumber());
            assertNotNull("Order should be returned on removal.", removed);
            assertEquals("Order should be returned on removal.", ordersForTesting[i], removed);
            ordersAdded--;
        }

        assertNotNull("List of all orders should not be null", testDao.getOrdersByDate(year[0]));
        assertEquals("Expected order count does not match after removing half of the orders", ordersAdded, testDao.getOrdersByDate(year[0]).size());

        for (int i = 0; i < ordersForTesting.length; i++) {
            if (i % 2 == 0) {
                assertNull("Order should return null after being removed", testDao.getOrder(year[0], ordersForTesting[i].getOrderNumber()));
            } else {
                assertEquals("Returned order does not match expected", ordersForTesting[i], testDao.getOrder(year[0], ordersForTesting[i].getOrderNumber()));
            }
        }
    }

    @Test
    public void testAddAndRemoveOrdersMultipleTimes() {
        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
        }

        for (Order order : ordersForTesting) {
            testDao.removeOrder(year[0], order.getOrderNumber());
        }

        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
        }

        assertEquals("Expcted count not equal after adding/removing/re-adding year[0], ordersForTesting", ordersForTesting.length, testDao.getOrdersByDate(year[0]).size());
        assertEquals("Order should return after being re-added", ordersForTesting[0], testDao.getOrder(year[0], ordersForTesting[0].getOrderNumber()));
        assertNotNull("List of all orders should not be null", testDao.getOrdersByDate(year[0]));

        for (Order order : ordersForTesting) {
            testDao.removeOrder(year[0], order.getOrderNumber());
            testDao.addOrder(year[0], order);
            testDao.removeOrder(year[0], order.getOrderNumber());
        }

        assertEquals("There should no orders after removing them all.", 0, testDao.getOrdersByDate(year[0]).size());
        assertNull("Getting a order should return null since there is nothing there.", testDao.getOrder(year[0], ordersForTesting[0].getOrderNumber()));
        assertNotNull("Still: List of all orders should not be null", testDao.getOrdersByDate(year[0]));
    }

    @Test
    public void testOrderCountOnAddition() {
        for (int i = 0; i < ordersForTesting.length; i++) {
            testDao.addOrder(year[0], ordersForTesting[i]);
            assertEquals("Expected" + (i + 1) + " orders after adding another order", i + 1, testDao.getOrdersByDate(year[0]).size());
        }
    }

    @Test
    public void testOrderCountOnRemoval() {
        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
        }

        for (int i = 0; i < ordersForTesting.length; i++) {
            testDao.removeOrder(year[0], ordersForTesting[i].getOrderNumber());
            assertEquals("Expected " + (ordersForTesting.length - i - 1) + " orders after removing 1 at a time.", (ordersForTesting.length - i - 1), testDao.getOrdersByDate(year[0]).size());
        }
    }

    @Test
    public void testOrderCountAfterRemovalOfNonExistant() {
        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
        }

        testDao.removeOrder(year[0], 77);
        assertEquals("Expected size not to change.", ordersForTesting.length, testDao.getOrdersByDate(year[0]).size());
    }

    @Test
    public void testOrderCountAfterTwicedRemoved() {
        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
        }

        for (Order order : ordersForTesting) {
            testDao.removeOrder(year[0], order.getOrderNumber());
        }

        assertEquals("Expected 0 orders", 0, testDao.getOrdersByDate(year[0]).size());

        for (Order order : ordersForTesting) {
            testDao.removeOrder(year[0], order.getOrderNumber());
        }

        assertEquals("Still 0 orders", 0, testDao.getOrdersByDate(year[0]).size());
    }

    @Test
    public void testOrderCountOnReplace() {
        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
        }

        for (Order order : ordersForTesting) {
            testDao.addOrder(year[0], order);
            assertEquals("Expected size to not change.", true, testDao.getOrdersByDate(year[0]).size() == ordersForTesting.length);
        }
    }

    // FOR TESTING NESTED HASHMAPS
    
    @Test
    public void testMultipleYearsWithOneOrder() {

        for (int i = 0; i < year.length; i++) {
            testDao.addOrder(year[i], ordersForTesting[i]);
        }

        assertEquals("Adding multiple years with 1 order each.", year.length, testDao.getAllDates().size());
        assertNotNull(testDao.getAllDates());
    }

    @Test
    public void testMultipleYearsAddDelete() {

        for (int i = 0; i < year.length; i++) {
            testDao.addOrder(year[i], ordersForTesting[i]);
        }

        for (int i = 0; i < year.length; i++) {
            testDao.removeOrder(year[i], ordersForTesting[i].getOrderNumber());
        }
        
        for (String year : year) {
            assertEquals("Orders should all be removed.", 0, testDao.getOrdersByDate(year).size());
        }

        assertEquals("Adding and removing multiple years with 1 order each should be 4 years worth of data", 4, testDao.getAllDates().size());
        assertNotNull(testDao.getAllDates());
    }

    @Test
    public void testMultipleYearsWithMultipleOrders() {

        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < ordersForTesting.length; j++) {
                testDao.addOrder(year[i], ordersForTesting[j]);
            }
        }

        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < ordersForTesting.length; j++) {
                assertEquals("Returned order does not match expected replaced order", ordersForTesting[j], testDao.getOrder(year[i], ordersForTesting[j].getOrderNumber()));
            }
        }

        assertEquals("Adding multiple years with 1 order each.", year.length, testDao.getAllDates().size());
        assertNotNull(testDao.getAllDates());
    }

    @Test
    public void testMultipleYearsWithDuplicateOrders() {

        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < ordersForTesting.length; j++) {
                testDao.addOrder(year[i], ordersForTesting[j]);
                testDao.addOrder(year[i], duplicateOrdersForTesting[j]);
            }
        }

        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < ordersForTesting.length; j++) {
                assertEquals("Returned order does not match expected replaced order", duplicateOrdersForTesting[j], testDao.getOrder(year[i], duplicateOrdersForTesting[j].getOrderNumber()));
            }
        }

        for (String year : year) {
            assertEquals("Size of orders does not match year.", ordersForTesting.length, testDao.getOrdersByDate(year).size());
        }

        assertEquals("Adding multiple years with duplicate orders.", year.length, testDao.getAllDates().size());
        assertNotNull(testDao.getAllDates());
    }

    @Test
    public void testMultipleYearsWithSimilarOrders() {

        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < ordersForTesting.length; j++) {
                testDao.addOrder(year[i], ordersForTesting[j]);
                testDao.addOrder(year[i], similarOrdersForTesting[j]);
            }
        }

        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < ordersForTesting.length; j++) {
                assertEquals("Returned order does not match expected order", ordersForTesting[j], testDao.getOrder(year[i], ordersForTesting[j].getOrderNumber()));
            }
            for (int j = 0; j < similarOrdersForTesting.length; j++) {
                assertEquals("Returned order does not match expected similar order", similarOrdersForTesting[j], testDao.getOrder(year[i], similarOrdersForTesting[j].getOrderNumber()));
            }
        }

        for (String year : year) {
            assertEquals("Size of orders does not match year.", ordersForTesting.length + similarOrdersForTesting.length, testDao.getOrdersByDate(year).size());
        }

        assertEquals("Adding multiple years with similar orders.", year.length, testDao.getAllDates().size());
        assertNotNull(testDao.getAllDates());
    }
}
