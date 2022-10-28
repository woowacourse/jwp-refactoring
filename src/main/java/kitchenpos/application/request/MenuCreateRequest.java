package kitchenpos.application.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId,
                             final List<MenuProductCreateRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        return new Menu(null, name, price, menuGroupId, menuProducts.stream()
                        .map(MenuProductCreateRequest::toEntity)
                        .collect(Collectors.toList()));
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

    public static class MenuProductCreateRequest {

        private Long productId;
        private long quantity;

        private MenuProductCreateRequest() {
        }

        public MenuProductCreateRequest(final Long productId, final long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public MenuProduct toEntity() {
            return new MenuProduct(null, null, productId, quantity);
        }

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
