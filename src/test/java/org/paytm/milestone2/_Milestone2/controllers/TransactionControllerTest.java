package org.paytm.milestone2._Milestone2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.paytm.milestone2._Milestone2.DTO.Response.MessageResponse;
import org.paytm.milestone2._Milestone2.DTO.Response.SignInResponseBody;
import org.paytm.milestone2._Milestone2.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    String generateTokenUsingLogin(String signInRequestBody) throws Exception{

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signin")
                        .content(signInRequestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertNotNull(result);

        String body = result.getResponse().getContentAsString();
        SignInResponseBody signInResponseBody1 = objectMapper.readValue(body,SignInResponseBody.class);
        return signInResponseBody1.getJwt();

    }

    @Test
    @Order(1)
    @DisplayName("Create transaction test")
    void transactionP2P() throws Exception {

        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/signInRequestObject1.json")));
        String jwtForUser = generateTokenUsingLogin(signInRequestBody);
        String transactionCreateRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/transactionP2pRequestObject1.json")));

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwtForUser)
                        .content(transactionCreateRequestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String resultAsString = result.getResponse().getContentAsString();
        MessageResponse response = objectMapper.readValue(resultAsString,MessageResponse.class);

        assertNotNull(result);
        assertEquals("Transaction Successfull",response.getMessage());
    }

    @Test
    @Order(2)
    @DisplayName("View transaction by transaction Id")
    void viewTransactionById() throws Exception {

        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/signInRequestObject1.json")));
        String jwtForUser = generateTokenUsingLogin(signInRequestBody);
        int txnId = 36;

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/transaction")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwtForUser)
                        .param("txnId",String.valueOf(txnId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String resultAsString = result.getResponse().getContentAsString();
        Transaction response = objectMapper.readValue(resultAsString,Transaction.class);

        assertEquals(txnId,response.getTxnId());
        assertNotNull(result);
    }

    @Test
    @Order(3)
    @DisplayName("View transaction by user id")
    void viewTransactionByUserId() throws Exception {
        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/signInRequestObject1.json")));
        String jwtForUser = generateTokenUsingLogin(signInRequestBody);
        int userId = 32;
        int pageNo = 0;

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/transaction/"+userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwtForUser)
                        .param("pageNo",String.valueOf(pageNo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        assertEquals(200,result.getResponse().getStatus());
        assertNotNull(result);

    }
}