package com.cargo.customerMS;

import com.cargo.customerMS.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@ActiveProfiles("test")
class CustomerApiIntegrationTest {

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        mockMvc = webAppContextSetup(applicationContext).build();
    }

    @Test
    void createCustomerThenRetrieveItThroughApi() throws Exception {
        String response = mockMvc.perform(post("/api/customers/create")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"Robin\",\"age\":27}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Robin"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Matcher idMatcher = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(response);
        assertTrue(idMatcher.find(), "Create response should contain the generated customer ID");
        long id = Long.parseLong(idMatcher.group(1));

        mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Robin"))
                .andExpect(jsonPath("$.age").value(27));

        mockMvc.perform(get("/api/customers/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void retrieveUnknownCustomerReturnsNotFoundThroughApi() throws Exception {
        mockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }
}
