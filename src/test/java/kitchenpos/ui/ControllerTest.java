package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static kitchenpos.ui.OrderRestController.ORDER_API;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ControllerTest {
    private static final String ENCODING = "UTF-8";

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @BeforeEach
    protected void setUp(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter(ENCODING, true))
                .alwaysDo(print())
                .build();

        objectMapper = new ObjectMapper();
    }

    protected ResultActions requestWithPost(String uri, String body) throws Exception {
        return mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());
    }

    protected ResultActions requestWithGet(String uri) throws Exception {
        return mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    protected ResultActions requestWithPut(String uri, String body) throws Exception {
        return mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }
}
