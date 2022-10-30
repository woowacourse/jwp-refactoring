package kitchenpos.menu.presentation.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuGroupRequestDto;
import kitchenpos.menu.application.dto.MenuProductDto;
import kitchenpos.menu.application.dto.MenuRequestDto;
import kitchenpos.menu.domain.MenuProduct;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(final String name,
                       final BigDecimal price,
                       final Long menuGroupId,
                       final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public MenuRequestDto toServiceDto(){
        final List<MenuProductDto> menuProductDtos = menuProducts.stream()
                .map(it -> new MenuProductDto(null, null, it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new MenuRequestDto(name, price, menuGroupId, menuProductDtos);
    }
}
