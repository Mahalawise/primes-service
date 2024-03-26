package edu.iu.mamawise.primesservice.service;

import edu.iu.mamawise.primesservice.model.Customer;

import java.io.IOException;

public interface IAuthenticationService {
    boolean register(Customer customer) throws IOException;
}