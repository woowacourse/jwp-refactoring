package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class MenuCreateRequest {

    @NotNull
    private final String name;

    @NotNull
    private final Long price;

    @NotNull
    private final Long menuGroupId;

    @NotNull
    private final List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest(
            final String name,
            final Long price,
            final Long menuGroupId,
            final List<MenuProductCreateRequest> menuProducts
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProductCreateRequest::getProductId)
                .collect(Collectors.toList());
    }
}
