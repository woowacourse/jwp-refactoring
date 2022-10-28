package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity(final List<Product> products) {
        final List<MenuProduct> menuProducts = this.menuProducts.stream()
                .map(it -> new MenuProduct(null, findProduct(it.getProductId(), products), it.getQuantity()))
                .collect(Collectors.toList());
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    private Product findProduct(final Long productId, final List<Product> products) {
        return products.stream()
                .filter(it -> it.getId().equals(productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
