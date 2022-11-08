package kitchenpos.menu.ui.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.application.request.MenuProductRequest;
import kitchenpos.menu.application.request.MenuRequest;

public class MenuApiRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductApiRequest> menuProducts;

    public MenuApiRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductApiRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuRequest toServiceRequest() {
        List<MenuProductRequest> menuProductRequests = menuProducts.stream()
                .map(MenuProductApiRequest::toServiceRequest)
                .collect(Collectors.toList());

        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }
}
