package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Money;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

public class CreateMenuCommand {

    public static class CreateMenuProductCommand {
        private Long productId;
        private Integer quantity;

        public CreateMenuProductCommand(final Long productId, final Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public MenuProduct toDomain() {
            return new MenuProduct(null, productId, quantity);
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

    public Menu toDomain() {
        List<MenuProduct> products = menuProducts.stream()
                .map(CreateMenuProductCommand::toDomain)
                .collect(Collectors.toList());
        return new Menu(null, name, new Money(price), menuGroupId, products);
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
