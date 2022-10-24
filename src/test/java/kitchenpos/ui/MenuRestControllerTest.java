package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
@ExtendWith(MockitoExtension.class)
public class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @DisplayName("menu를 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(1L, 3);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 3);
        final List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final Menu menu = new Menu("메뉴1", BigDecimal.valueOf(3000), 1L, menuProducts);

        final Menu savedMenu = new Menu(1L,"메뉴1", BigDecimal.valueOf(3000), 1L, menuProducts);

        given(menuService.create(any(Menu.class))).willReturn(savedMenu);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menu)))
                .andDo(print());

        // then
        resultActions.andExpect(status().isCreated());
    }
}
