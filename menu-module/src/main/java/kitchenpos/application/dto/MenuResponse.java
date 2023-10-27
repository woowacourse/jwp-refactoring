package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(
            Long id,
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductResponse> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroup().getId(),
                menu.getMenuProducts().stream()
                        .map(MenuProductResponse::from)
                        .toList()
        );
    }

    public Long getId() {
        return id;
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public static class MenuProductResponse {

        private Long seq;
        private Long productId;
        private long quantity;

        public MenuProductResponse(Long seq, Long productId, long quantity) {
            this.seq = seq;
            this.productId = productId;
            this.quantity = quantity;
        }

        public static MenuProductResponse from(MenuProduct menuProduct) {
            return new MenuProductResponse(
                    menuProduct.getSeq(),
                    menuProduct.getProductId(),
                    menuProduct.getQuantity()
            );
        }

        public Long getSeq() {
            return seq;
        }

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
