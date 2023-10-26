package kitchenpos.api.menu;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.common.vo.Price;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.menu.application.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuCreateApiTest extends ApiTestConfig {

    @DisplayName("메뉴 생성 API 테스트")
    @Test
    void createMenu() throws Exception {
        // given
        final MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(1L, 2);
        final MenuCreateRequest request = new MenuCreateRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1L,
                List.of(menuProductCreateRequest)
        );

        final Menu menu = spyMenu(request);
        final MenuProduct menuProduct = spyMenuProduct(menuProductCreateRequest, menu);
        menu.addMenuProducts(List.of(menuProduct));

        // when
        final MenuResponse response = MenuResponse.from(menu);

        when(menuService.create(eq(request))).thenReturn(response);

        // then
        mockMvc.perform(post("/api/menus")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/menus/%d", response.getId())));
    }

    private Menu spyMenu(final MenuCreateRequest menuCreateRequest) {
        final MenuGroup menuGroup = spyMenuGroup(menuCreateRequest.getMenuGroupId());
        final Menu menu = new Menu(menuCreateRequest.getName(), new Price(menuCreateRequest.getPrice()), menuGroup);
        final Menu spyMenu = spy(menu);
        when(spyMenu.getId()).thenReturn(1L);
        return spyMenu;
    }

    private MenuGroup spyMenuGroup(final Long menuGroupId) {
        final MenuGroup menuGroup = new MenuGroup("여우 팬모임");
        final MenuGroup spyMenuGroup = spy(menuGroup);
        when(spyMenuGroup.getId()).thenReturn(menuGroupId);
        return spyMenuGroup;
    }

    private MenuProduct spyMenuProduct(final MenuProductCreateRequest menuProductCreateRequest, final Menu menu) {
        final Product product = spyProduct(menuProductCreateRequest.getProductId());
        final MenuProduct menuProduct = new MenuProduct(menuProductCreateRequest.getQuantity(), menu, product);
        final MenuProduct spyMenuProduct = spy(menuProduct);
        when(spyMenuProduct.getSeq()).thenReturn(1L);
        return spyMenuProduct;
    }

    private Product spyProduct(final Long productId) {
        final Product product = new Product("상품", new Price(BigDecimal.valueOf(19000)));
        final Product spyProduct = spy(product);
        when(spyProduct.getId()).thenReturn(productId);
        return spyProduct;
    }
}
