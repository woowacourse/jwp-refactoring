package kitchenpos.menu.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

public class MenuCreationRequest {

    @NotNull
    private final String name;
    @NotNull
    private final BigDecimal price;
    @NotNull
    private final Long menuGroupId;
    @NotNull
    private final List<MenuProductRequest> menuProductRequests;

    public MenuCreationRequest(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductRequest> menuProductRequests
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
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

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }

    public List<Long> getProductIdsInMenuProduct() {
        return menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    public Map<Long, Long> getProductQuantitiesByProductId() {
        return menuProductRequests.stream()
                .collect(Collectors.toMap(MenuProductRequest::getProductId, MenuProductRequest::getQuantity));
    }
}
