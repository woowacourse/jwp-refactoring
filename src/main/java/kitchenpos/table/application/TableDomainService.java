package kitchenpos.table.application;

import kitchenpos.table.ui.dto.request.OrderTableEmptyRequestDto;
import kitchenpos.table.ui.dto.response.OrderTableResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface TableDomainService {

    OrderTableResponseDto changeEmpty(
        final Long orderTableId,
        final OrderTableEmptyRequestDto orderTableEmptyRequestDto
    );

}
