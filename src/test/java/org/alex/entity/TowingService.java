package org.alex.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TowingService {
    private CustomerService customerService;
    private int emergency;
}
