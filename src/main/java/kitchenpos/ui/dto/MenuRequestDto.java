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

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public void setMenuProducts(List<MenuProductRequestDto> menuProducts) {
        this.menuProducts = menuProducts;
    }

    static class MenuProductRequestDto {

        private Long productId;
        private Integer quantity;

        public MenuProductRequestDto() {
        }

        public CreateMenuProductDto toCreateMenuProductDto() {
            return new CreateMenuProductDto(productId, quantity);
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
