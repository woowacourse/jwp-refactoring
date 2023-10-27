package kitchenpos.menu.ui.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuRequest {
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public MenuRequest(final String name,
                       final BigDecimal price,
                       final Long menuGroupId,
                       final List<MenuProductRequest> menuProducts) {
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

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuRequest that = (MenuRequest) o;
        return Objects.equals(name, that.name)
                && Objects.equals(price, that.price)
                && Objects.equals(menuGroupId, that.menuGroupId)
                && Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroupId, menuProducts);
    }

    @Override
    public String toString() {
        return "MenuRequest{" +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
