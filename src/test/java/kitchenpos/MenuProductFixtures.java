package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.application.dto.request.MenuProductCreateRequest;
import kitchenpos.application.dto.response.MenuProductResponse;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.MenuProduct;

public class MenuProductFixtures {

    private static final long PRODUCT_ID = 1L;
    private static final long PRICE = 1000L;
    private static final int QUANTITY = 3;

    private MenuProductFixtures() {
    }

    public static MenuProduct createMenuProduct() {
        return createMenuProduct(1L, PRICE, QUANTITY);
    }

    public static MenuProduct createMenuProduct(Long productId, Long price, int quantity) {
        return new MenuProduct(productId, new Price(BigDecimal.valueOf(price)), new Quantity(quantity));
    }

    public static MenuProductCreateRequest createMenuProductCreateRequest() {
        return createMenuProductCreateRequest(PRODUCT_ID, QUANTITY);
    }

    public static MenuProductCreateRequest createMenuProductCreateRequest(Long productId, int quantity) {
        return new MenuProductCreateRequest(productId, quantity);
    }

    public static MenuProductResponse createMenuProductResponse() {
        return new MenuProductResponse(1L, 1L, 1L, 2);
    }
}
