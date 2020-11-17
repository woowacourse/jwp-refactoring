package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends RestControllerTest {
    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성 요청을 수행한다.")
    @Test
    void create() throws Exception {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("test");
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(1L, menuGroupCreateRequest.getName());

        given(menuGroupService.create(any(MenuGroupCreateRequest.class))).willReturn(menuGroupResponse);

        mockMvc.perform(post("/api/menu-groups")
            .content(objectMapper.writeValueAsString(menuGroupCreateRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/menu-groups/" + menuGroupResponse.getId()))
            .andDo(print());
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회한다.")
    @Test
    void list() throws Exception {
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(1L, "test");

        given(menuGroupService.list()).willReturn(Collections.singletonList(menuGroupResponse));

        mockMvc.perform(get("/api/menu-groups")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }
}