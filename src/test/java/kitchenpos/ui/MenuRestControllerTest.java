package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.MenuService;
import kitchenpos.domain.MenuProductCreateInfo;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuResponse;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends RestControllerTest {
    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴 생성 요청을 수행한다.")
    @Test
    void create() throws Exception {
        MenuProductCreateInfo menuProductCreateInfo1 = new MenuProductCreateInfo(1L, 1L);
        MenuProductCreateInfo menuProductCreateInfo2 = new MenuProductCreateInfo(2L, 2L);
        List<MenuProductCreateInfo> menuProductCreateInfos = Arrays.asList(menuProductCreateInfo1,
            menuProductCreateInfo2);

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("menu name", BigDecimal.valueOf(1_000L), 1L,
            menuProductCreateInfos);

        List<MenuProductResponse> menuProductResponses = Arrays.asList(
            new MenuProductResponse(1L, menuProductCreateInfo1.getProductId(),
                menuProductCreateInfo1.getQuantity()),
            new MenuProductResponse(2L, menuProductCreateInfo2.getProductId(),
                menuProductCreateInfo2.getQuantity()));
        MenuResponse menuResponse = new MenuResponse(1L, menuCreateRequest.getName(), menuCreateRequest.getPrice(),
            menuCreateRequest.getMenuGroupId(), menuProductResponses);

        given(menuService.create(any(MenuCreateRequest.class))).willReturn(menuResponse);

        mockMvc.perform(post("/api/menus")
            .content(objectMapper.writeValueAsString(menuCreateRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/menus/" + menuResponse.getId()))
            .andDo(print());
    }

    @DisplayName("메뉴 전체 조회 요청을 수행한다.")
    @Test
    void list() throws Exception {
        List<MenuProductResponse> menuProductResponses = Arrays.asList(
            new MenuProductResponse(1L, 1L, 1L),
            new MenuProductResponse(2L, 2L, 2L));
        MenuResponse menuResponse = new MenuResponse(1L, "test", BigDecimal.valueOf(1_000L), 1L, menuProductResponses);

        given(menuService.list()).willReturn(Collections.singletonList(menuResponse));

        mockMvc.perform(get("/api/menus")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }
}