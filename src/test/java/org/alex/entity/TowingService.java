package org.alex.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Setter
@Getter
public class TowingService {
    private CustomerService customerService;
    private int emergency;
}
