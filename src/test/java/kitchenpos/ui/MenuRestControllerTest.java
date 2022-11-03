package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.MenuFixtures;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.response.MenuResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class MenuRestControllerTest extends ControllerTest {

    private MenuService menuService;

    @Autowired
    public MenuRestControllerTest(MenuService menuService) {
        this.menuService = menuService;
    }

    @Test
    void create() throws Exception {
        // given
        MenuResponse response = MenuFixtures.createMenuResponse();
        given(menuService.create(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(post("/api/menus")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(MenuFixtures.createMenuCreateRequest()))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/" + response.getId()));
    }

    @Test
    void list() throws Exception {
        // given
        MenuResponse response = MenuFixtures.createMenuResponse();
        given(menuService.list()).willReturn(List.of(response));

        // when
        ResultActions actions = mockMvc.perform(get("/api/menus"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(response))));
    }
}
