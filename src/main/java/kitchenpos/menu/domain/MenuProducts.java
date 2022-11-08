package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import kitchenpos.menu.exception.NotFoundException;
import kitchenpos.product.domain.Product;

@Embeddable
public class MenuProducts {

    private static final String NOT_FOUND_MENU_GROUP_ERROR_MESSAGE = "존재하지 않는 메뉴그룹입니다.";

    @ElementCollection
    @CollectionTable(name = "menu_product", joinColumns = @JoinColumn(name = "menu_id"))
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public BigDecimal calculateTotalAmount(final List<Product> products) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Long productId = menuProduct.getProductId();
            final BigDecimal price = getPrice(products, productId);
            final long quantity = menuProduct.getQuantity();
            final BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(amount);
        }
        return totalAmount;
    }

    private BigDecimal getPrice(final List<Product> products, final Long productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MENU_GROUP_ERROR_MESSAGE))
                .getPrice();
    }
}
