package kitchenpos.mapper;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductDto;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        return productDto;
    }

    public static Product toEntity(ProductDto productDto) {
        return new Product.Builder()
            .name(productDto.getName())
            .price(productDto.getPrice())
            .build();
    }
}
