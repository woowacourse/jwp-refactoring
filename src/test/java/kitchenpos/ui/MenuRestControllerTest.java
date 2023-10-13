package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 메뉴를_저장한다() throws Exception {
        // given
        Menu menu = new Menu();
        menu.setId(1L);
        when(menuService.create(any(Menu.class))).thenReturn(menu);

        // when
        ResultActions result = mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(menu))
        );

        // then
        result.andExpectAll(
                status().isCreated(),
                header().string("Location", "/api/menus/" + menu.getId()),
                content().json(objectMapper.writeValueAsString(menu))
        );
    }

    @Test
    void 메뉴_목록을_조회한다() throws Exception {
        // given
        List<Menu> menus = List.of(new Menu(), new Menu());
        when(menuService.list()).thenReturn(menus);

        // when
        ResultActions result = mockMvc.perform(get("/api/menus"));

        // then
        result.andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(menus))
        );
    }
}
