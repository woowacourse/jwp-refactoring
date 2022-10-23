package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuDto;
import kitchenpos.application.dto.CreateMenuProductDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MenuRequestDto {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequestDto> menuProducts;

    public CreateMenuDto toCreateMenuDto() {
        List<CreateMenuProductDto> menuProductsDto = menuProducts.stream()
                .map(MenuProductRequestDto::toCreateMenuProductDto)
                .collect(Collectors.toList());
        return new CreateMenuDto(name, price, menuGroupId, menuProductsDto);
    }

    @NoArgsConstructor
    static class MenuProductRequestDto {

        private Long productId;
        private Integer quantity;

        public CreateMenuProductDto toCreateMenuProductDto() {
            return new CreateMenuProductDto(productId, quantity);
        }
    }
}
