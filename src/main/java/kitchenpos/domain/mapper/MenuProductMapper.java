package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.domain.MenuProduct;

public class MenuProductMapper {
    private MenuProductMapper() {
    }

    public static MenuProduct toMenuProduct(final MenuProductRequest request) {
        return MenuProduct.builder()
                .seq(request.getSeq())
                .menuId(request.getMenuId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();
    }
}
