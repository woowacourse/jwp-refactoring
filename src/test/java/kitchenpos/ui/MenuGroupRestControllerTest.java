package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.util.List;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() throws Exception {
        // given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("신메뉴");

        given(menuGroupService.create(any(MenuGroupRequest.class)))
            .willReturn(new MenuGroupResponse(1L, "신메뉴"));

        // when
        ResultActions result = mockMvc.perform(post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(menuGroupRequest)));

        // then
        result.andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/menu-groups/1"));
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
            .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }
}
