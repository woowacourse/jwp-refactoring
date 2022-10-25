package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kitchenpos.MenuFixtures;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class MenuRestControllerTest extends ControllerTest {

    @Autowired
    private MenuService menuService;

    @Test
    void create() throws Exception {
        // given
        Menu menu = MenuFixtures.createMenu();
        given(menuService.create(any())).willReturn(menu);

        // when
        ResultActions actions = mockMvc.perform(post("/api/menus")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(menu))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void list() throws Exception {
        // given
        Menu menu = MenuFixtures.createMenu();
        given(menuService.list()).willReturn(List.of(menu));

        // when
        ResultActions actions = mockMvc.perform(get("/api/menus"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(menu))));
    }
}
