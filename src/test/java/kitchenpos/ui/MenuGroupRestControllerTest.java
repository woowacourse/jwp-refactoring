package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static kitchenpos.MenuFixture.MENU_GROUP_NAME1;
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
        final Long menuGroupId = 1L;
        final MenuGroupRequest request = new MenuGroupRequest(MENU_GROUP_NAME1);
        final MenuGroupResponse response = new MenuGroupResponse(menuGroupId, MENU_GROUP_NAME1);

        when(menuGroupService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/menu-groups")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menu-groups/" + menuGroupId))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        final List<MenuGroupResponse> response = Collections.singletonList(new MenuGroupResponse(1L, MENU_GROUP_NAME1));

        when(menuGroupService.list()).thenReturn(response);

        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
