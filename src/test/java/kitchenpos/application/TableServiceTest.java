package kitchenpos.application;

import kitchenpos.event.OrderStatusCheckEventPublisher;
import kitchenpos.exception.NonExistentException;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.ui.dto.TableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.utils.Fixture.DomainFactory.CREATE_ORDER;
import static kitchenpos.utils.Fixture.DomainFactory.CREATE_ORDER_TABLE;
import static kitchenpos.utils.Fixture.DomainFactory.CREATE_TABLE_GROUP;
import static kitchenpos.utils.Fixture.RequestFactory.CREATE_TABLE_REQUEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ServiceTest
class TableServiceTest {
    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderStatusCheckEventPublisher orderStatusCheckEventPublisher;

    @DisplayName("방문한 손님 수를 변경한다. - 실패, 주문 테이블을 찾을 수 없음.")
    @Test
    void changeNumberOfGuestsFailedWhenOrderTableNotFound() {
        // given
        Long orderTableId = -100L;
        TableRequest tableRequest = CREATE_TABLE_REQUEST(10, false);

        given(orderTableRepository.findById(orderTableId)).willThrow(NonExistentException.class);

        // when - then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, tableRequest))
                .isInstanceOf(NonExistentException.class);
        then(orderTableRepository).should(times(1))
                .findById(orderTableId);
    }
}
