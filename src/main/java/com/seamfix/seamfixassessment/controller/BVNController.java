package com.seamfix.seamfixassessment.controller;

import com.seamfix.seamfixassessment.payload.BvnRequest;
import com.seamfix.seamfixassessment.payload.BvnResponse;
import com.seamfix.seamfixassessment.utils.BvnUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping
@RestController
public class BVNController {

    @PostMapping(value="/bv-service/svalidate/wrapper", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public BvnResponse validate(@RequestBody BvnRequest request) {
        BvnUtils bvnUtils = new BvnUtils();
        BvnResponse validationResponse = bvnUtils.validateRequest(request);

        if (!validationResponse.getCode().equals("00")){
            return validationResponse;
        }

        if (request.getBvn().equals("12345678901")){
            validationResponse.setBvn(request.getBvn());
            validationResponse.setImageDetail("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCAuLi4=");
            validationResponse.setBasicDetail("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCAuLi4=");
            validationResponse.setMessage("Success");
            validationResponse.setCode("00");
        } else {
            validationResponse.setBvn(request.getBvn());
            validationResponse.setMessage("The searched BVN does not exist");
            validationResponse.setCode("01");
        }


        return validationResponse;
    }

}
