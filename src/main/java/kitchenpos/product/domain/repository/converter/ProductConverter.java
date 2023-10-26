package kitchenpos.product.domain.repository.converter;

import kitchenpos.product.domain.Product;
import kitchenpos.product.persistence.dto.ProductDataDto;
import kitchenpos.support.Converter;
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
