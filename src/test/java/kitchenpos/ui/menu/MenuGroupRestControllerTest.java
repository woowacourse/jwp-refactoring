package kitchenpos.ui.menu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.menu.dto.CreateMenuGroupResponse;
import kitchenpos.application.menu.dto.SearchMenuGroupResponse;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.ui.ControllerTest;
import kitchenpos.ui.menu.dto.CreateMenuGroupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class MenuGroupRestControllerTest extends ControllerTest {

    @Test
    void 메뉴_그룹_생성() throws Exception {
        // given
        CreateMenuGroupRequest createMenuGroupRequest = new CreateMenuGroupRequest("추천메뉴");
        String request = objectMapper.writeValueAsString(createMenuGroupRequest);

        MenuGroup menuGroup = new MenuGroup(1L, "추천메뉴");
        CreateMenuGroupResponse createMenuGroupResponse = CreateMenuGroupResponse.from(menuGroup);

        given(menuGroupService.create(any())).willReturn(createMenuGroupResponse);
        String response = objectMapper.writeValueAsString(createMenuGroupResponse);

        // when & then
        mockMvc.perform(post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void 메뉴_그룹_조회() throws Exception {
        // given
        List<SearchMenuGroupResponse> menuGroupResponses = List.of(
                SearchMenuGroupResponse.from(new MenuGroup(1L, "추천메뉴")),
                SearchMenuGroupResponse.from(new MenuGroup(2L, "인기메뉴"))
        );

        given(menuGroupService.list()).willReturn(menuGroupResponses);
        String response = objectMapper.writeValueAsString(menuGroupResponses);

        // when & then
        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}
