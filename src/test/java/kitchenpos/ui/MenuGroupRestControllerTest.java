package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends ControllerTest {

    @MockBean
    private MenuGroupService menuGroupService;

    @Test
    void create() throws Exception {
        // given
        long id = 1L;
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        menuGroup.setId(id);

        given(menuGroupService.create(any())).willReturn(menuGroup);

        // when
        ResultActions actions = mockMvc.perform(post("/api/menu-groups")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(menuGroup))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menu-groups/" + id));
    }

    @Test
    void list() throws Exception {
        // given
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        given(menuGroupService.list()).willReturn(List.of(menuGroup));

        // when
        ResultActions actions = mockMvc.perform(get("/api/menu-groups"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(menuGroup))));
    }
}
