package kitchenpos.menu.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuProductSaveRequest;
import kitchenpos.menu.application.dto.MenuSaveRequest;

public class MenuCreateRequest {

    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, Integer price, Long menuGroupId,
                             List<MenuProductCreateRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuSaveRequest toRequest() {
        List<MenuProductSaveRequest> menuProductSaveRequests = menuProducts.stream()
            .map(MenuProductCreateRequest::toResponse)
            .collect(Collectors.toList());
        return new MenuSaveRequest(name, price, menuGroupId, menuProductSaveRequests);
    }
}
