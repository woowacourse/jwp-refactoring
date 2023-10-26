package kitchenpos.menu.persistence.dto;

import java.math.BigDecimal;

public class MenuDataDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;

    public MenuDataDto(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
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
}
