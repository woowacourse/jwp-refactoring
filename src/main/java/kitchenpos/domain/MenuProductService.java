package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MenuProductService {

    private final Map<Long, Product> productById;
    private List<MenuProduct> menuProducts;

    private MenuProductService(final Map<Long, Product> productById) {
        this.productById = productById;
    }

    public MenuProductService(final Map<Long, Product> productById, final List<MenuProduct> menuProducts) {
        this.productById = productById;
        this.menuProducts = menuProducts;
    }

    public static MenuProductService of(final List<Product> products, final List<MenuProduct> menuProducts) {
        final Map<Long, Product> productById = products
                .stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        Function.identity()
                ));
        return new MenuProductService(productById, menuProducts);
    }

    public boolean isGraterThanTotalAmount(final BigDecimal price) {
        final BigDecimal totalAmount = calculateTotalAmount();
        return price.compareTo(totalAmount) > 0;
    }

    private BigDecimal calculateTotalAmount() {
        return menuProducts.stream()
                .map(this::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getAmount(final MenuProduct menuProduct) {
        final Product product = productById.get(menuProduct.getProductId());
        return product.calculateAmount(menuProduct.getQuantity());
    }

    public void verifyAllMenuProductExist() {
        if (menuProducts.size() != productById.size()) {
            throw new IllegalArgumentException();
        }
    }
}
