package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.Customer;
import org.springframework.stereotype.Service;

/**
 * Service for the {@link Customer} object to make interactions with the database.
 */
@Service
public class CustomerService extends AbstractDatabaseService<Customer, CustomerRepository>
{

    /**
     * The constructor.
     *
     * @param customerRepository The {@link CustomerRepository}
     */
    public CustomerService( CustomerRepository customerRepository )
    {
        super( customerRepository );
    }

}
