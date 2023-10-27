package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.request.MenuProductDto;

public class MenuProductFixtures {

    public static List<MenuProductDto> of(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                           .map(menuProduct -> new MenuProductDto(menuProduct.getProductId(), menuProduct.getQuantity()))
                           .collect(Collectors.toList());
    }

}
