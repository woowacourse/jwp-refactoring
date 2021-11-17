package kitchenpos.ui;

import kitchenpos.MenuFixture;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.menugroup.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.MenuFixture.createMenuGroup1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends ControllerTest {

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        Long menuGroupId = 1L;
        MenuGroup menuGroup = MenuFixture.createMenuGroup1();
        MenuGroup savedMenuGroup = createMenuGroup1(menuGroupId);

        when(menuGroupService.create(any())).thenReturn(savedMenuGroup);

        mockMvc.perform(post("/api/menu-groups")
                .content(objectMapper.writeValueAsString(menuGroup))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menu-groups/" + menuGroupId))
                .andExpect(content().json(objectMapper.writeValueAsString(savedMenuGroup)));
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<MenuGroup> menuGroups = Arrays.asList(createMenuGroup1(1L), createMenuGroup1(2L));
        when(menuGroupService.list()).thenReturn(menuGroups);

        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(menuGroups)));
    }
}
