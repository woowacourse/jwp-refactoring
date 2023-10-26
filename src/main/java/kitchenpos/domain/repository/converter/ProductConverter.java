package kitchenpos.domain.repository.converter;

import kitchenpos.domain.Product;
import kitchenpos.persistence.dto.ProductDataDto;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements Converter<Product, ProductDataDto> {

    @Override
    public ProductDataDto entityToData(final Product product) {
        return new ProductDataDto(product.getId(), product.getName(), product.getPrice());
    }

    @Override
    public Product dataToEntity(final ProductDataDto productDataDto) {
        return new Product(productDataDto.getId(), productDataDto.getName(), productDataDto.getPrice());
    }
}
