package kitchenpos.api.menu;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.vo.Price;
import kitchenpos.ui.dto.response.MenuProductResponse;
import kitchenpos.ui.dto.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MenuListApiTest extends ApiTestConfig {

    @DisplayName("메뉴 전체 조회 API 테스트")
    @Test
    void listMenu() throws Exception {
        // given
        final Menu menu = new Menu("fox menu", new Price(BigDecimal.valueOf(15000)), new MenuGroup("menu group name"));

        final MenuProductResponse menuProductResponse = new MenuProductResponse(1L, 2L, 1L);
        final MenuResponse response = new MenuResponse(1L, menu.getName(), menu.getPrice().getValue(), 1L, List.of(menuProductResponse));

        // when
        when(menuService.list()).thenReturn(List.of(response));

        // then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(response.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(response.getName())))
                .andExpect(jsonPath("$[0].price", is(response.getPrice().intValue())))
                .andExpect(jsonPath("$[0].menuGroupId", is(response.getMenuGroupId().intValue())))
                .andExpect(jsonPath("$[0].menuProducts.size()", is(1)))
                .andExpect(jsonPath("$[0].menuProducts[0].seq", is(response.getMenuProducts().get(0).getSeq().intValue())))
                .andExpect(jsonPath("$[0].menuProducts[0].productId", is(response.getMenuProducts().get(0).getProductId().intValue())))
                .andExpect(jsonPath("$[0].menuProducts[0].quantity", is(Long.valueOf(response.getMenuProducts().get(0).getQuantity()).intValue())));
    }
}
