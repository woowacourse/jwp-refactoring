package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductInfo> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductInfo> menuProducts) {
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

    public List<MenuProductInfo> getMenuProducts() {
        return menuProducts;
    }

    public List<MenuProduct> toMenuProducts() {
        return getMenuProducts()
                .stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
    }

    public static class MenuProductInfo {

        private Long productId;
        private long quantity;

        private MenuProductInfo() {
        }

        public MenuProductInfo(Long productId, long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
