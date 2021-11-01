package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    protected  MenuCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity() {
        return new Menu(
                name,
                price,
                new MenuGroup(menuGroupId),
                this.menuProducts.stream()
                    .map(product -> new MenuProduct(
                            new Product(product.productId),
                            product.quantity)
                    )
                    .collect(Collectors.toList())
        );
    }

    public static class MenuProductRequest {

        private long productId;
        private long quantity;

        protected MenuProductRequest() {
        }

        public long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
