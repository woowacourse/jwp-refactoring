package kitchenpos.ui;

import static org.hamcrest.core.StringContains.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    private final String BASE_URL = "/api/menus";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    MenuService menuService;
    private Menu menu;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .build();
        menu = new Menu();
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(1000));
    }

    @Test
    void create() throws Exception {
        String body = objectMapper.writeValueAsString(menu);

        given(menuService.create(any())).willReturn(menu);

        mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body)
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("menu")))
            .andExpect(content().string(containsString("1000")));
    }

    @Test
    void list() throws Exception {
        Menu menu2 = new Menu();
        menu2.setName("menu2");

        given(menuService.list()).willReturn(Arrays.asList(menu, menu2));

        mockMvc.perform(get(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("menu")))
            .andExpect(content().string(containsString("menu2")));
    }
}
