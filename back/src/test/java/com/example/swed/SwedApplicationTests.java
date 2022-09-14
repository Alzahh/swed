package com.example.swed;

import com.example.swed.payload.request.CurrencyExchangeRequest;
import com.example.swed.payload.request.LoginRequest;
import com.example.swed.payload.request.SignupRequest;
import com.example.swed.payload.request.UserDataUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@Transactional
@ContextConfiguration
class SwedApplicationTests {


    @Autowired
    private MockMvc mockMvc;


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static final SignupRequest sampleSignupRequest = new SignupRequest("Bob", "Bob", "bob@test.bob", null, null, "123");

    @Test
    public void createUserAndDisableTest() throws Exception {

//        create user

        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(asJsonString(sampleSignupRequest))).andExpect(status().isOk());


        //        login

        LoginRequest loginRequest = new LoginRequest(sampleSignupRequest.getEmail(), sampleSignupRequest.getPassword());

        MvcResult result = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest))).andExpect(status().isOk()).andExpect(jsonPath("token").isNotEmpty()).andReturn();
//        take token

        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(json, Map.class);

//        disable user


        mockMvc.perform(post("/disable").contentType(MediaType.APPLICATION_JSON)

                .header(HttpHeaders.AUTHORIZATION, "Bearer " + map.get("token")).content(asJsonString(loginRequest))).andExpect(status().isOk());

//        try to log in again


        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest))).andExpect(status().is4xxClientError());

//        signup with same email


        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(asJsonString(sampleSignupRequest))).andExpect(status().isOk());


    }


    @Test
    public void createUserAndEditTest() throws Exception {

//        create user


        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(asJsonString(sampleSignupRequest))).andExpect(status().isOk());

//        login

        LoginRequest loginRequest = new LoginRequest(sampleSignupRequest.getEmail(), sampleSignupRequest.getPassword());


        MvcResult result = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest))).andExpect(status().isOk()).andExpect(jsonPath("token").isNotEmpty()).andReturn();

//        take token

        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(json, Map.class);


//        change username

        UserDataUpdateRequest request = new UserDataUpdateRequest("bob@test.bob", "newName", "newLastName", null, null, null, null);

        MvcResult editResult = mockMvc.perform(post("/edit").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + map.get("token")).content(asJsonString(request))).andExpect(status().isOk()).andExpect(jsonPath("firstName").isNotEmpty()).andReturn();

        String editJson = editResult.getResponse().getContentAsString();
        Map userMap = mapper.readValue(editJson, Map.class);


        assert userMap.get("firstName").equals("newName");

    }


    @Test
    public void adminCanSeeUsersAndEditTest() throws Exception {

        // create test non-admin user


        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(asJsonString(sampleSignupRequest))).andExpect(status().isOk());


        LoginRequest loginRequest = new LoginRequest("admin@admin.ee", "admin");

        MvcResult loginResult = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest))).andExpect(status().isOk()).andReturn();

        //        take admin token

        String json = loginResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(json, Map.class);

//        request users data

        MvcResult usersDataResult = mockMvc.perform(get("/admin/users").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + map.get("token"))).andExpect(status().isOk()).andExpect(jsonPath("users").isNotEmpty()).andReturn();


        String users = usersDataResult.getResponse().getContentAsString();
        ArrayList<HashMap> usersList = (ArrayList) mapper.readValue(users, Map.class).get("users");

//        admin can see information about himself and bob
        assert usersList.size() == 2;
        HashMap userBeforeChanges = usersList.stream().filter(u -> u.get("email").equals("bob@test.bob")).findAny().orElse(null);

        //        edit user

        UserDataUpdateRequest request = new UserDataUpdateRequest("bob@test.bob", "NewName", null, null, null, null, null);

        usersDataResult = mockMvc.perform(post("/admin/users/edit").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + map.get("token")).content(asJsonString(request))).andExpect(status().isOk()).andReturn();


        String user = usersDataResult.getResponse().getContentAsString();
        Map userAfterChanges = mapper.readValue(user, Map.class);


        assert userBeforeChanges.get("firstName") != userAfterChanges.get("firstname");
        assert userAfterChanges.get("firstName").equals("NewName");

    }

    @Test
    public void accessAdminWithoutProperRoleTest() throws Exception {
//        create user


        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(asJsonString(sampleSignupRequest))).andExpect(status().isOk());

//        login

        LoginRequest loginRequest = new LoginRequest(sampleSignupRequest.getEmail(), sampleSignupRequest.getPassword());


        MvcResult result = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest))).andExpect(status().isOk()).andExpect(jsonPath("token").isNotEmpty()).andReturn();

//        take token

        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(json, Map.class);

        mockMvc.perform(get("/admin/users").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + map.get("token"))).andExpect(status().is4xxClientError());

    }

    @Test
    public void currencyExchangeTest() throws Exception {
//        create user


        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(asJsonString(sampleSignupRequest))).andExpect(status().isOk());

//        login

        LoginRequest loginRequest = new LoginRequest(sampleSignupRequest.getEmail(), sampleSignupRequest.getPassword());


        MvcResult result = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest))).andExpect(status().isOk()).andExpect(jsonPath("token").isNotEmpty()).andReturn();

//        take token

        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(json, Map.class);

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("EUR", "USD", 100.0);

//        cannot access without token
        mockMvc.perform(post("/currency").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request))).andExpect(status().is4xxClientError()).andReturn();

//        with token
        mockMvc.perform(post("/currency").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + map.get("token")).content(asJsonString(request))).andExpect(status().isOk()).andExpect(jsonPath("newAmount").isNotEmpty());

    }

    @Test
    public void loginWithWrongCredentialsTest() throws Exception {
        //        create user

        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(asJsonString(sampleSignupRequest))).andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest("randomMail", sampleSignupRequest.getPassword());


        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest))).andExpect(status().is4xxClientError());

        loginRequest = new LoginRequest(sampleSignupRequest.getEmail(), "RandomPass");


        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest))).andExpect(status().is4xxClientError());
    }


}
