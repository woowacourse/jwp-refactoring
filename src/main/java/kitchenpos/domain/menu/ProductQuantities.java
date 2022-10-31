package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductQuantities {

    private final List<ProductQuantity> values;

    public ProductQuantities(List<ProductQuantity> values) {
        this.values = values;
    }

    public BigDecimal calculateTotalPrice() {
        return this.values.stream()
                .map(ProductQuantity::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> toMenuProducts(Long menuId) {
        return values.stream()
                .map(it -> new MenuProduct(menuId, it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
