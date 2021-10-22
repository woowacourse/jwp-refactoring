package kitchenpos.integration.api;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.integration.utils.MockMvcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuApi {

    private static final String BASE_URL = "/api/menus";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<Menu> 메뉴_등록(Menu menu) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(menu)
            .asSingleResult(Menu.class);
    }

    public MockMvcResponse<Menu> 메뉴_등록(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return 메뉴_등록(menu);
    }

    public MockMvcResponse<List<Menu>> 메뉴_조회() {
        return mockMvcUtils.request()
            .get(BASE_URL)
            .asMultiResult(Menu.class);
    }
}
