package kitchenpos.ui.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.response.ProductCreateResponse;
import kitchenpos.ui.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    @Mapping(target = "price", expression = "java(product.getPrice().getValue())")
    ProductCreateResponse toProductCreateResponse(Product product);

    default List<ProductResponse> toProductResponses(List<Product> product) {
        return product.stream()
                .map(it -> new ProductResponse(it.getId(), it.getName(), it.getPrice().getValue()))
                .collect(Collectors.toList());
    }
}
