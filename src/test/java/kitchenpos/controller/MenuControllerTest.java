package kitchenpos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.MenuRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
public class MenuControllerTest extends ControllerTest {

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    public void create() throws Exception {
        // given
        Menu menu = new Menu("후라이드+후라이드",
                new BigDecimal("19000"),
                1L,
                List.of(new MenuProduct(1L, 2)));
        given(menuService.create(any()))
                .willReturn(new Menu(1L,
                        "후라이드+후라이드",
                        new BigDecimal(19000),
                        1L,
                        List.of(new MenuProduct(1L, 2))));

        // when
        ResultActions perform = mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(menu)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    public void list() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}
