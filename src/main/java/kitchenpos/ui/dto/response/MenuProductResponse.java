package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.response.MenuProductResponseDto;

public class MenuProductResponse {

    private Long id;
    private ProductResponse productResponse;
    private Long quantity;

    private MenuProductResponse() {
    }

    public MenuProductResponse(Long id, ProductResponse productResponse, Long quantity) {
        this.id = id;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProductResponseDto menuProductResponseDto) {
        return new MenuProductResponse(
            menuProductResponseDto.getId(),
            ProductResponse.from(menuProductResponseDto.getProductResponseDto()),
            menuProductResponseDto.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public Long getQuantity() {
        return quantity;
    }
}
