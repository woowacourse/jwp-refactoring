package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.function.Consumer;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductDto;

public enum ProductFixture {

    FRIED_CHICKEN(1L, "후라이드치킨", BigDecimal.valueOf(16000L)),
    SPICY_CHICKEN(2L, "매운치킨", BigDecimal.valueOf(16000L));

    public final Long id;
    public final String name;
    public final BigDecimal price;

    ProductFixture(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductDto computeDefaultProductDto(Consumer<ProductDto> consumer) {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("후라이드치킨");
        productDto.setPrice(BigDecimal.valueOf(16000L));
        consumer.accept(productDto);
        return productDto;
    }

    public ProductDto toDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(name);
        productDto.setPrice(price);
        return productDto;
    }

    public Product toEntity() {
        return new Product(id, name, price);
    }
}

