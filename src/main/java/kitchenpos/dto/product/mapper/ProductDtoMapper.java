package kitchenpos.dto.product.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    @Mapping(target = "price", expression = "java(product.getPrice().getValue())")
    ProductResponse toProductResponse(Product product);

    default List<ProductResponse> toProductResponses(List<Product> product) {
        return product.stream()
                .map(it -> new ProductResponse(it.getId(), it.getName(), it.getPrice().getValue()))
                .collect(Collectors.toList());
    }
}
