package kitchenpos.menu.application.response;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    public MenuResponse(final Long id,
                        final String name,
                        final BigDecimal price,
                        final Long menuGroupId,
                        final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        final List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menuProductResponses
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuResponse that = (MenuResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(price, that.price)
                && Objects.equals(menuGroupId, that.menuGroupId)
                && Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }

    @Override
    public String toString() {
        return "MenuResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
