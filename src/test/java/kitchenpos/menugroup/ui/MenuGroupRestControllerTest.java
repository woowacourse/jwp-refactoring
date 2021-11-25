package kitchenpos.menugroup.ui;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.builder.MenuGroupBuilder;
import kitchenpos.ui.BaseWebMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MenuGroupRestControllerTest extends BaseWebMvcTest {

    MenuGroup menuGroup1;
    MenuGroup menuGroup2;

    @BeforeEach
    void setUp() {
        this.menuGroup1 = new MenuGroupBuilder()
                .id(1L)
                .name("추천 메뉴")
                .build();
        this.menuGroup2 = new MenuGroupBuilder()
                .id(2L)
                .name("사이드 메뉴")
                .build();
        ;
    }

    @DisplayName("POST /api/menu-groups -> 메뉴 그룹을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        MenuGroup requestMenuGroup = new MenuGroupBuilder()
                .id(null)
                .name("추천 메뉴")
                .build();
        String content = super.parseJson(requestMenuGroup);

        given(menuGroupService.create(any(MenuGroup.class)))
                .willReturn(menuGroup1);

        // when
        ResultActions actions = mvc.perform(postRequest("/api/menu-groups", content));

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

        given(menuGroupService.list()).willReturn(menuGroups);

        // when
        ResultActions actions = mvc.perform(getRequest("/api/menu-groups"));

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