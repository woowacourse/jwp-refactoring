package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final MenuGroupResponse menuGroupResponse;
    private final List<MenuProductResponse> menuProductResponses;

    public MenuResponse(final Long id,
                        final String name,
                        final BigDecimal price,
                        final MenuGroupResponse menuGroupResponse,
                        final List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
        this.menuProductResponses = menuProductResponses;
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

    public MenuGroupResponse getMenuGroupResponse() {
        return menuGroupResponse;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}
