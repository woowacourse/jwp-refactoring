package kitchenpos.ui;

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
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MenuGroupService service;

    public MenuGroup menuGroup1 = new MenuGroup();
    public MenuGroup menuGroup2 = new MenuGroup();

    @BeforeEach
    void setUp() {
        menuGroup1.setId(1L);
        menuGroup1.setName("추천 메뉴");
        menuGroup2.setId(2L);
        menuGroup2.setName("사이드 메뉴");
    }

    @DisplayName("POST /api/menu-groups -> 메뉴 그룹을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천 메뉴");
        String content = objectMapper.writeValueAsString(menuGroup);

        MenuGroup testObject = new MenuGroup();
        testObject.setId(1L);
        testObject.setName("추천 메뉴");

        given(service.create(any(MenuGroup.class)))
                .willReturn(testObject);

        // when
        ResultActions actions = mvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(content));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menu-groups/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("추천 메뉴")))
                .andDo(print());
    }

    @DisplayName("GET /api/menu-groups -> 메뉴 그룹 전체를 조회한다.")
    @Test
    void list() throws Exception {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(menuGroup1, menuGroup2);

        given(service.list()).willReturn(menuGroups);

        // when
        ResultActions actions = mvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("추천 메뉴")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("사이드 메뉴")))
                .andDo(print());
    }
}