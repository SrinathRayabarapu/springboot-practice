package com.boot.jpa.transactional.service;

import com.boot.jpa.transactional.dto.FlightBookingAcknowledgement;
import com.boot.jpa.transactional.dto.FlightBookingRequest;
import com.boot.jpa.transactional.entity.PassengerInfo;
import com.boot.jpa.transactional.entity.PaymentInfo;
import com.boot.jpa.transactional.repository.PassengerInfoRepository;
import com.boot.jpa.transactional.repository.PaymentInfoRepository;
import com.boot.jpa.transactional.util.PaymentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class FlightBookingService {

    @Autowired
    private PassengerInfoRepository passengerInfoRepository;

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;


    // @Transactional enables to treat the method as a single transaction with atomic behaviour as in ACID.
    @Transactional
    public FlightBookingAcknowledgement bookFlightTicket(FlightBookingRequest request) {

        PassengerInfo passengerInfo = request.getPassengerInfo();
        passengerInfoRepository.save(passengerInfo);

        PaymentInfo paymentInfo = request.getPaymentInfo();

        PaymentUtils.validateCreditLimit(paymentInfo.getAccountNo(), passengerInfo.getFare());

        paymentInfo.setPassengerId(passengerInfo.getPId());
        paymentInfo.setAmount(passengerInfo.getFare());

        paymentInfoRepository.save(paymentInfo);

        return new FlightBookingAcknowledgement("SUCCESS", passengerInfo.getFare(), UUID.randomUUID().toString().split("-")[0], passengerInfo);
    }

}
