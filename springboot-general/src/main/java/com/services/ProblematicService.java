package com.services;

import org.springframework.stereotype.Service;

@Service
public class ProblematicService {

    public void raiseNPE(){
        throw new NullPointerException();
    }

}
