package kitchenpos.dto.product.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoMapperImpl implements ProductDtoMapper {

    @Override
    public ProductResponse toProductResponse(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().getValue());
    }

    @Override
    public List<ProductResponse> toProductResponses(final List<Product> products) {
        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }
}
