package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId,
                             List<MenuProductCreateRequest> menuProducts) {
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

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity(List<Product> products) {
        return Menu.create(getName(), getPrice(), getMenuGroupId(), toMenuProductEntities(products));
    }

    private List<MenuProduct> toMenuProductEntities(List<Product> products) {
        return menuProducts.stream()
                .map(menuProduct -> MenuProduct.createWithPrice(
                        null,
                        menuProduct.getProductId(),
                        toProductPrice(menuProduct.getProductId(), products),
                        menuProduct.getQuantity()))
                .collect(Collectors.toList());
    }

    private BigDecimal toProductPrice(Long productId, List<Product> products) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findAny()
                .map(Product::getPrice)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProductCreateRequest::getProductId)
                .collect(Collectors.toList());
    }
}
