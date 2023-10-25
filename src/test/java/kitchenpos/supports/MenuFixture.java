package kitchenpos.supports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.vo.Price;

public class MenuFixture {

    private static final String DEFAULT_NAME = "상품+상품";
    private static final long DEFAULT_QUANTITY = 2;

    public static MenuRequest of(final BigDecimal price, final Long menuGroupId, final List<ProductResponse> products) {
        final List<MenuProductRequest> menuProducts = new ArrayList<>();

        for (ProductResponse product : products) {
            final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), DEFAULT_QUANTITY);
            menuProducts.add(menuProduct);
        }

        return new MenuRequest(
                DEFAULT_NAME,
                new Price(price),
                menuGroupId,
                menuProducts
        );
    }

    public static MenuRequest of(final Long menuGroupId, final List<ProductResponse> products) {
        final List<MenuProductRequest> menuProducts = new ArrayList<>();

        Price price = new Price(BigDecimal.ZERO);
        for (ProductResponse product : products) {
            final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), DEFAULT_QUANTITY);
            menuProducts.add(menuProduct);
            price = price.add(new Price(product.getPrice().multiply(BigDecimal.valueOf(DEFAULT_QUANTITY))));
        }

        return new MenuRequest(
                DEFAULT_NAME,
                price,
                menuGroupId,
                menuProducts
        );
    }
}
