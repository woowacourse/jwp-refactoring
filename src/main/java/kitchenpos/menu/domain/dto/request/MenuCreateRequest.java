package kitchenpos.menu.domain.dto.request;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    protected MenuCreateRequest() {
    }

    public Menu toEntity() {
        return new Menu(
                name,
                new Price(price),
                menuGroupId,
                this.menuProducts.stream()
                        .map(product -> new MenuProduct(
                                product.productId,
                                product.quantity)
                        )
                        .collect(Collectors.toList())
        );
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
