package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class MenuRestControllerTest extends RestControllerTest {

    @Test
    void 메뉴_생성에_성공한다() throws Exception {
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest("메뉴", BigDecimal.valueOf(1_000), 1L,
                Arrays.asList(menuProductRequest));
        MenuProduct expectedMenuProduct =
                new MenuProduct(1L, 1L, 1L, 1, BigDecimal.valueOf(1_000));
        Menu expected =
                new Menu(1L, "메뉴", BigDecimal.valueOf(1_000), 1L, Arrays.asList(expectedMenuProduct));

        when(menuService.create(any())).thenReturn(expected);

        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(menuRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 메뉴_목록_조회에_성공한다() throws Exception {
        MenuProduct expectedMenuProduct = new MenuProduct(1L, 1L, 1L, 1, BigDecimal.valueOf(1_000));
        Menu expected =
                new Menu(1L, "메뉴", BigDecimal.valueOf(1_000), 1L, Arrays.asList(expectedMenuProduct));

        when(menuService.list()).thenReturn(Arrays.asList(expected));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/menus"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
