package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.application.dto.OrderTableCreationDto;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.ui.dto.request.OrderTableCreationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("TableService 는 ")
@SpringTestWithData
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createOrderTable() {
        final OrderTableCreationRequest orderTableCreationRequest = new OrderTableCreationRequest(0, true);
        final OrderTableCreationDto orderTableCreationDto = OrderTableCreationDto.from(orderTableCreationRequest);

        final OrderTableDto orderTableDto = tableService.createOrderTable(orderTableCreationDto);

        assertAll(
                () -> assertThat(orderTableDto.getId()).isGreaterThanOrEqualTo(1L),
                () -> assertThat(orderTableDto.getNumberOfGuests()).isEqualTo(0)
        );
    }
}
