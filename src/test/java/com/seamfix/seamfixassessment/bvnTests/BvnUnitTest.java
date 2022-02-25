package com.seamfix.seamfixassessment.bvnTests;

import com.seamfix.seamfixassessment.payload.BvnRequest;
import com.seamfix.seamfixassessment.payload.BvnResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.seamfix.seamfixassessment.utils.BvnUtils;

@SpringBootTest
public class BvnUnitTest {

    BvnUtils utils = null;

    @BeforeEach
    void setup() {
        utils = new BvnUtils();
    }

    @Test
    public void EmptyBvnValueTest(){

        BvnRequest bvnRequest = new BvnRequest("");

        BvnResponse bvnResponse = utils.validateRequest(bvnRequest);

        assertEquals("400", bvnResponse.getCode());
        assertEquals("One or more of your request parameters failed validation. Please retry", bvnResponse.getMessage());
    }

    @Test
    public void inValidBvnLengthTest(){

        BvnRequest bvnRequest = new BvnRequest("123456");

        BvnResponse bvnResponse = utils.validateRequest(bvnRequest);

        assertEquals("02", bvnResponse.getCode());
        assertEquals("The searched BVN is invalid", bvnResponse.getMessage());
    }

    @Test
    public void specialCharsInBvnTest(){

        BvnRequest bvnRequest = new BvnRequest("&234567890*");

        BvnResponse bvnResponse = utils.validateRequest(bvnRequest);

        assertEquals("400", bvnResponse.getCode());
        assertEquals("The searched BVN is invalid", bvnResponse.getMessage());
    }

    @Test
    public void validBvnTest(){
        BvnRequest bvnRequest = new BvnRequest("12345678901");

        BvnResponse bvnResponse = utils.validateRequest(bvnRequest);

        assertEquals("00", bvnResponse.getCode());
    }

}
