package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.MenuFixture.createMenu;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends ControllerTest {

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void create() throws Exception {
        Long menuId = 1L;
        Menu menu = createMenu();
        Menu savedMenu = createMenu(menuId);
        when(menuService.create(any())).thenReturn(savedMenu);

        mockMvc.perform(post("/api/menus")
                .content(objectMapper.writeValueAsString(menu))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/" + menuId))
                .andExpect(content().json(objectMapper.writeValueAsString(savedMenu)));
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<Menu> menus = Arrays.asList(createMenu(1L), createMenu(2L));
        when(menuService.list()).thenReturn(menus);

        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(menus)));
    }
}
