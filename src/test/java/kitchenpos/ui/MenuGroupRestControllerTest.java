package kitchenpos.ui;

import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.dto.request.MenuGroupCommand;
import kitchenpos.application.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class MenuGroupRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("MenuGroup을 생성한다.")
    void create() throws Exception {
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(1L, MENU_GROUP_NAME1);
        given(menuGroupService.create(any(MenuGroupCommand.class))).willReturn(menuGroupResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MenuGroupCommand(MENU_GROUP_NAME1))))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/menu-groups/1"));
    }

    @Test
    @DisplayName("MenuGroup을 모두 조회한다.")
    void list() throws Exception {
        List<MenuGroupResponse> menuGroupResponses = List.of(new MenuGroupResponse(1L, MENU_GROUP_NAME1),
                new MenuGroupResponse(2L, MENU_GROUP_NAME2));
        given(menuGroupService.list()).willReturn(menuGroupResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/menu-groups"))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(menuGroupResponses)));
    }
}
