package org.alex.context;

import org.alex.entity.CustomerService;
import org.alex.entity.RentalService;
import org.alex.entity.TowingService;
import org.alex.exception.ContainerException;
import org.alex.reader.sax.SAXBeanDefinitionReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClassPathApplicationContextITest {
    private final ClassPathApplicationContext domContext = new ClassPathApplicationContext("/context/context_test.xml",
            "/context/import_context_test.xml");

        private final ClassPathApplicationContext saxContext = new ClassPathApplicationContext
                    (new SAXBeanDefinitionReader("/context/context_test.xml",
                            "/context/import_context_test.xml"));

    private final String duplicateClassBeanContext = "/context/one_class_different_beans_context_test.xml";

    @Test
    @DisplayName("Get beans using Class through DOM reader")
    public void testDOMGetBeanWithValueAndRefDependenciesUsingClass() {
        RentalService rentalService = domContext.getBean(RentalService.class);
        CustomerService customerService = domContext.getBean(CustomerService.class);
        TowingService towingService = domContext.getBean(TowingService.class);

        int expectedAmountOfCars = 26;
        boolean expectedCoverage = false;
        String expectedPhoneNumber = "(800)RENTALS";
        String expectedEmailAddress = "cs@rentals.com";

        assertNotNull(rentalService);
        assertNotNull(rentalService.getCustomerService());
        assertNotNull(customerService);
        assertNotNull(towingService);
        assertNotNull(towingService.getCustomerService());

        assertEquals(expectedAmountOfCars, rentalService.getNumberOfCarsAvailable());
        assertEquals(expectedCoverage, rentalService.isExtraCoverage());
        assertEquals(expectedPhoneNumber, customerService.getPhoneNumber());
        assertEquals(expectedEmailAddress, customerService.getEmail());

        assertSame(customerService, rentalService.getCustomerService());
        assertSame(customerService, towingService.getCustomerService());
    }

    @Test
    @DisplayName("Get beans using Class through SAX reader")
    public void testSAXGetBeanWithValueAndRefDependenciesUsingClass() {
        RentalService rentalService = saxContext.getBean(RentalService.class);
        CustomerService customerService = saxContext.getBean(CustomerService.class);
        TowingService towingService = saxContext.getBean(TowingService.class);

        int expectedAmountOfCars = 26;
        boolean expectedCoverage = false;
        String expectedPhoneNumber = "(800)RENTALS";
        String expectedEmailAddress = "cs@rentals.com";

        assertNotNull(rentalService);
        assertNotNull(rentalService.getCustomerService());
        assertNotNull(customerService);
        assertNotNull(towingService);
        assertNotNull(towingService.getCustomerService());

        assertEquals(expectedAmountOfCars, rentalService.getNumberOfCarsAvailable());
        assertEquals(expectedCoverage, rentalService.isExtraCoverage());
        assertEquals(expectedPhoneNumber, customerService.getPhoneNumber());
        assertEquals(expectedEmailAddress, customerService.getEmail());

        assertSame(customerService, rentalService.getCustomerService());
        assertSame(customerService, towingService.getCustomerService());
    }

    @Test
    @DisplayName("Get beans using ID through DOM reader")
    public void testDOMGetBeansWithValueAndRefDependenciesUsingId() {
        RentalService rentalService = (RentalService) domContext.getBean("rentalService");
        CustomerService customerService = (CustomerService) domContext.getBean("customerService");
        TowingService towingService = (TowingService) domContext.getBean("towingService");

        int expectedAmountOfCars = 26;
        boolean expectedCoverage = false;
        String expectedPhoneNumber = "(800)RENTALS";
        String expectedEmailAddress = "cs@rentals.com";

        assertNotNull(rentalService);
        assertNotNull(rentalService.getCustomerService());
        assertNotNull(customerService);
        assertNotNull(towingService);
        assertNotNull(towingService.getCustomerService());

        assertEquals(expectedAmountOfCars, rentalService.getNumberOfCarsAvailable());
        assertEquals(expectedCoverage, rentalService.isExtraCoverage());
        assertEquals(expectedPhoneNumber, customerService.getPhoneNumber());
        assertEquals(expectedEmailAddress, customerService.getEmail());

        assertSame(customerService, rentalService.getCustomerService());
        assertSame(customerService, towingService.getCustomerService());
    }

    @Test
    @DisplayName("Get beans using ID through SAX reader")
    public void testSAXGetBeansWithValueAndRefDependenciesUsingId() {
        RentalService rentalService = (RentalService) saxContext.getBean("rentalService");
        CustomerService customerService = (CustomerService) saxContext.getBean("customerService");
        TowingService towingService = (TowingService) saxContext.getBean("towingService");

        int expectedAmountOfCars = 26;
        boolean expectedCoverage = false;
        String expectedPhoneNumber = "(800)RENTALS";
        String expectedEmailAddress = "cs@rentals.com";

        assertNotNull(rentalService);
        assertNotNull(rentalService.getCustomerService());
        assertNotNull(customerService);
        assertNotNull(towingService);
        assertNotNull(towingService.getCustomerService());

        assertEquals(expectedAmountOfCars, rentalService.getNumberOfCarsAvailable());
        assertEquals(expectedCoverage, rentalService.isExtraCoverage());
        assertEquals(expectedPhoneNumber, customerService.getPhoneNumber());
        assertEquals(expectedEmailAddress, customerService.getEmail());

        assertSame(customerService, rentalService.getCustomerService());
        assertSame(customerService, towingService.getCustomerService());
    }

    @Test
    @DisplayName("Get beans using ID and Class through DOM reader")
    public void testDOMGetBeansWithValueAndRefDependenciesUsingIdAndClass() {
        RentalService rentalService = domContext.getBean("rentalService", RentalService.class);
        CustomerService customerService = domContext.getBean("customerService", CustomerService.class);
        TowingService towingService = domContext.getBean("towingService", TowingService.class);

        int expectedAmountOfCars = 26;
        boolean expectedCoverage = false;
        String expectedPhoneNumber = "(800)RENTALS";
        String expectedEmailAddress = "cs@rentals.com";

        assertNotNull(rentalService);
        assertNotNull(rentalService.getCustomerService());
        assertNotNull(customerService);
        assertNotNull(towingService);
        assertNotNull(towingService.getCustomerService());

        assertEquals(expectedAmountOfCars, rentalService.getNumberOfCarsAvailable());
        assertEquals(expectedCoverage, rentalService.isExtraCoverage());
        assertEquals(expectedPhoneNumber, customerService.getPhoneNumber());
        assertEquals(expectedEmailAddress, customerService.getEmail());

        assertSame(customerService, rentalService.getCustomerService());
        assertSame(customerService, towingService.getCustomerService());
    }

    @Test
    @DisplayName("Get beans using ID and Class through SAX reader")
    public void testSAXGetBeansWithValueAndRefDependenciesUsingIdAndClass() {
        RentalService rentalService = saxContext.getBean("rentalService", RentalService.class);
        CustomerService customerService = saxContext.getBean("customerService", CustomerService.class);
        TowingService towingService = saxContext.getBean("towingService", TowingService.class);

        int expectedAmountOfCars = 26;
        boolean expectedCoverage = false;
        String expectedPhoneNumber = "(800)RENTALS";
        String expectedEmailAddress = "cs@rentals.com";

        assertNotNull(rentalService);
        assertNotNull(rentalService.getCustomerService());
        assertNotNull(customerService);
        assertNotNull(towingService);
        assertNotNull(towingService.getCustomerService());

        assertEquals(expectedAmountOfCars, rentalService.getNumberOfCarsAvailable());
        assertEquals(expectedCoverage, rentalService.isExtraCoverage());
        assertEquals(expectedPhoneNumber, customerService.getPhoneNumber());
        assertEquals(expectedEmailAddress, customerService.getEmail());

        assertSame(customerService, rentalService.getCustomerService());
        assertSame(customerService, towingService.getCustomerService());
    }

    @Test
    @DisplayName("Getting the list of bean names through DOM reader")
    public void testDOMGetListOfBeans() {
        List<String> beans = domContext.getBeanNames();
        String expectedFirstBean = "rentalService";
        String expectedSecondBean = "customerService";
        String expectedThirdBean = "towingService";
        int expectedListSize = 3;

        assertNotNull(beans);
        assertEquals(expectedListSize, beans.size());
        assertEquals(expectedFirstBean, beans.get(0));
        assertEquals(expectedSecondBean, beans.get(1));
        assertEquals(expectedThirdBean, beans.get(2));
    }

    @Test
    @DisplayName("Getting a list of bean names through the SAX reader")
    public void testSAXGetListOfBeans() {
        List<String> beans = saxContext.getBeanNames();
        String expectedFirstBean = "rentalService";
        String expectedSecondBean = "customerService";
        String expectedThirdBean = "towingService";
        int expectedListSize = 3;

        assertNotNull(beans);
        assertEquals(expectedListSize, beans.size());
        assertEquals(expectedFirstBean, beans.get(0));
        assertEquals(expectedSecondBean, beans.get(1));
        assertEquals(expectedThirdBean, beans.get(2));
    }

    @Test
    @DisplayName("Get different beans from one class using ID through DOM reader")
    public void testDOMGetBeansUsingIdFromOneClass() {
        ClassPathApplicationContext domContext = new ClassPathApplicationContext(duplicateClassBeanContext);

        CustomerService customerService = (CustomerService) domContext.getBean("customerService");
        TowingService towingService = (TowingService) domContext.getBean("towingService");
        TowingService towingServiceEmergency = (TowingService) domContext.getBean("towingServiceEmergency");

        assertNotNull(towingService);
        assertNotNull(towingService.getCustomerService());
        assertSame(customerService, towingService.getCustomerService());

        int expectedPhoneNumber = 911;
        assertNotNull(towingServiceEmergency);
        assertEquals(expectedPhoneNumber, towingServiceEmergency.getEmergency());

    }

    @Test
    @DisplayName("Get different beans from one class using ID through SAX reader")
    public void testSAXGetBeansUsingIdFromOneClass() {
        ClassPathApplicationContext saxContext = new ClassPathApplicationContext
                (new SAXBeanDefinitionReader(duplicateClassBeanContext));

        CustomerService customerService = (CustomerService) saxContext.getBean("customerService");
        TowingService towingService = (TowingService) saxContext.getBean("towingService");
        TowingService towingServiceEmergency = (TowingService) saxContext.getBean("towingServiceEmergency");

        assertNotNull(towingService);
        assertNotNull(towingService.getCustomerService());
        assertSame(customerService, towingService.getCustomerService());

        int expectedPhoneNumber = 911;
        assertNotNull(towingServiceEmergency);
        assertEquals(expectedPhoneNumber, towingServiceEmergency.getEmergency());
    }

    @Test
    @DisplayName("Checking getBean  ID and Class parameters for empty or null")
    public void testDOMSAXParameterCheck() {
        assertThrows(ContainerException.class, () -> domContext.parameterCheck(""));
        assertThrows(ContainerException.class, () -> saxContext.parameterCheck(""));
        assertThrows(ContainerException.class, () -> domContext.parameterCheck((Class<?>) null));
        assertThrows(ContainerException.class, () -> saxContext.parameterCheck((Class<?>) null));
    }
}
