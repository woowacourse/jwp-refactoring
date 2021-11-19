package kitchenpos.ui.dto.response.product;

import java.util.List;

public class ProductsResponseDto {

    private List<ProductResponseDto> products;

    private ProductsResponseDto() {
    }

    public ProductsResponseDto(List<ProductResponseDto> products) {
        this.products = products;
    }

    public List<ProductResponseDto> getProducts() {
        return products;
    }
}
