package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    private static final String BASE_URL = "/api/menus";

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("새로운 메뉴 등록")
    @Test
    void createTest() throws Exception {
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2L);
        MenuRequest menuRequest = new MenuRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1L,
                Collections.singletonList(menuProductRequest)
        );
        String content = objectMapper.writeValueAsString(menuRequest);
        MenuResponse menuResponse = new MenuResponse(1L);

        given(menuService.createWithRequest(any())).willReturn(menuResponse);

        mockMvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }

    @DisplayName("저장되어 있는 모든 메뉴 출력")
    @Test
    void listTest() throws Exception {
        List<MenuResponse> menuResponses = Arrays.asList(
                new MenuResponse(1L),
                new MenuResponse(2L),
                new MenuResponse(3L)
        );

        given(menuService.listWithResponse()).willReturn(menuResponses);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.[0].id", Matchers.is(1)));
    }
}
