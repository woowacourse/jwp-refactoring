package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ControllerTest;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends ControllerTest {

    private final String defaultMenuGroupUrl = "/api/menu-groups";
    private final MenuGroupResponse menuGroupResponse = new MenuGroupResponse(1L, "set-menu");

    @MockBean
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성할_수_있다() throws Exception {
        // given
        given(menuGroupService.create(any(MenuGroupCreateRequest.class))).willReturn(menuGroupResponse);

        // when
        ResultActions response = postRequestWithJson(defaultMenuGroupUrl, new MenuGroupCreateRequest());

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(menuGroupResponse)));
    }

    @Test
    void 메뉴_그룹_목록을_조회할_수_있다() throws Exception {
        // given
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(1L, "set-menu");
        List<MenuGroupResponse> menuGroupResponses = Arrays.asList(menuGroupResponse);
        when(menuGroupService.list()).thenReturn(menuGroupResponses);

        // when
        ResultActions response = getRequest(defaultMenuGroupUrl);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(menuGroupResponses)));
    }
}
