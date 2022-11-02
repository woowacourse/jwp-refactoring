package kitchenpos.application.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderStatusRecordRepository;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.request.OrderTableCreateRequest;
import kitchenpos.dto.table.response.OrderTableResponse;
import kitchenpos.exception.badrequest.OrderNotExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableServiceTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStatusRecordRepository orderStatusRecordRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableService tableService;

    @Test
    void 주문_테이블을_생성할_수_있다() {
        OrderTableCreateRequest request = new OrderTableCreateRequest(1, false);

        OrderTableResponse response = tableService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있다() {
        Long orderTableId1 = tableService.create(new OrderTableCreateRequest(0, true))
                .getId();
        Long orderTableId2 = tableService.create(new OrderTableCreateRequest(0, true))
                .getId();

        List<OrderTableResponse> actual = tableService.list();

        assertThat(actual).hasSize(2)
                .extracting("id")
                .containsExactly(orderTableId1, orderTableId2);
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경할_수_있다() {
        Long orderTableId = tableService.create(new OrderTableCreateRequest(1, false))
                .getId();

        OrderTableResponse response = tableService.changeEmpty(orderTableId, true);

        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    void 변경_대상_테이블이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> tableService.changeEmpty(0L, true))
                .isInstanceOf(OrderNotExistsException.class);
    }

    @Test
    void 테이블의_방문한_손님_수를_변경할_수_있다() {
        Long orderTableId = tableService.create(new OrderTableCreateRequest(1, false))
                .getId();

        OrderTableResponse response = tableService.changeNumberOfGuests(orderTableId, 1);

        assertThat(response.getNumberOfGuests()).isOne();
    }

    @Test
    void 인원_변경_테이블이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, 1))
                .isInstanceOf(OrderNotExistsException.class);
    }
}
