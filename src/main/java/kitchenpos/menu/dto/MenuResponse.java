package kitchenpos.menu.dto;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProduct;

public final class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<Long> menuProductIds;

    public MenuResponse(
            final Long id,
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<Long> menuProductIds
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductIds = menuProductIds;
    }

    public MenuResponse(Menu menu, List<MenuProduct> menuProducts) {
        this(
                menu.getId(),
                menu.getName(),
                menu.getPrice().getValue(),
                menu.getMenuGroup().getId(),
                menuProducts.stream()
                        .map(MenuProduct::getSeq)
                        .collect(Collectors.toList())
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

    public List<Long> getMenuProductIds() {
        return menuProductIds;
    }
}
