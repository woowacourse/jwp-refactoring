package kitchenpos.ui;

import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class MenuGroupRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("MenuGroup을 생성한다.")
    void create() throws Exception {
        MenuGroup menuGroup = new MenuGroup(1L, MENU_GROUP_NAME1);
        given(menuGroupService.create(any(MenuGroup.class))).willReturn(menuGroup);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroup)))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/menu-groups/1"));
    }

    @Test
    @DisplayName("MenuGroup을 모두 조회한다.")
    void list() throws Exception {
        List<MenuGroup> menuGroups = List.of(new MenuGroup(1L, MENU_GROUP_NAME1),
                new MenuGroup(2L, MENU_GROUP_NAME2));
        given(menuGroupService.list()).willReturn(menuGroups);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/menu-groups"))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(menuGroups)));
    }
}
