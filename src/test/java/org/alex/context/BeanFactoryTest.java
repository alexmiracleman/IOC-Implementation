package org.alex.context;

import org.alex.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BeanFactoryTest {
    private final BeanFactory beanFactory = new BeanFactory();
    private final ClassPathApplicationContext classPathApplicationContext = new ClassPathApplicationContext();

    @Test
    @DisplayName("Getting raw beans from bean definitions;")
    public void testGetRawBeansFromBeanDefinitions() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        beanDefinitions.add(new BeanDefinition("customerService", "org.alex.entity.CustomerService",
                new HashMap<>(), new HashMap<>()));
        beanDefinitions.add(new BeanDefinition("rentalService","org.alex.entity.RentalService",
                new HashMap<>(), new HashMap<>()));
        beanDefinitions.add(new BeanDefinition("towingService", "org.alex.entity.TowingService",
                new HashMap<>(), new HashMap<>()));

        List<Bean> beans = beanFactory.getRawBeans(beanDefinitions);

        RentalService rentals = (RentalService) classPathApplicationContext.getBean("rentalService", beans);
        CustomerService cs = (CustomerService) classPathApplicationContext.getBean("customerService", beans);
        TowingService towing = (TowingService) classPathApplicationContext.getBean("towingService", beans);

        assertNotNull(rentals);
        assertNotNull(cs);
        assertNotNull(towing);
    }

    @Test
    @DisplayName("Dependency value injection from bean definitions")
    public void testInjectValueDependencies() {
        List<Bean> beans = new ArrayList<>();
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        beans.add(new Bean("towingService", new TowingService()));
        beans.add(new Bean("rentalService", new RentalService()));
        beans.add(new Bean("customerService", new CustomerService()));

        beanDefinitions.add(new BeanDefinition("rentalService", "org.alex.entity.RentalService",
                Map.of("numberOfCarsAvailable", "66", "extraCoverage", "true"), new HashMap<>()));
        beanDefinitions.add(new BeanDefinition("customerService", "org.alex.entity.CustomerService",
                Map.of("phoneNumber", "911", "email", "test@test.com"), new HashMap<>()));

        beanFactory.injectValueDependencies(beans, beanDefinitions);

        int expectedAmountOfCars = 66;
        boolean expectedCoverage = true;
        String expectedPhoneNumber = "911";
        String expectedEmailAddress = "test@test.com";

        boolean actualCoverage = ((RentalService) classPathApplicationContext.getBean("rentalService", beans)).isExtraCoverage();
        int actualAmountOfCars = ((RentalService) classPathApplicationContext.getBean("rentalService", beans)).getNumberOfCarsAvailable();
        String actualPhoneNumber = ((CustomerService) classPathApplicationContext.getBean("customerService", beans)).getPhoneNumber();
        String actualEmailAddress = ((CustomerService) classPathApplicationContext.getBean("customerService", beans)).getEmail();
        assertEquals(expectedAmountOfCars, actualAmountOfCars);
        assertEquals(expectedCoverage, actualCoverage);
        assertEquals(expectedPhoneNumber, actualPhoneNumber);
        assertEquals(expectedEmailAddress, actualEmailAddress);
    }

    @Test
    @DisplayName("Ref dependency value injection from bean definitions")
    public void testInjectRefDependencies() {
        List<Bean> beans = new ArrayList<>();
        List<BeanDefinition> beanDefinitions = new ArrayList<>();

        beans.add(new Bean("towingService", new TowingService()));
        beans.add(new Bean("rentalService", new RentalService()));
        beans.add(new Bean("customerService", new CustomerService()));

        beanDefinitions.add(new BeanDefinition("customerService", "org.alex.entity.CustomerService",
                new HashMap<>(), new HashMap<>()));
        beanDefinitions.add(new BeanDefinition("rentalService","org.alex.entity.RentalService",
                new HashMap<>(), Map.of("customerService", "customerService")));
        beanDefinitions.add(new BeanDefinition("towingService", "org.alex.entity.TowingService",
                new HashMap<>(), Map.of("customerService", "customerService")));

        beanFactory.injectRefDependencies(beans, beanDefinitions);

        RentalService rentals = (RentalService) classPathApplicationContext.getBean("rentalService", beans);
        CustomerService cs = (CustomerService) classPathApplicationContext.getBean("customerService", beans);
        TowingService towing = (TowingService) classPathApplicationContext.getBean("towingService", beans);

        CustomerService csRentalService = rentals.getCustomerService();
        CustomerService csTowingService = towing.getCustomerService();

        assertNotNull(cs);
        assertNotNull(csRentalService);
        assertNotNull(csTowingService);
        assertNotNull(towing);

        assertSame(csRentalService, csTowingService);
        assertSame(cs, csRentalService);
        assertSame(cs, csTowingService);
    }
}

