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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
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
    @DisplayName("Create transaction test")
    @Sql(statements = "insert into user (user_id,user_name,first_name,last_name,email_id,mobile_number,address_1,password) values (1000,\"Sd099\",\"Son\",\"Doe\",\"sondoe@gmail.com\",\"9876543210\",\"Time square, NY, USA\",\"$2a$10$hph6xo16D8ED1ZOHcMo7wuvrCH5ebxiVyTtPFGsHaZmIgAHWa55C2\")\n", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "insert into user (user_id,user_name,first_name,last_name,email_id,mobile_number,address_1,password) values (2000,\"Jd099\",\"John\",\"Doe\",\"johndoe@gmail.com\",\"0123456789\",\"Time square, NY, USA\",\"$2a$10$3IT1NBW1r60UtCPOXGlwweqmHJDwy3bsr4cuO4XkNnUguTtYgA8va\")\n", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "insert into wallet (wallet_id,mobile_number,current_balance) values (1000,\"9876543210\",5000.0)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "insert into wallet (wallet_id,mobile_number,current_balance) values (2000,\"0123456789\",0.0)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void transactionP2P() throws Exception {

        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/jsonObject/userObjects/signInRequestObject1.json")));
        String jwtForUser = generateTokenUsingLogin(signInRequestBody);
        String transactionCreateRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/jsonObject/transactionObjects/transactionP2pRequestObject1.json")));

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
    @DisplayName("View transaction by transaction Id")
    @Sql(statements = "insert into user (user_id,user_name,first_name,last_name,email_id,mobile_number,address_1,password) values (3000,\"Ss099\",\"Steve\",\"Smith\",\"stevesmith@gmail.com\",\"5432109876\",\"Time square, NY, USA\",\"$2a$10$7uLjRL1XR99ovnrin52/puncFBPdMKv79FQ8CZVi8UfGqcxv6Yfge\")\n", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "insert into transaction (txn_id,amount,payee_mobile_number,payer_mobile_number,status,timestamp) values (3000,200.0,\"0123456789\",\"5432109876\",\"SUCCESS\",\"1998-01-02 00:00:00.000\")\n", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM user", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM transaction", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void viewTransactionById() throws Exception {

        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/jsonObject/userObjects/signInRequestObject3.json")));
        String jwtForUser = generateTokenUsingLogin(signInRequestBody);
        int txnId = 3000;

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
    @DisplayName("View transaction by user id")
    @Sql(statements = "insert into user (user_id,user_name,first_name,last_name,email_id,mobile_number,address_1,password) values (3000,\"Ss099\",\"Steve\",\"Smith\",\"stevesmith@gmail.com\",\"5432109876\",\"Time square, NY, USA\",\"$2a$10$7uLjRL1XR99ovnrin52/puncFBPdMKv79FQ8CZVi8UfGqcxv6Yfge\")\n", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "insert into transaction (txn_id,amount,payee_mobile_number,payer_mobile_number,status,timestamp) values (3000,200.0,\"0123456789\",\"5432109876\",\"SUCCESS\",\"1998-01-02 00:00:00.000\")\n", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void viewTransactionByUserId() throws Exception {
        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/jsonObject/userObjects/signInRequestObject3.json")));
        String jwtForUser = generateTokenUsingLogin(signInRequestBody);
        int userId = 3000;
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