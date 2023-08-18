package org.alex.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RentalService {
    private CustomerService customerService;
    private boolean extraCoverage;
    private int numberOfCarsAvailable;
}