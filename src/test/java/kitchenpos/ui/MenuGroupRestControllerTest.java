package kitchenpos.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
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
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다")
    void create() throws Exception {
        given(menuGroupService.create(any(MenuGroup.class)))
                .willReturn(createMenuGroup(1L, "추천메뉴"));
        byte[] content = objectMapper.writeValueAsBytes(createMenuGroupRequest("추천메뉴"));

        mockMvc.perform(post("/api/menu-groups")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("추천메뉴"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회한다")
    void list() throws Exception {
        List<MenuGroup> menuGroups = Arrays.asList(
                createMenuGroup(1L, "추천 메뉴"),
                createMenuGroup(2L, "맛있는 메뉴"),
                createMenuGroup(3L, "기가 막힌 메뉴")
        );
        given(menuGroupService.list()).willReturn(menuGroups);

        byte[] responseBody = mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        List<MenuGroup> results = objectMapper.readValue(responseBody, new TypeReference<List<MenuGroup>>() {
        });

        assertThat(results).usingFieldByFieldElementComparator().isEqualTo(menuGroups);
    }
}