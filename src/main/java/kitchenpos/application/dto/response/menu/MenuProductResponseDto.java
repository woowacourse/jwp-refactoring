package kitchenpos.application.dto.response.menu;

import kitchenpos.application.dto.response.product.ProductResponseDto;
import kitchenpos.domain.MenuProduct;

public class MenuProductResponseDto {

    private Long id;
    private ProductResponseDto productResponseDto;
    private Long quantity;

    private MenuProductResponseDto() {
    }

    public MenuProductResponseDto(Long id, ProductResponseDto productResponseDto, Long quantity) {
        this.id = id;
        this.productResponseDto = productResponseDto;
        this.quantity = quantity;
    }

    public static MenuProductResponseDto from(MenuProduct menuProduct) {
        return new MenuProductResponseDto(menuProduct.getSeq(), ProductResponseDto.from(menuProduct.getProduct()), menuProduct.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public ProductResponseDto getProductResponseDto() {
        return productResponseDto;
    }

    public Long getQuantity() {
        return quantity;
    }
}
