package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static kitchenpos.fixture.MenuFixture.menu;
import static kitchenpos.fixture.MenuFixture.menuRequest;
import static kitchenpos.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.fixture.ProductFixture.product;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @Test
    void 메뉴를_생성한다() throws Exception {
        // given
        List<MenuProduct> menuProducts = List.of(menuProduct(product("chicken", 1000L), 1L));
        Menu createdMenu = menu("Test Menu", 1000L, 1L, menuProducts);
        MenuRequest request = menuRequest("Test Menu", 1000L, 1L, menuProducts);

        // when
        when(menuService.create(any(MenuRequest.class))).thenReturn(createdMenu);

        // then
        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/" + createdMenu.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(createdMenu)));
    }

    @Test
    void 메뉴를_전체_조회한다() throws Exception {
        // given
        List<MenuProduct> menuProducts = List.of(menuProduct(product("chicken", 1000L), 1L));
        Menu menu1 = menu("Menu1", 1000L, 1L, menuProducts);
        Menu menu2 = menu("Menu2", 1000L, 1L, menuProducts);

        // when
        when(menuService.list()).thenReturn(List.of(menu1, menu2));

        // then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(menu1, menu2))));
    }
}
