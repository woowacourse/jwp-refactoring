package kitchenpos.application;

import kitchenpos.dto.request.OrderTableEmptyRequestDto;
import kitchenpos.dto.response.OrderTableResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface TableDomainService {

    OrderTableResponseDto changeEmpty(
        final Long orderTableId,
        final OrderTableEmptyRequestDto orderTableEmptyRequestDto
    );

}
