package org.alex.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Setter
@Getter
public class RentalService {
    private CustomerService customerService;
    private boolean extraCoverage;
    private int numberOfCarsAvailable;
}