//package org.alex.context;
//
//import org.alex.entity.*;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class BeanFactoryTest {
//    private final BeanFactory beanFactory = new BeanFactory();
//
//    @Test
//    @DisplayName("Getting raw beans from bean definitions;")
//    public void testGetRawBeansFromBeanDefinitions() {
//        List<BeanDefinition> beanDefinitions = new ArrayList<>();
//
//        BeanDefinition beanDefinition1 = new BeanDefinition();
//        beanDefinition1.setId("customerService");
//        beanDefinition1.setBeanClassName("org.alex.entity.CustomerService");
//        beanDefinition1.setDependencies(new HashMap<>());
//        beanDefinition1.setRefDependencies(new HashMap<>());
//
//        BeanDefinition beanDefinition2 = new BeanDefinition();
//        beanDefinition2.setId("rentalService");
//        beanDefinition2.setBeanClassName("org.alex.entity.RentalService");
//        beanDefinition2.setDependencies(new HashMap<>());
//        beanDefinition2.setRefDependencies(new HashMap<>());
//
//        BeanDefinition beanDefinition3 = new BeanDefinition();
//        beanDefinition3.setId("towingService");
//        beanDefinition3.setBeanClassName("org.alex.entity.TowingService");
//        beanDefinition3.setDependencies(new HashMap<>());
//        beanDefinition3.setRefDependencies(new HashMap<>());
//
//        beanDefinitions.add(beanDefinition1);
//        beanDefinitions.add(beanDefinition2);
//        beanDefinitions.add(beanDefinition3);
//
//        List<Bean> beans = beanFactory.createListOfBeans(beanDefinitions);
//
//        RentalService rentals = (RentalService) getBean("rentalService", beans);
//        CustomerService cs = (CustomerService) getBean("customerService", beans);
//        TowingService towing = (TowingService) getBean("towingService", beans);
//
//        assertNotNull(rentals);
//        assertNotNull(cs);
//        assertNotNull(towing);
//    }
//
//    @Test
//    @DisplayName("Dependency value injection from bean definitions")
//    public void testInjectValueDependencies() {
//        List<Bean> beans = new ArrayList<>();
//        List<BeanDefinition> beanDefinitions = new ArrayList<>();
//        beans.add(new Bean("towingService", new TowingService()));
//        beans.add(new Bean("rentalService", new RentalService()));
//        beans.add(new Bean("customerService", new CustomerService()));
//
//        BeanDefinition beanDefinition1 = new BeanDefinition();
//        beanDefinition1.setId("rentalService");
//        beanDefinition1.setBeanClassName("org.alex.entity.RentalService");
//        beanDefinition1.setDependencies(Map.of("numberOfCarsAvailable", "66", "extraCoverage", "true"));
//        beanDefinition1.setRefDependencies(new HashMap<>());
//
//        BeanDefinition beanDefinition2 = new BeanDefinition();
//        beanDefinition2.setId("customerService");
//        beanDefinition2.setBeanClassName("org.alex.entity.CustomerService");
//        beanDefinition2.setDependencies(Map.of("phoneNumber", "911", "email", "test@test.com"));
//        beanDefinition2.setRefDependencies(new HashMap<>());
//
//        beanDefinitions.add(beanDefinition1);
//        beanDefinitions.add(beanDefinition2);
//
//        beanFactory.injectValueDependencies(beans, beanDefinitions);
//
//        int expectedAmountOfCars = 66;
//        boolean expectedCoverage = true;        String expectedPhoneNumber = "911";
//        String expectedEmailAddress = "test@test.com";
//
//        boolean actualCoverage = ((RentalService) getBean("rentalService", beans)).isExtraCoverage();
//        int actualAmountOfCars = ((RentalService) getBean("rentalService", beans)).getNumberOfCarsAvailable();
//        String actualPhoneNumber = ((CustomerService) getBean("customerService", beans)).getPhoneNumber();
//        String actualEmailAddress = ((CustomerService) getBean("customerService", beans)).getEmail();
//        assertEquals(expectedAmountOfCars, actualAmountOfCars);
//        assertEquals(expectedCoverage, actualCoverage);        assertEquals(expectedPhoneNumber, actualPhoneNumber);
//        assertEquals(expectedEmailAddress, actualEmailAddress);
//    }
//
//    @Test
//    @DisplayName("Ref dependency value injection from bean definitions")
//    public void testInjectRefDependencies() {
//        List<Bean> beans = new ArrayList<>();
//        List<BeanDefinition> beanDefinitions = new ArrayList<>();
//
//        beans.add(new Bean("towingService", new TowingService()));
//        beans.add(new Bean("rentalService", new RentalService()));
//        beans.add(new Bean("customerService", new CustomerService()));
//
//        BeanDefinition beanDefinition1 = new BeanDefinition();
//        beanDefinition1.setId("customerService");
//        beanDefinition1.setBeanClassName("org.alex.entity.CustomerService");
//        beanDefinition1.setDependencies(new HashMap<>());
//        beanDefinition1.setRefDependencies(new HashMap<>());
//
//        BeanDefinition beanDefinition2 = new BeanDefinition();
//        beanDefinition2.setId("rentalService");
//        beanDefinition2.setBeanClassName("org.alex.entity.RentalService");
//        beanDefinition2.setDependencies(new HashMap<>());
//        beanDefinition2.setRefDependencies(Map.of("customerService", "customerService"));
//
//        BeanDefinition beanDefinition3 = new BeanDefinition();
//        beanDefinition3.setId("towingService");
//        beanDefinition3.setBeanClassName("org.alex.entity.TowingService");
//        beanDefinition3.setDependencies(new HashMap<>());
//        beanDefinition3.setRefDependencies(Map.of("customerService", "customerService"));
//
//        beanDefinitions.add(beanDefinition1);
//        beanDefinitions.add(beanDefinition2);
//        beanDefinitions.add(beanDefinition3);
//
//        beanFactory.injectRefDependencies(beans, beanDefinitions);
//
//        RentalService rentals = (RentalService) getBean("rentalService", beans);
//        CustomerService cs = (CustomerService) getBean("customerService", beans);
//        TowingService towing = (TowingService) getBean("towingService", beans);
//
//        CustomerService csRentalService = rentals.getCustomerService();
//        CustomerService csTowingService = towing.getCustomerService();
//
//        assertNotNull(cs);
//        assertNotNull(csRentalService);
//        assertNotNull(csTowingService);
//        assertNotNull(towing);
//
//        assertSame(csRentalService, csTowingService);
//        assertSame(cs, csRentalService);
//        assertSame(cs, csTowingService);
//    }
//    public Object getBean(String id, List<Bean> beans) {
//        for (Bean bean : beans) {
//            if (bean.getId().equals(id)) {
//                return bean.getValue();
//            }
//        }
//        return null;
//    }
//}
//
