package org.paytm.milestone2._Milestone2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.paytm.milestone2._Milestone2.DTO.Response.MessageResponse;
import org.paytm.milestone2._Milestone2.DTO.Response.SignInResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    @DisplayName("Signup users")
    void signUpUser() throws Exception {

        String user1 = new String(Files.readAllBytes(Paths.get("src/test/resources/userObject1.json")));
        String user2 = new String(Files.readAllBytes(Paths.get("src/test/resources/userObject2.json")));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .content(user1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String resultAsString = result.getResponse().getContentAsString();
        MessageResponse response = objectMapper.readValue(resultAsString,MessageResponse.class);

        assertNotNull(result);
        assertEquals("Signup Complete",response.getMessage());

        result = mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .content(user2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        resultAsString = result.getResponse().getContentAsString();
        response = objectMapper.readValue(resultAsString,MessageResponse.class);

        assertNotNull(result);
        assertEquals("Signup Complete",response.getMessage());
    }

    @Test
    @Order(2)
    @DisplayName("SignIn Users")
    void signInUser() throws Exception {
        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/signInRequestObject1.json")));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signin")
                        .content(signInRequestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String resultAsString = result.getResponse().getContentAsString();
        SignInResponseBody response = objectMapper.readValue(resultAsString,SignInResponseBody.class);

        assertNotNull(result);
        assertNotNull(response.getJwt());

    }
}