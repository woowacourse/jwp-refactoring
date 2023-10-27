package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuUpdateRequest extends MenuRequest {

    private Long id;

    private MenuUpdateRequest() {
        super();
    }

    public MenuUpdateRequest(final Long id,
                             final String name,
                             final BigDecimal price,
                             final Long menuGroupId,
                             final List<MenuProductRequest> menuProductRequests) {
        super(name, price, menuGroupId, menuProductRequests);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
