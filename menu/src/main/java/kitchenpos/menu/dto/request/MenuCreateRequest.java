package kitchenpos.menu.dto.request;

import java.math.BigDecimal;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
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
