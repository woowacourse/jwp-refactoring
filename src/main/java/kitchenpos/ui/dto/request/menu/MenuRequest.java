package kitchenpos.ui.dto.request.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.menu.MenuProductRequestDto;
import kitchenpos.application.dto.request.menu.MenuRequestDto;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    private MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public MenuRequestDto toDto() {
        return new MenuRequestDto(name, price, menuGroupId, convert());
    }

    private List<MenuProductRequestDto> convert() {
        return menuProductRequests.stream()
            .map(MenuProductRequest::toDto)
            .collect(Collectors.toList());
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
}
