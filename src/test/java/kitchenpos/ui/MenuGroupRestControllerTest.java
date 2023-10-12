package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class MenuGroupRestControllerTest extends ControllerTest {

    @Test
    void 메뉴_그룹_생성() throws Exception {
        // given
        MenuGroup menuGroup = 메뉴_그룹("추천메뉴");
        String request = objectMapper.writeValueAsString(menuGroup);
        menuGroup.setId(1L);
        given(menuGroupService.create(any())).willReturn(menuGroup);
        String response = objectMapper.writeValueAsString(menuGroup);

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
        List<MenuGroup> menuGroups = List.of(메뉴_그룹(1L, "추천메뉴"), 메뉴_그룹(2L, "인기메뉴"));
        given(menuGroupService.list()).willReturn(menuGroups);
        String response = objectMapper.writeValueAsString(menuGroups);

        // when & then
        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}
