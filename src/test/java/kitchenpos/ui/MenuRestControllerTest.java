package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.Constructor;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends Constructor {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() throws Exception {
        //given
        Menu menu = menuConstructor("치즈 버거 세트", 6800, 1L, Arrays.asList(
            menuProductConstructor(1L, 1L, 1L, 1),
            menuProductConstructor(2L, 2L, 2L, 5),
            menuProductConstructor(3L, 3L, 3L, 1)
            )
        );
        Menu expected = menuConstructor(1L, "치즈 버거 세트", new BigDecimal(6800), 1L, Arrays.asList(
            menuProductConstructor(1L, 1L, 1L, 1),
            menuProductConstructor(2L, 2L, 2L, 5),
            menuProductConstructor(3L, 3L, 3L, 1)
            )
        );
        given(menuService.create(any(Menu.class))).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectToJson(menu))
        );

        //then
        response.andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/menus/%s", expected.getId())))
            .andExpect(content().json(objectToJson(expected)));
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void readAll() throws Exception {
        //given
        Menu menuA = menuConstructor("치즈 버거 세트", 6800, 1L, Arrays.asList(
            menuProductConstructor(1L, 1L, 1L, 1),
            menuProductConstructor(2L, 2L, 2L, 5),
            menuProductConstructor(3L, 3L, 3L, 1)
            )
        );
        Menu menuB = menuConstructor("불고기 버거 세트", 8000, 1L, Arrays.asList(
            menuProductConstructor(1L, 4L, 1L, 1),
            menuProductConstructor(2L, 2L, 2L, 5),
            menuProductConstructor(3L, 3L, 3L, 1)
            )
        );
        Menu menuC = menuConstructor("치킨 버거 단품", 3500, 2L, Collections.singletonList(
            menuProductConstructor(1L, 5L, 1L, 1)
            )
        );
        List<Menu> expected = Arrays.asList(menuA, menuB, menuC);
        given(menuService.list()).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(get("/api/menus"));

        //then
        response.andExpect(status().isOk())
            .andExpect(content().json(objectToJson(expected)));
    }
}
