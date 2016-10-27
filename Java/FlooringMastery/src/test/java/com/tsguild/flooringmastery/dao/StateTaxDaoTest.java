/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.StateTax;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apprentice
 */
public class StateTaxDaoTest {

    StateTaxDao testDao;
    StateTax[] stateTaxesForTesting = {
        new StateTax("KY", 1),
        new StateTax("IN", 2),
        new StateTax("OH", 3),
        new StateTax("MN", 4)
    };
    StateTax[] duplicateStateTaxesForTesting = {
        new StateTax("KY", 4),
        new StateTax("IN", 5),
        new StateTax("OH", 6),
        new StateTax("MN", 7)
    };
    StateTax[] similarStateTaxesForTesting = {
        new StateTax("KA", 1),
        new StateTax("IA", 2),
        new StateTax("OA", 3),
        new StateTax("MA", 4)
    };

    public StateTaxDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        testDao = new StateTaxDaoImpl();
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void addOneToEmptyDaoTest() {
        StateTax state = new StateTax("MN", 3.5);
        testDao.addStateTax(state);
        StateTax possibleState = testDao.getStateTax(state.getState());
        assertEquals("Expected state does not much state retrieved.", state, possibleState);
    }
    
    @Test
    public void testEmptyDao() {
        assertEquals("Empty DAO should have 0 states", 0, testDao.getAllStateTaxes().size());
        assertNull("If there isn't a state it should be null.", testDao.getStateTax("KY"));
        assertNotNull("The hashmap should be instantiated so it shouldn't be null.", testDao.getAllStateTaxes());
    }
    
    @Test
    public void testAddOneStateTax() {
        StateTax stateToAdd = stateTaxesForTesting[0];
        testDao.addStateTax(stateToAdd);
        assertEquals("After adding 1 state, the size should be 1.", 1, testDao.getAllStateTaxes().size());
        assertEquals("Returned state doesn't match expected", stateToAdd, testDao.getStateTax(stateToAdd.getState()));
        assertNotNull("List of all states shouldn't be null.", testDao.getAllStateTaxes());
        assertTrue("Returned state in getAllStateTaxes does not match expected.", testDao.getAllStateTaxes().contains(stateToAdd));
    }

    @Test
    public void testReplaceOneStateTax() {
        testDao.addStateTax(stateTaxesForTesting[0]);
        testDao.addStateTax(duplicateStateTaxesForTesting[0]);
        assertEquals("Expected state count doesn't match after replacing one state.", 1, testDao.getAllStateTaxes().size());
        assertEquals("Replaced state get doesn't match expected.", duplicateStateTaxesForTesting[0], testDao.getStateTax(duplicateStateTaxesForTesting[0].getState()));
        assertNotNull("List of all states shouldn't be null.", testDao.getAllStateTaxes());
        assertTrue("Returned state in getAllStateTaxes does not match expected.", testDao.getAllStateTaxes().contains(duplicateStateTaxesForTesting[0]));
    }
    
    @Test
    public void testAddOneSimilarStateTax() {
        testDao.addStateTax(stateTaxesForTesting[0]);
        testDao.addStateTax(similarStateTaxesForTesting[0]);
        assertEquals("Expected state count doesn't match after adding 2 states", 2, testDao.getAllStateTaxes().size());
        assertNotNull("List of all states shouldn't be null.", testDao.getAllStateTaxes());
        assertEquals("Added state get doesn't match expected.", stateTaxesForTesting[0], testDao.getStateTax(stateTaxesForTesting[0].getState()));
        assertEquals("Similar state get doesn't match expected.", similarStateTaxesForTesting[0], testDao.getStateTax(similarStateTaxesForTesting[0].getState()));

    }
    
    @Test
    public void addMultipleStateTaxs() {
        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
        }

        assertEquals("Expected state count does not match after adding multiple states", stateTaxesForTesting.length, testDao.getAllStateTaxes().size());
        assertNotNull("List of all states should not be null", testDao.getAllStateTaxes());

