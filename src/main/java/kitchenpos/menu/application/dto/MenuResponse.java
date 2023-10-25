package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final List<MenuProductResponse> menuProductResponses;

    private MenuResponse(final Long id,
                         final String name,
                         final BigDecimal price,
                         final List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse of(final Menu menu, final List<MenuProduct> menuProducts) {
        List<MenuProductResponse> menuProductResponses = menuProducts.stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menuProductResponses);
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

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuResponse that = (MenuResponse) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(price, that.price)
                && Objects.equals(menuProductResponses, that.menuProductResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuProductResponses);
    }
}
