package kitchenpos.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.menu.MenuProductResponse;

public class MenuProductMapper {

    private MenuProductMapper() {
    }

    public static MenuProductResponse toMenuProductResponse(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getMenuId().orElse(null),
                menuProduct.getProductId().orElse(null),
                menuProduct.getQuantity().getValue()
        );
    }

    public static List<MenuProductResponse> toMenuProductResponses(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductMapper::toMenuProductResponse)
                .collect(Collectors.toList());
    }
}
