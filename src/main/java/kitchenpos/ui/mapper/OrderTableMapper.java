package kitchenpos.ui.mapper;

import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderTableMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tableGroup", ignore = true)
    OrderTable toOrderTable(OrderTableCreateRequest orderTableCreateRequest);
}
