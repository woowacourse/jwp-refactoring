package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {

    private long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductResponse> menuProductResponses;

    private MenuResponse(final long id,
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

    public long getId() {
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
}
