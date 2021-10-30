package kitchenpos.ui;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dto.request.menu.CreateMenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("MenuGroupRestController 단위 테스트")
class MenuGroupRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void create() throws Exception {
        // given
        CreateMenuGroupRequest 추천메뉴 = new CreateMenuGroupRequest("추천 메뉴");
        MenuGroupResponse expected = new MenuGroupResponse(1L, "추천 메뉴");
        given(menuGroupService.create(any(CreateMenuGroupRequest.class))).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(post("/api/menu-groups")
                .content(objectToJsonString(추천메뉴))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/menu-groups/" + expected.getId()))
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다.")
    void list() throws Exception {
        // given
        MenuGroupResponse 추천메뉴 = new MenuGroupResponse(1L, "추천 메뉴");
        MenuGroupResponse 세트메뉴 = new MenuGroupResponse(2L, "세트 메뉴");
        List<MenuGroupResponse> expected = Arrays.asList(추천메뉴, 세트메뉴);
        given(menuGroupService.list()).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectToJsonString(expected)));
    }
}
