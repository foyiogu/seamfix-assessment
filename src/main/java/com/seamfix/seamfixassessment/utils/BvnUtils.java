package com.seamfix.seamfixassessment.utils;

import com.seamfix.seamfixassessment.payload.BvnRequest;
import com.seamfix.seamfixassessment.payload.BvnResponse;


public class BvnUtils {

    public BvnResponse validateRequest(BvnRequest validationRequest) {
        //aim to detect null
        //to detect invalid confirmationCode
        //to detect invalid phoneNumber
        String bvn = validationRequest.getBvn().trim();

        if (bvn.isEmpty()){
            BvnResponse validationResponse = new BvnResponse();
            validationResponse.setCode("400");
            validationResponse.setMessage("One or more of your request parameters failed validation. Please retry");
            validationResponse.setBvn(bvn);
            return validationResponse;
        }

        if (bvn.length() != 11) {
            System.out.println("Wrong length");
            BvnResponse validationResponse = new BvnResponse();
            validationResponse.setCode("02");
            validationResponse.setMessage("The searched BVN is invalid");
            validationResponse.setBvn(bvn);
            return validationResponse;
        }

        if (!bvn.matches("^[0-9]+")) {
            System.out.println("contains non number");
            BvnResponse validationResponse = new BvnResponse();
            validationResponse.setCode("400");
            validationResponse.setMessage("The searched BVN is invalid");
            validationResponse.setBvn(bvn);
            return validationResponse;
        }


        BvnResponse validationResponse = new BvnResponse();
        validationResponse.setCode("00");
        return validationResponse;
    }


}
