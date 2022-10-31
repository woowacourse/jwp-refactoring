package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateMenuDto;
import kitchenpos.application.dto.request.CreateMenuProductDto;

public class MenuRequestDto {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequestDto> menuProducts;

    public MenuRequestDto() {
    }

    public CreateMenuDto toCreateMenuDto() {
        List<CreateMenuProductDto> menuProductsDto = menuProducts.stream()
                .map(MenuProductRequestDto::toCreateMenuProductDto)
                .collect(Collectors.toList());
        return new CreateMenuDto(name, price, menuGroupId, menuProductsDto);
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

    public List<MenuProductRequestDto> getMenuProducts() {
        return menuProducts;
    }

    static class MenuProductRequestDto {

        private Long productId;
        private Integer quantity;

        public MenuProductRequestDto() {
        }

        public CreateMenuProductDto toCreateMenuProductDto() {
            return new CreateMenuProductDto(productId, quantity);
        }

        public Long getProductId() {
            return productId;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}
