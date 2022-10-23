package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;



import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

class MenuRestControllerTest extends ControllerTest {

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() throws Exception {
        // given
        List<MenuProductRequest> menuProductRequests = List.of(
            new MenuProductRequest(1L, 2),
            new MenuProductRequest(2L, 1)
        );

        MenuRequest menuRequest = new MenuRequest(
            "앙념 두마리 치킨",
            BigDecimal.valueOf(20000),
            1L,
            menuProductRequests
        );

        List<MenuProductResponse> menuProductResponses = List.of(
            new MenuProductResponse(1L, 1L, 1L, 2),
            new MenuProductResponse(2L, 1L, 2L, 1)
        );

        MenuResponse menuResponse = new MenuResponse(
            1L, "양념 두마리 치킨",
            BigDecimal.valueOf(20000), 1L,
            menuProductResponses
        );

        given(menuService.create(any(MenuRequest.class)))
            .willReturn(menuResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(menuRequest)));

        // then
        result.andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/menus/1"))
            .andExpect(content().json(objectMapper.writeValueAsString(menuResponse)));
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        List<MenuProductResponse> chickenMenuProductResponses = List.of(
            new MenuProductResponse(1L, 1L, 1L, 2),
            new MenuProductResponse(2L, 1L, 2L, 1)
        );

        MenuResponse chickenMenuResponse = new MenuResponse(
            1L, "양념 두마리 치킨",
            BigDecimal.valueOf(20000), 1L,
            chickenMenuProductResponses
        );

        List<MenuProductResponse> pizzaMenuProductResponses = List.of(
            new MenuProductResponse(3L, 2L, 3L, 1),
            new MenuProductResponse(4L, 2L, 4L, 1)
        );

        MenuResponse pizzaMenuResponse = new MenuResponse(
            2L, "피자 세트",
            BigDecimal.valueOf(20000), 2L,
            pizzaMenuProductResponses
        );

        List<MenuResponse> menuResponses = List.of(chickenMenuResponse, pizzaMenuResponse);

        given(menuService.list())
            .willReturn(menuResponses);

        // when
        ResultActions result = mockMvc.perform(get("/api/menus"));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(menuResponses)));
    }
}
