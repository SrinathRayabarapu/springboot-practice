package com.boot.jpa.transactional.dto;

import com.boot.jpa.transactional.entity.PassengerInfo;
import com.boot.jpa.transactional.entity.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightBookingRequest {

    private PassengerInfo passengerInfo;

    private PaymentInfo paymentInfo;

}
