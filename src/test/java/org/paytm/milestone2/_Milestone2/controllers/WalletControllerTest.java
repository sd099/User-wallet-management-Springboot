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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    void createWallet() throws Exception{

        String signInRequestBody1 = new String(Files.readAllBytes(Paths.get("src/test/resources/signInRequestObject1.json")));
        String jwtForUser1 = generateTokenUsingLogin(signInRequestBody1);
        String createWalletRequestBody1 = new String(Files.readAllBytes(Paths.get("src/test/resources/createWalletRequestObject1.json")));

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/wallet")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwtForUser1)
                        .content(createWalletRequestBody1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String resultAsString1 = result1.getResponse().getContentAsString();
        MessageResponse response1 = objectMapper.readValue(resultAsString1,MessageResponse.class);

        assertNotNull(result1);
        assertEquals("Wallet Created Successfully!!",response1.getMessage());


        String signInRequestBody2 = new String(Files.readAllBytes(Paths.get("src/test/resources/signInRequestObject2.json")));
        String jwtForUser2 = generateTokenUsingLogin(signInRequestBody2);
        String createWalletRequestBody2 = new String(Files.readAllBytes(Paths.get("src/test/resources/createWalletRequestObject2.json")));

        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post("/wallet")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwtForUser2)
                        .content(createWalletRequestBody2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String resultAsString2 = result2.getResponse().getContentAsString();
        MessageResponse response2 = objectMapper.readValue(resultAsString2,MessageResponse.class);

        assertNotNull(result2);
        assertEquals("Wallet Created Successfully!!",response2.getMessage());
    }

    @Test
    @DisplayName("Add money into wallet tests")
    @Order(2)
    void addMoneyIntoWallet() throws Exception {

        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/signInRequestObject1.json")));
        String jwtForUser = generateTokenUsingLogin(signInRequestBody);
        String addMoneyRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/addMoneyObject1.json")));

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.put("/wallet/addmoney")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwtForUser)
                        .content(addMoneyRequestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertNotNull(result);
    }
}