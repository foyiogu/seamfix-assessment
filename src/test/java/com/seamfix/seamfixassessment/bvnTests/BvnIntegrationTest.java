package com.seamfix.seamfixassessment.bvnTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.seamfix.seamfixassessment.payload.BvnRequest;
import com.seamfix.seamfixassessment.payload.BvnResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class BvnIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Nested
    public class ValidBvnValueTestCases {
        String requestJson = "";
        BvnRequest bvnRequest = null;

        @BeforeEach
        void setup() throws JsonProcessingException {
            bvnRequest = new BvnRequest("12345678901");

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            requestJson = ow.writeValueAsString(bvnRequest);
        }

        @Test
        public void messageMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Success"))
                    .andReturn();

        }

        @Test
        public void codeMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("00"))
                    .andReturn();

        }

        @Test
        public void bvnMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.bvn").value(bvnRequest.getBvn()))
                    .andReturn();

        }

        @Test
        public void Base64MatchTest() throws Exception {

            MvcResult mvcResult = mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andDo(print()).andExpect(status().isOk())
                    .andReturn();

            String result = mvcResult.getResponse().getContentAsString();

            ObjectMapper obj = new ObjectMapper();
            BvnResponse response = obj.readValue(result, BvnResponse.class);

            assertEquals(bvnRequest.getBvn(), response.getBvn());
            assertNotNull(response.getBasicDetail());
            assertNotNull(response.getImageDetail());
            assertTrue(response.getBasicDetail().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$"));
            assertTrue(response.getImageDetail().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$"));

        }

        @Test
        public void responseTimeTest() throws Exception {

            long startTime = System.currentTimeMillis();

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Success"))
                    .andExpect(jsonPath("$.code").value("00"))
                    .andExpect(jsonPath("$.bvn").value(bvnRequest.getBvn()))
                    .andReturn();

            long endTime = System.currentTimeMillis();

            assertTrue(endTime - startTime < 5000);

        }
    }

    @Nested
    public class EmptyBvnValueTestCases {
        String requestJson = "";
        BvnRequest bvnRequest = null;

        @BeforeEach
        void setup() throws JsonProcessingException {
            bvnRequest = new BvnRequest("");

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            requestJson = ow.writeValueAsString(bvnRequest);
        }

        @Test
        public void messageMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("One or more of your request parameters failed validation. Please retry"))
                    .andReturn();

        }

        @Test
        public void codeMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andReturn();

        }

        @Test
        public void responseTimeTest() throws Exception {

            long startTime = System.currentTimeMillis();

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("One or more of your request parameters failed validation. Please retry"))
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.bvn").value(bvnRequest.getBvn()))
                    .andReturn();

            long endTime = System.currentTimeMillis();

            assertTrue(endTime - startTime < 1000);

        }
    }

    @Nested
    public class InvalidBvnLengthTestCases{
        String requestJson = "";
        BvnRequest bvnRequest = null;

        @BeforeEach
        void setup() throws JsonProcessingException {
            bvnRequest = new BvnRequest("1234567890112323");

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            requestJson = ow.writeValueAsString(bvnRequest);
        }

        @Test
        public void messageMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("The searched BVN is invalid"))
                    .andReturn();

        }

        @Test
        public void codeMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("02"))
                    .andReturn();

        }

        @Test
        public void bvnMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.bvn").value(bvnRequest.getBvn()))
                    .andReturn();

        }

        @Test
        public void responseTimeTest() throws Exception {

            long startTime = System.currentTimeMillis();

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("The searched BVN is invalid"))
                    .andExpect(jsonPath("$.code").value("02"))
                    .andExpect(jsonPath("$.bvn").value(bvnRequest.getBvn()))
                    .andReturn();

            long endTime = System.currentTimeMillis();

            assertTrue(endTime - startTime < 1000);

        }
    }

    @Nested
    public class BvnContainsNonDigitTestCase {
        String requestJson = "";
        BvnRequest bvnRequest = null;

        @BeforeEach
        void setup() throws JsonProcessingException {
            bvnRequest = new BvnRequest("!234@678#00");

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            requestJson = ow.writeValueAsString(bvnRequest);
        }

        @Test
        public void messageMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("The searched BVN is invalid"))
                    .andReturn();

        }

        @Test
        public void codeMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andReturn();

        }

        @Test
        public void bvnMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.bvn").value(bvnRequest.getBvn()))
                    .andReturn();

        }

        @Test
        public void responseTimeTest() throws Exception {

            long startTime = System.currentTimeMillis();

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("The searched BVN is invalid"))
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.bvn").value(bvnRequest.getBvn()))
                    .andReturn();

            long endTime = System.currentTimeMillis();

            assertTrue(endTime - startTime < 1000);

        }
    }

    @Nested
    public class InvalidBvnTestCases {
        String requestJson = "";
        BvnRequest bvnRequest = null;

        @BeforeEach
        void setup() throws JsonProcessingException {
            bvnRequest = new BvnRequest("12345678900");

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            requestJson = ow.writeValueAsString(bvnRequest);
        }

        @Test
        public void messageMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("The searched BVN does not exist"))
                    .andReturn();

        }

        @Test
        public void codeMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("01"))
                    .andReturn();

        }

        @Test
        public void bvnMatchTest() throws Exception {

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.bvn").value(bvnRequest.getBvn()))
                    .andReturn();

        }

        @Test
        public void responseTimeTest() throws Exception {

            long startTime = System.currentTimeMillis();

            mockMvc.perform(post("/bv-service/svalidate/wrapper").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("The searched BVN does not exist"))
                    .andExpect(jsonPath("$.code").value("01"))
                    .andExpect(jsonPath("$.bvn").value(bvnRequest.getBvn()))
                    .andReturn();

            long endTime = System.currentTimeMillis();

            assertTrue(endTime - startTime < 1000);

        }

    }
}
