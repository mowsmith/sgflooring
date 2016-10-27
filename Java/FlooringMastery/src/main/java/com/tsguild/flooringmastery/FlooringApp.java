package com.tsguild.flooringmastery;

import com.tsguild.flooringmastery.ops.FlooringController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Morgan Smith
 */
public class FlooringApp {

    public static void main(String[] args) {

        ApplicationContext springFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringController ctrl = springFactory.getBean("controller", FlooringController.class);
        ctrl.run();
    }
}
