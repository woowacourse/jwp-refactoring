package kitchenpos.api.menu;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.application.dto.response.MenuProductResponse;
import kitchenpos.application.dto.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MenuListApiTest extends ApiTestConfig {

    @DisplayName("메뉴 전체 조회 API 테스트")
    @Test
    void listMenu() throws Exception {
        // given
        final Menu menu = spyMenu();
        final MenuProduct menuProduct = spyMenuProduct(menu);
        menu.addMenuProducts(List.of(menuProduct));
        final MenuResponse response = MenuResponse.from(menu);

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

    private Menu spyMenu() {
        final MenuGroup menuGroup = spyMenuGroup();
        final Menu menu = new Menu("menu", new Price(BigDecimal.valueOf(19000)), menuGroup);
        final Menu spyMenu = spy(menu);
        when(spyMenu.getId()).thenReturn(1L);
        return spyMenu;
    }

    private MenuGroup spyMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup("여우 팬모임");
        final MenuGroup spyMenuGroup = spy(menuGroup);
        when(spyMenuGroup.getId()).thenReturn(1L);
        return spyMenuGroup;
    }

    private MenuProduct spyMenuProduct(final Menu menu) {
        final Product product = spyProduct();
        final MenuProduct menuProduct = new MenuProduct(1L, menu, product);
        final MenuProduct spyMenuProduct = spy(menuProduct);
        when(spyMenuProduct.getSeq()).thenReturn(1L);
        return spyMenuProduct;
    }

    private Product spyProduct() {
        final Product product = new Product("상품", new Price(BigDecimal.valueOf(19000)));
        final Product spyProduct = spy(product);
        when(spyProduct.getId()).thenReturn(1L);
        return spyProduct;
    }
}
