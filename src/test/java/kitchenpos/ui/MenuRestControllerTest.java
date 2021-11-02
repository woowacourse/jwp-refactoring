package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.ObjectMapperForTest;
import kitchenpos.application.MenuService;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.ui.request.MenuRequest;
import kitchenpos.ui.response.MenuProductResponse;
import kitchenpos.ui.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends ObjectMapperForTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() throws Exception {
        //given
        MenuRequest menu = new MenuRequest("치즈 버거 세트", 6800L, 1L, Arrays.asList(
            new MenuProductRequest(1L, 1L, 1L, 1L),
            new MenuProductRequest(2L, 2L, 2L, 5L),
            new MenuProductRequest(3L, 3L, 3L, 1L)
        )
        );
        MenuResponse expected = new MenuResponse(1L, "치즈 버거 세트", 6800L, 1L, Arrays.asList(
            new MenuProductResponse(1L, 1L, 1L, 1L),
            new MenuProductResponse(2L, 2L, 2L, 5L),
            new MenuProductResponse(3L, 3L, 3L, 1L)
        )
        );
        given(menuService.create(any(MenuRequest.class))).willReturn(expected);

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
        MenuResponse menuA = new MenuResponse(1L, "치즈 버거 세트", 6800L, 1L, Arrays.asList(
            new MenuProductResponse(1L, 1L, 1L, 1L),
            new MenuProductResponse(2L, 2L, 2L, 5L),
            new MenuProductResponse(3L, 3L, 3L, 1L)
        )
        );
        MenuResponse menuB = new MenuResponse(2L, "불고기 버거 세트", 8000L, 1L, Arrays.asList(
            new MenuProductResponse(1L, 4L, 1L, 1L),
            new MenuProductResponse(2L, 2L, 2L, 5L),
            new MenuProductResponse(3L, 3L, 3L, 1L)
        )
        );
        MenuResponse menuC = new MenuResponse(3L, "치킨 버거 단품", 3500L, 2L, Collections.singletonList(
            new MenuProductResponse(1L, 5L, 1L, 1L)
        )
        );
        List<MenuResponse> expected = Arrays.asList(menuA, menuB, menuC);
        given(menuService.list()).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(get("/api/menus"));

        //then
        response.andExpect(status().isOk())
            .andExpect(content().json(objectToJson(expected)));
    }
}
