/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Product;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apprentice
 */
public class ProductDaoTest {
    
    ProductDao testDao;
    Product[] productsForTesting = {
        new Product("wood", 3, 5),
        new Product("tile", 4, 7),
        new Product("carpet", 5, 8),
        new Product("glass", 8, 10)
    };
    Product[] duplicateCodesForTesting = {
        new Product("wood", 9, 51),
        new Product("tile", 44, 72),
        new Product("carpet", 55, 48),
        new Product("glass", 85, 101)
    };
    Product[] similarProductsForTesting = {
        new Product("woodish", 3, 5),
        new Product("tileish", 4, 7),
        new Product("carpetish", 5, 8),
        new Product("glassish", 8, 10)
    };
    
    public ProductDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testDao = new ProductDaoImpl();
    }
    
    @After
    public void tearDown() {
    } 
    
    @Test
    public void addOneToEmptyDaoTest() {
        Product product = new Product("cats", 3, 4);
        testDao.addProduct(product);
        Product possibleproduct = testDao.getProduct(product.getProductType());
        Assert.assertEquals("Product stored vs product retrieved does not match.", product, possibleproduct);
    }

    @Test
    public void testAgainstEmptyDAO() {
        assertEquals("Empty DAO should have 0 products", 0, testDao.getAllProducts().size());
        assertNull("If there isn't a product it should be null.", testDao.getProduct("productsMcGoo"));
        assertNotNull("The hashmap should be instantiated so it shouldn't be null.", testDao.getAllProducts());
    }

    @Test
    public void testAddOneProduct() {
        Product productToAdd = productsForTesting[0];
        testDao.addProduct(productToAdd);
        assertEquals("After adding 1 product, the size should be 1.", 1, testDao.getAllProducts().size());
        assertEquals("Returned product doesn't match expected", productToAdd, testDao.getProduct(productToAdd.getProductType()));
        assertNotNull("List of all products shouldn't be null.", testDao.getAllProducts());
        assertTrue("Returned product in getAllProducts does not match expected.", testDao.getAllProducts().contains(productToAdd));
    }

    @Test
    public void testReplaceOneProduct() {
        testDao.addProduct(productsForTesting[0]);
        testDao.addProduct(duplicateCodesForTesting[0]);
        assertEquals("Expected product count doesn't match after replacing one product.", 1, testDao.getAllProducts().size());
        assertEquals("Replaced product get doesn't match expected.", duplicateCodesForTesting[0], testDao.getProduct(duplicateCodesForTesting[0].getProductType()));
        assertNotNull("List of all products shouldn't be null.", testDao.getAllProducts());
        assertTrue("Returned product in getAllProducts does not match expected.", testDao.getAllProducts().contains(duplicateCodesForTesting[0]));
    }

    @Test
    public void testAddOneSimilarProduct() {
        testDao.addProduct(productsForTesting[0]);
        testDao.addProduct(similarProductsForTesting[0]);
        assertEquals("Expected product count doesn't match after adding 2 products", 2, testDao.getAllProducts().size());
        assertNotNull("List of all products shouldn't be null.", testDao.getAllProducts());
        assertEquals("Added product get doesn't match expected.", productsForTesting[0], testDao.getProduct(productsForTesting[0].getProductType()));
        assertEquals("Similar product get doesn't match expected.", similarProductsForTesting[0], testDao.getProduct(similarProductsForTesting[0].getProductType()));

    }

    @Test
    public void addMultipleProducts() {
        for (Product product : productsForTesting) {
            testDao.addProduct(product);
        }

        assertEquals("Expected product count does not match after adding multiple products", productsForTesting.length, testDao.getAllProducts().size());
        assertNotNull("List of all products should not be null", testDao.getAllProducts());

        for (int i = 0; i < productsForTesting.length; i++) {
            assertEquals("Returned product does not match expected", productsForTesting[i], testDao.getProduct(productsForTesting[i].getProductType()));
        }
    }

    @Test
    public void testAddDuplicateProducts() {
        for (Product product : productsForTesting) {
            testDao.addProduct(product);
        }
        for (Product product : duplicateCodesForTesting) {
            testDao.addProduct(product);
        }

        assertEquals("Expected product count does not match after replacing duplicate products", productsForTesting.length, testDao.getAllProducts().size());
        assertNotNull("List of all products should not be null", testDao.getAllProducts());

        for (int i = 0; i < duplicateCodesForTesting.length; i++) {
            assertEquals("Returned product does not match expected replaced product", duplicateCodesForTesting[i], testDao.getProduct(duplicateCodesForTesting[i].getProductType()));
        }
    }

    @Test
    public void testAddSimilarProducts() {
        for (Product product : productsForTesting) {
            testDao.addProduct(product);
        }
        for (Product product : similarProductsForTesting) {
            testDao.addProduct(product);
        }

        assertEquals("Expected product count doest not match after adding simliar products", productsForTesting.length + similarProductsForTesting.length, testDao.getAllProducts().size());
        assertNotNull("List of all products should not be null", testDao.getAllProducts());

        for (int i = 0; i < productsForTesting.length; i++) {
            assertEquals("Returned product does not match expected", productsForTesting[i], testDao.getProduct(productsForTesting[i].getProductType()));
        }

        for (int i = 0; i < similarProductsForTesting.length; i++) {
            assertEquals("Returned product does not match expected", similarProductsForTesting[i], testDao.getProduct(similarProductsForTesting[i].getProductType()));
        }
    }

    @Test
    public void testAddAndRemoveOneProduct() {
        testDao.addProduct(productsForTesting[0]);
        Product removed = testDao.removeProduct(productsForTesting[0].getProductType());

        assertNotNull("Product should be returned on removal.", removed);
        assertEquals("Product should be returned on removal", productsForTesting[0], removed);
        assertEquals("Expected 0 products after adding/removing 1 product", 0, testDao.getAllProducts().size());
        assertNull("Product should return null after being removed", testDao.getProduct(productsForTesting[0].getProductType()));
        assertNotNull("List of all products should not be null", testDao.getAllProducts());
        assertEquals("Expected product count should be 0 after adding/removing 1 product", 0, testDao.getAllProducts().size());
    }

    @Test
    public void testAddAndRemoveProductTwice() {
        testDao.addProduct(productsForTesting[0]);
        testDao.removeProduct(productsForTesting[0].getProductType());
        Product removed = testDao.removeProduct(productsForTesting[0].getProductType());

        assertNull("Product should be removed first time, so second removal should be null.", removed);
        assertEquals("Expected 0 products after adding one product and then removing twice.", 0, testDao.getAllProducts().size());
        assertNull("Product should return null after being removed.", testDao.getProduct(productsForTesting[0].getProductType()));
        assertNotNull("List of all products should not be null", testDao.getAllProducts());
    }

    @Test
    public void testAddAndRemoveMultipleProducts() {
        for (Product product : productsForTesting) {
            testDao.addProduct(product);
        }

        int productsAdded = productsForTesting.length;
        for (int i = 0; i < productsForTesting.length; i += 2) {
            Product removed = testDao.removeProduct(productsForTesting[i].getProductType());
            assertNotNull("Product should be returned on removal.", removed);
            assertEquals("Product should be returned on removal.", productsForTesting[i], removed);
            productsAdded--;
        }

        assertNotNull("List of all products should not be null", testDao.getAllProducts());
        assertEquals("Expected product count does not match after removing half of the products", productsAdded, testDao.getAllProducts().size());

        for (int i = 0; i < productsForTesting.length; i++) {
            if (i % 2 == 0) {
                assertNull("Product should return null after being removed", testDao.getProduct(productsForTesting[i].getProductType()));
            } else {
                assertEquals("Returned product does not match expected", productsForTesting[i], testDao.getProduct(productsForTesting[i].getProductType()));
            }
        }
    }

    @Test
    public void testAddAndRemoveProductsMultipleTimes() {
        for (Product product : productsForTesting) {
            testDao.addProduct(product);
        }

        for (Product product : productsForTesting) {
            testDao.removeProduct(product.getProductType());
        }

        for (Product product : productsForTesting) {
            testDao.addProduct(product);
        }

        assertEquals("Expcted count not equal after adding/removing/re-adding productsForTesting", productsForTesting.length, testDao.getAllProducts().size());
        assertEquals("Product should return after being re-added", productsForTesting[0], testDao.getProduct(productsForTesting[0].getProductType()));
        assertNotNull("List of all products should not be null", testDao.getAllProducts());

        for (Product product : productsForTesting) {
            testDao.removeProduct(product.getProductType());
            testDao.addProduct(product);
            testDao.removeProduct(product.getProductType());
        }

        assertEquals("There should no products after removing them all.", 0, testDao.getAllProducts().size());
        assertNull("Getting a product should return null since there is nothing there.", testDao.getProduct(productsForTesting[0].getProductType()));
        assertNotNull("Still: List of all products should not be null", testDao.getAllProducts());
    }

    @Test
    public void testProductCountOnAddition() {
        for (int i = 0; i < productsForTesting.length; i++) {
            testDao.addProduct(productsForTesting[i]);
            assertEquals("Expected" + (i + 1) + " products after adding another product", i+1, testDao.getAllProducts().size());
        }
    }
    
    @Test
    public void testProductCountOnRemoval() {
        for (Product product : productsForTesting) {
            testDao.addProduct(product);
        }
        
        for (int i = 0; i < productsForTesting.length; i++) {
            testDao.removeProduct(productsForTesting[i].getProductType());
            assertEquals("Expected " + (productsForTesting.length - i - 1) + " products after removing 1 at a time.", (productsForTesting.length - i - 1), testDao.getAllProducts().size());
        }
    }
    
    @Test
    public void testProductCountAfterRemovalOfNonExistant() {
        for (Product product : productsForTesting) {
            testDao.addProduct(product);
        }
        
        testDao.removeProduct("productsMcGoo");
        assertEquals("Expected size not to change.", productsForTesting.length, testDao.getAllProducts().size());
    }
    
    @Test
    public void testProductCountAfterTwicedRemoved() {
        for (Product product : productsForTesting) {
            testDao.addProduct(product);
        }
        
        for (Product product : productsForTesting) {
            testDao.removeProduct(product.getProductType());
        }
        
        assertEquals("Expected 0 products", 0, testDao.getAllProducts().size());
        
        for (Product product : productsForTesting) {
            testDao.removeProduct(product.getProductType());
        }
        
        assertEquals("Still 0 products", 0, testDao.getAllProducts().size());
    }
    
    @Test
    public void testProductCountOnReplace() {
        for (Product product : productsForTesting) {
            testDao.addProduct(product);
        }
        
        for (Product product : productsForTesting) {
            testDao.addProduct(product);
            assertEquals("Expected size to not change.", true, testDao.getAllProducts().size() == productsForTesting.length);
        }
    }
}