        for (int i = 0; i < stateTaxesForTesting.length; i++) {
            assertEquals("Returned state does not match expected", stateTaxesForTesting[i], testDao.getStateTax(stateTaxesForTesting[i].getState()));
        }
    }
    
    @Test
    public void testAddDuplicateStateTaxs() {
        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
        }
        for (StateTax state : duplicateStateTaxesForTesting) {
            testDao.addStateTax(state);
        }

        assertEquals("Expected state count does not match after replacing duplicate states", stateTaxesForTesting.length, testDao.getAllStateTaxes().size());
        assertNotNull("List of all states should not be null", testDao.getAllStateTaxes());

        for (int i = 0; i < duplicateStateTaxesForTesting.length; i++) {
            assertEquals("Returned state does not match expected replaced state", duplicateStateTaxesForTesting[i], testDao.getStateTax(duplicateStateTaxesForTesting[i].getState()));
        }
    }
    
    @Test
    public void testAddSimilarStateTaxs() {
        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
        }
        for (StateTax state : similarStateTaxesForTesting) {
            testDao.addStateTax(state);
        }

        assertEquals("Expected state count doest not match after adding simliar states", stateTaxesForTesting.length + similarStateTaxesForTesting.length, testDao.getAllStateTaxes().size());
        assertNotNull("List of all states should not be null", testDao.getAllStateTaxes());

        for (int i = 0; i < stateTaxesForTesting.length; i++) {
            assertEquals("Returned state does not match expected", stateTaxesForTesting[i], testDao.getStateTax(stateTaxesForTesting[i].getState()));
        }

        for (int i = 0; i < similarStateTaxesForTesting.length; i++) {
            assertEquals("Returned state does not match expected", similarStateTaxesForTesting[i], testDao.getStateTax(similarStateTaxesForTesting[i].getState()));
        }
    }
    
    @Test
    public void testAddAndRemoveOneStateTax() {
        testDao.addStateTax(stateTaxesForTesting[0]);
        StateTax removed = testDao.removeStateTax(stateTaxesForTesting[0].getState());

        assertNotNull("StateTax should be returned on removal.", removed);
        assertEquals("StateTax should be returned on removal", stateTaxesForTesting[0], removed);
        assertEquals("Expected 0 states after adding/removing 1 state", 0, testDao.getAllStateTaxes().size());
        assertNull("StateTax should return null after being removed", testDao.getStateTax(stateTaxesForTesting[0].getState()));
        assertNotNull("List of all states should not be null", testDao.getAllStateTaxes());
        assertEquals("Expected state count should be 0 after adding/removing 1 state", 0, testDao.getAllStateTaxes().size());
    }

    @Test
    public void testAddAndRemoveStateTaxTwice() {
        testDao.addStateTax(stateTaxesForTesting[0]);
        testDao.removeStateTax(stateTaxesForTesting[0].getState());
        StateTax removed = testDao.removeStateTax(stateTaxesForTesting[0].getState());

        assertNull("StateTax should be removed first time, so second removal should be null.", removed);
        assertEquals("Expected 0 states after adding one state and then removing twice.", 0, testDao.getAllStateTaxes().size());
        assertNull("StateTax should return null after being removed.", testDao.getStateTax(stateTaxesForTesting[0].getState()));
        assertNotNull("List of all states should not be null", testDao.getAllStateTaxes());
    }
    
    @Test
    public void testAddAndRemoveMultipleStateTaxs() {
        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
        }

        int statesAdded = stateTaxesForTesting.length;
        for (int i = 0; i < stateTaxesForTesting.length; i += 2) {
            StateTax removed = testDao.removeStateTax(stateTaxesForTesting[i].getState());
            assertNotNull("StateTax should be returned on removal.", removed);
            assertEquals("StateTax should be returned on removal.", stateTaxesForTesting[i], removed);
            statesAdded--;
        }

        assertNotNull("List of all states should not be null", testDao.getAllStateTaxes());
        assertEquals("Expected state count does not match after removing half of the states", statesAdded, testDao.getAllStateTaxes().size());

        for (int i = 0; i < stateTaxesForTesting.length; i++) {
            if (i % 2 == 0) {
                assertNull("StateTax should return null after being removed", testDao.getStateTax(stateTaxesForTesting[i].getState()));
            } else {
                assertEquals("Returned state does not match expected", stateTaxesForTesting[i], testDao.getStateTax(stateTaxesForTesting[i].getState()));
            }
        }
    }

    @Test
    public void testAddAndRemoveStateTaxsMultipleTimes() {
        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
        }

        for (StateTax state : stateTaxesForTesting) {
            testDao.removeStateTax(state.getState());
        }

        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
        }

        assertEquals("Expcted count not equal after adding/removing/re-adding stateTaxesForTesting", stateTaxesForTesting.length, testDao.getAllStateTaxes().size());
        assertEquals("StateTax should return after being re-added", stateTaxesForTesting[0], testDao.getStateTax(stateTaxesForTesting[0].getState()));
        assertNotNull("List of all states should not be null", testDao.getAllStateTaxes());

        for (StateTax state : stateTaxesForTesting) {
            testDao.removeStateTax(state.getState());
            testDao.addStateTax(state);
            testDao.removeStateTax(state.getState());
        }

        assertEquals("There should no states after removing them all.", 0, testDao.getAllStateTaxes().size());
        assertNull("Getting a state should return null since there is nothing there.", testDao.getStateTax(stateTaxesForTesting[0].getState()));
        assertNotNull("Still: List of all states should not be null", testDao.getAllStateTaxes());
    }

    @Test
    public void testStateTaxCountOnAddition() {
        for (int i = 0; i < stateTaxesForTesting.length; i++) {
            testDao.addStateTax(stateTaxesForTesting[i]);
            assertEquals("Expected" + (i + 1) + " states after adding another state", i+1, testDao.getAllStateTaxes().size());
        }
    }
    
    @Test
    public void testStateTaxCountOnRemoval() {
        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
        }
        
        for (int i = 0; i < stateTaxesForTesting.length; i++) {
            testDao.removeStateTax(stateTaxesForTesting[i].getState());
            assertEquals("Expected " + (stateTaxesForTesting.length - i - 1) + " states after removing 1 at a time.", (stateTaxesForTesting.length - i - 1), testDao.getAllStateTaxes().size());
        }
    }
    
    @Test
    public void testStateTaxCountAfterRemovalOfNonExistant() {
        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
        }
        
        testDao.removeStateTax("PN");
        assertEquals("Expected size not to change.", stateTaxesForTesting.length, testDao.getAllStateTaxes().size());
    }
    
    @Test
    public void testStateTaxCountAfterTwicedRemoved() {
        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
        }
        
        for (StateTax state : stateTaxesForTesting) {
            testDao.removeStateTax(state.getState());
        }
        
        assertEquals("Expected 0 states", 0, testDao.getAllStateTaxes().size());
        
        for (StateTax state : stateTaxesForTesting) {
            testDao.removeStateTax(state.getState());
        }
        
        assertEquals("Still 0 states", 0, testDao.getAllStateTaxes().size());
    }
    
    @Test
    public void testStateTaxCountOnReplace() {
        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
        }
        
        for (StateTax state : stateTaxesForTesting) {
            testDao.addStateTax(state);
            assertEquals("Expected size to not change.", true, testDao.getAllStateTaxes().size() == stateTaxesForTesting.length);
        }
    }
}
