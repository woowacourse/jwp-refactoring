package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class MenuGroupRestControllerTest extends ControllerTest {

    @Test
    void 메뉴_그룹_생성() throws Exception {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
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
}
