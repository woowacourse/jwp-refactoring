package kitchenpos.api.menu;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
        // FIXME: domain -> dto 로 변경
        final Product product = new Product(1L, "여우가 좋아하는 피자", BigDecimal.valueOf(17000));

        final Menu expectedMenu = new Menu();
        expectedMenu.setId(1L);
        expectedMenu.setName("여우메뉴");
        expectedMenu.setPrice(BigDecimal.valueOf(15000));

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(expectedMenu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        expectedMenu.setMenuProducts(List.of(menuProduct));

        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("여우메뉴그룹");

        expectedMenu.setMenuGroupId(menuGroup.getId());

        // when
        when(menuService.list()).thenReturn(List.of(expectedMenu));

        // then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(expectedMenu.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedMenu.getName())))
                .andExpect(jsonPath("$[0].price", is(expectedMenu.getPrice().intValue())))
                .andExpect(jsonPath("$[0].menuGroupId", is(expectedMenu.getMenuGroupId().intValue())))
                .andExpect(jsonPath("$[0].menuProducts.size()", is(1)))
                .andExpect(jsonPath("$[0].menuProducts[0].seq", is(expectedMenu.getMenuProducts().get(0).getSeq().intValue())))
                .andExpect(jsonPath("$[0].menuProducts[0].productId", is(expectedMenu.getMenuProducts().get(0).getProductId().intValue())))
                .andExpect(jsonPath("$[0].menuProducts[0].menuId", is(expectedMenu.getMenuProducts().get(0).getMenuId().intValue())))
                .andExpect(jsonPath("$[0].menuProducts[0].quantity", is(Long.valueOf(expectedMenu.getMenuProducts().get(0).getQuantity()).intValue())));
    }
}
