package kitchenpos.ui.dto.response;


import java.math.BigDecimal;
import java.util.List;

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
