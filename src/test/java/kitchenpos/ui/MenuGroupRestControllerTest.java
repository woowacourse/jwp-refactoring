package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;

class MenuGroupRestControllerTest extends ControllerTest {

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() throws Exception {
        // given
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(1L, "신메뉴");
        given(menuGroupService.create(any(MenuGroupRequest.class)))
            .willReturn(menuGroupResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(
                    new MenuGroupRequest("신메뉴")
                )
            ));

        // then
        result.andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/menu-groups/1"))
            .andExpect(content().json(toJson(menuGroupResponse)));
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        List<MenuGroupResponse> responses = List.of(
            new MenuGroupResponse(1L, "신메뉴"),
            new MenuGroupResponse(2L, "세트 메뉴")
        );
        given(menuGroupService.list())
            .willReturn(responses);

        // when
        ResultActions result = mockMvc.perform(get("/api/menu-groups"));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().json(toJson(responses)));
    }
}
