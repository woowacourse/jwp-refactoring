package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.common.TestObjectFactory;
import kitchenpos.domain.MenuGroup;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성 요청 테스트")
    @Test
    void create() throws Exception {
        MenuGroup menuGroup = TestObjectFactory.createMenuGroupDto(1L, "추천메뉴");

        given(menuGroupService.create(any())).willReturn(menuGroup);

        mockMvc.perform(post("/api/menu-groups")
                .content("{\n"
                        + "  \"name\": \"추천메뉴\"\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menu-groups/1"))
                .andExpect(jsonPath("$.id", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.name", Matchers.instanceOf(String.class)));
    }

    @DisplayName("메뉴 그룹 목록을 조회하는 요청 테스트")
    @Test
    void list() throws Exception {
        List<MenuGroup> menus = new ArrayList<>();
        menus.add(TestObjectFactory.createMenuGroupDto(null, "name1"));
        menus.add(TestObjectFactory.createMenuGroupDto(null, "name2"));
        given(menuGroupService.list()).willReturn(menus);

        mockMvc.perform(get("/api/menu-groups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }
}
