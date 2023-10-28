package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.application.dto.MenuProductCreateRequest;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import org.springframework.stereotype.Component;

@Component
public class MenuProductsMapper {

    public MenuProducts mapFrom(final List<MenuProductCreateRequest> request) {
        return makeMenuProducts(request);
    }

    private MenuProducts makeMenuProducts(final List<MenuProductCreateRequest> request) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductCreateRequest menuProductRequest : request) {
            final MenuProduct menuProduct = new MenuProduct(
                    menuProductRequest.getProductId(),
                    menuProductRequest.getQuantity()
            );
            menuProducts.add(menuProduct);
        }
        return new MenuProducts(menuProducts);
    }
}
