package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public final class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<Long> menuProductIds;

    public static MenuResponse from(final Menu menu) {
        final List<Long> menuProductIds = menu.getMenuProducts()
                .stream()
                .map(MenuProduct::getSeq)
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProductIds);
    }

    private MenuResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId, final List<Long> menuProductIds) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductIds = menuProductIds;
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

    public List<Long> getMenuProductIds() {
        return menuProductIds;
    }
}
