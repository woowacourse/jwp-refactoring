package kitchenpos.menu.request;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductDto {

    @NotNull
    private final Long productId;
    @NotNull
    private final long quantity;

    private MenuProductDto(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductDto> of(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> new MenuProductDto(menuProduct.getProductId(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
