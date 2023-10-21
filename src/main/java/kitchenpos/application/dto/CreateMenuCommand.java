package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;

public class CreateMenuCommand {

    public static class CreateMenuProductCommand {
        private Long productId;
        private Integer quantity;

        public CreateMenuProductCommand(final Long productId, final Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

    }
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<CreateMenuProductCommand> menuProducts;

    public CreateMenuCommand(final String name, final BigDecimal price, final Long menuGroupId,
                             final List<CreateMenuProductCommand> menuProducts) {
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

    public List<CreateMenuProductCommand> getMenuProducts() {
        return menuProducts;
    }

}
