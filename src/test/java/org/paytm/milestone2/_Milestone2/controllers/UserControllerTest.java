package org.paytm.milestone2._Milestone2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.paytm.milestone2._Milestone2.DTO.Response.MessageResponse;
import org.paytm.milestone2._Milestone2.DTO.Response.SignInResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Signup users")
    void signUpUser() throws Exception {

        String user = new String(Files.readAllBytes(Paths.get("src/test/resources/jsonObject/userObjects/userObject2.json")));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String resultAsString = result.getResponse().getContentAsString();
        MessageResponse response = objectMapper.readValue(resultAsString,MessageResponse.class);

        assertNotNull(result);
        assertEquals("Signup Complete",response.getMessage());

    }

    @Test
    @DisplayName("SignIn Users")
    @Sql(statements = "insert into user (user_id,user_name,first_name,last_name,email_id,mobile_number,address_1,password) values (1000,\"Sd099\",\"Son\",\"Doe\",\"sondoe@gmail.com\",\"9876543210\",\"Time square, NY, USA\",\"$2a$10$hph6xo16D8ED1ZOHcMo7wuvrCH5ebxiVyTtPFGsHaZmIgAHWa55C2\")\n",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void signInUser() throws Exception {
        String signInRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/jsonObject/userObjects/signInRequestObject1.json")));

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





//spring.datasource.url = jdbc:h2:mem:testdb
//        spring.datasource.driverClassName=org.h2.Driver
//        spring.datasource.username =sa
//        spring.datasource.password =12345678
//        spring.jpa.database-platform =org.hibernate.dialect.H2Dialect
//        spring.sql.init.platform=h2
//        spring.h2.console.enabled=true

