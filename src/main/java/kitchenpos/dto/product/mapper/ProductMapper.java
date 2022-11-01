package kitchenpos.dto.product.mapper;

import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.request.ProductCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toProduct(ProductCreateRequest productCreateRequest);
}
