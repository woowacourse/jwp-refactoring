package kitchenpos.integration.api;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.integration.api.texture.ProductTexture;
import kitchenpos.menu.ui.request.MenuCreateRequest;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menu.ui.request.MenuUpdateRequest;
import kitchenpos.testtool.MockMvcResponse;
import kitchenpos.testtool.MockMvcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuApi {

    private static final String BASE_URL = "/api/menus";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<MenuResponse> 메뉴_등록(MenuCreateRequest menu) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(menu)
            .asSingleResult(MenuResponse.class);
    }

    public MockMvcResponse<MenuResponse> 메뉴_등록(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return 메뉴_등록(MenuCreateRequest.create(name, price, menuGroupId, menuProducts));
    }

    public MockMvcResponse<MenuResponse> 메뉴_등록(ProductTexture productTexture, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        final String name = productTexture.getProduct().getName();
        final BigDecimal price = productTexture.getProduct().getPrice();
        return 메뉴_등록(MenuCreateRequest.create(name, price, menuGroupId, menuProducts));
    }

    public MockMvcResponse<List<MenuResponse>> 메뉴_조회() {
        return mockMvcUtils.request()
            .get(BASE_URL)
            .asMultiResult(MenuResponse.class);
    }

    public MockMvcResponse<MenuResponse> 메뉴_수정(Long menuId, String name, BigDecimal price) {
        return mockMvcUtils.request()
            .put(BASE_URL + "/{menuId}", menuId)
            .content(MenuUpdateRequest.create(name, price))
            .asSingleResult(MenuResponse.class);
    }
}
