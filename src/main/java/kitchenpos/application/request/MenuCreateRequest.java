package kitchenpos.application.request;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;

@Getter
public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId,
                             final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }
}
