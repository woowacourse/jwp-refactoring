package kitchenpos.menu.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class MenuCreateRequest {
    @NotBlank
    private String name;

    @NotNull
    private Long price;

    @NotNull
    private Long menuGroupId;

    private List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, Long price, Long menuGroupId, List<MenuProductCreateRequest> menuProducts) {
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
        return this.menuProducts.stream()
                .map(MenuProductCreateRequest::getProductId)
                .collect(Collectors.toList());
    }
}
