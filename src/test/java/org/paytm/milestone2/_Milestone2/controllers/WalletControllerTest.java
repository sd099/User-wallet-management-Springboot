package org.paytm.milestone2._Milestone2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.paytm.milestone2._Milestone2.DTO.Response.MessageResponse;
import org.paytm.milestone2._Milestone2.DTO.Response.SignInResponseBody;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {

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
    @DisplayName("Create wallet tests")
    @Sql(statements = "insert into user (user_id,user_name,first_name,last_name,email_id,mobile_number,address_1,password) values (2000,\"Jd099\",\"John\",\"Doe\",\"johndoe@gmail.com\",\"0123456789\",\"Time square, NY, USA\",\"$2a$10$3IT1NBW1r60UtCPOXGlwweqmHJDwy3bsr4cuO4XkNnUguTtYgA8va\")\n", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createWallet() throws Exception{

        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/jsonObject/userObjects/signInRequestObject2.json")));
        String jwtForUser = generateTokenUsingLogin(signInRequestBody);
        String createWalletRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/jsonObject/walletObjects/createWalletRequestObject2.json")));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/wallet")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwtForUser)
                        .content(createWalletRequestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String resultAsString= result.getResponse().getContentAsString();
        MessageResponse response = objectMapper.readValue(resultAsString,MessageResponse.class);

        assertNotNull(result);
        assertEquals("Wallet Created Successfully!!",response.getMessage());

    }

    @Test
    @DisplayName("Add money into wallet tests")
    @Sql(statements = "insert into user (user_id,user_name,first_name,last_name,email_id,mobile_number,address_1,password) values (1000,\"Sd099\",\"Son\",\"Doe\",\"sondoe@gmail.com\",\"9876543210\",\"Time square, NY, USA\",\"$2a$10$hph6xo16D8ED1ZOHcMo7wuvrCH5ebxiVyTtPFGsHaZmIgAHWa55C2\")\n", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "insert into wallet (wallet_id,mobile_number,current_balance) values (1000,\"9876543210\",0.0)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void addMoneyIntoWallet() throws Exception {

        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/jsonObject/userObjects/signInRequestObject1.json")));
        String jwtForUser = generateTokenUsingLogin(signInRequestBody);
        String addMoneyRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/jsonObject/walletObjects/addMoneyObject1.json")));

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.put("/wallet/addmoney")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwtForUser)
                        .content(addMoneyRequestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertNotNull(result);
    }
}