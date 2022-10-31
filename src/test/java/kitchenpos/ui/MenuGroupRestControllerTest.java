package kitchenpos.ui;

import static kitchenpos.MenuGroupFixtures.createMenuGroupRequest;
import static kitchenpos.MenuGroupFixtures.createMenuGroupResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class MenuGroupRestControllerTest extends ControllerTest {

    private final MenuGroupService menuGroupService;

    @Autowired
    public MenuGroupRestControllerTest(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @Test
    void create() throws Exception {
        // given
        MenuGroupResponse response = createMenuGroupResponse();

        given(menuGroupService.create(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(post("/api/menu-groups")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createMenuGroupRequest()))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menu-groups/" + response.getId()));
    }

    @Test
    void list() throws Exception {
        // given
        MenuGroupResponse response = createMenuGroupResponse();
        given(menuGroupService.list()).willReturn(List.of(response));

        // when
        ResultActions actions = mockMvc.perform(get("/api/menu-groups"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(response))));
    }
}
