package kitchenpos.application.dto;

import kitchenpos.application.dto.response.ProductResponseDto;
import kitchenpos.domain.Product;

public class ProductDtoAssembler {

    private ProductDtoAssembler() {
    }

    public static ProductResponseDto productResponseDto(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice());
    }
}
