package kitchenpos.application;

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
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("테이블을 빈 상태로 변경한다. - 실패, 주문 테이블을 찾을 수 없음.")
    @Test
    void changeEmptyFailedWhenOrderTableNotFound() {
        // given
        Long orderTableId = -100L;
        TableRequest tableRequest = CREATE_TABLE_REQUEST(0, false);

        given(orderTableRepository.findById(orderTableId))
                .willThrow(NonExistentException.class);

        // when - then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, tableRequest))
                .isInstanceOf(NonExistentException.class);
        then(orderTableRepository).should(times(1))
                .findById(orderTableId);
        then(orderRepository).should(never())
                .findAllByOrderTableId(orderTableId);
    }

    @DisplayName("테이블을 빈 상태로 변경한다. - 실패, 조리 혹은 식사 중인 주문이 존재.")
    @Test
    void changeEmptyFailedWhenOrderStatusNotValid() {
        // given
        Long orderTableId = 1L;
        TableRequest tableRequest = CREATE_TABLE_REQUEST(0, false);
        OrderTable orderTable = CREATE_ORDER_TABLE(1L, CREATE_TABLE_GROUP(1L), 10, false);
        List<Order> orders = Collections.singletonList(
                CREATE_ORDER(
                        null,
                        CREATE_ORDER_TABLE(orderTableId, CREATE_TABLE_GROUP(1L), 10, false),
                        OrderStatus.MEAL.name()
                )
        );

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderRepository.findAllByOrderTableId(orderTableId)).willReturn(orders);

        // when - then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, tableRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findById(orderTableId);
        then(orderRepository).should(times(1))
                .findAllByOrderTableId(orderTableId);
    }

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
