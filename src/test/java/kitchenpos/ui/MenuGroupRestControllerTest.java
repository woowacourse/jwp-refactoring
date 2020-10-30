package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    private static final String BASE_URL = "/api/menu-groups";

    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("새로운 메뉴 그룹을 생성한다.")
    @Test
    void createTest() throws Exception {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("추천메뉴");
        String content = objectMapper.writeValueAsString(menuGroupRequest);
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(1L, "추천메뉴");

        given(menuGroupService.create(any())).willReturn(menuGroupResponse);

        mockMvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is(menuGroupResponse.getName())));
    }

    @DisplayName("저장되어 있는 모든 메뉴 그룹 출력")
    @Test
    void listTest() throws Exception {
        List<MenuGroupResponse> menuGroupResponses = Arrays.asList(
                new MenuGroupResponse(1L, "추천메뉴"),
                new MenuGroupResponse(2L, "인기메뉴"),
                new MenuGroupResponse(3L, "한정메뉴")
        );

        given(menuGroupService.list()).willReturn(menuGroupResponses);

        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$.[0].name", Matchers.is("추천메뉴")));
    }
}
