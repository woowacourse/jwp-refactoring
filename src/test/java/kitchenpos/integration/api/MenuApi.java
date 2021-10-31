package kitchenpos.integration.api;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.integration.api.texture.ProductTexture;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.integration.utils.MockMvcUtils;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.application.response.MenuResponse;
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
}
