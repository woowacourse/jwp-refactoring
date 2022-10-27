package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class MenuGroupRestControllerTest extends ControllerTest {

    @DisplayName("POST /api/menu-groups")
    @Test
    void create() throws Exception {
        // given
        MenuGroupRequest request = new MenuGroupRequest("메뉴그룹");
        MenuGroupResponse response = new MenuGroupResponse(1L, "메뉴그룹");
        given(menuGroupApiService.create(any(MenuGroupRequest.class)))
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menu-groups/" + response.getId()));
    }


    @DisplayName("GET /api/menu-groups")
    @Test
    void list() throws Exception {
        // given
        List<MenuGroupResponse> menuGroupResponses = List.of(
                new MenuGroupResponse(1L, "상품1"),
                new MenuGroupResponse(2L, "상품2")
        );
        given(menuGroupApiService.list()).willReturn(menuGroupResponses);

        // when
        ResultActions result = mockMvc.perform(get("/api/menu-groups"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(menuGroupResponses)));
    }
}
