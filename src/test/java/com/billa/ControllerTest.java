package com.billa;

import com.billa.controllers.FlywayController;
import com.billa.repository.FlywayRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlywayController.class)
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class MockConfig {

        @Bean
        FlywayRepository repository() {
            return Mockito.mock(FlywayRepository.class);
        }
    }

    @Test
    void testShowEndpoint() throws Exception {
        mockMvc.perform(get("/v1"))   // since @GetMapping("") → root of controller’s @RequestMapping
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Application")); // ✅ assert response body
    }

    @Test
    void testShowEndpoint2() throws Exception {

    }
}
