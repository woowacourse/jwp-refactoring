package kitchenpos.application.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    public MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
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

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }

    public Menu toMenu() {
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(MenuProductRequest::toMenuProduct)
                .collect(Collectors.toList());
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static class MenuProductRequest {
        private Long productId;
        private Long quantity;

        public MenuProductRequest() {
        }

        public MenuProductRequest(Long productId, Long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public MenuProduct toMenuProduct() {
            return new MenuProduct(productId, quantity);
        }

        public Long getProductId() {
            return productId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}
