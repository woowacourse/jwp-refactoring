package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.TableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("테이블 서비스 테스트")
class TableServiceTest extends ServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("테이블을 empty로 변경한다. - 실패, orderTableId에 해당하는 테이블이 존재하지 않는 경우")
    @Test
    void changeEmptyFailedWhenOrderTableIdNotFound() {
        // given
        long orderTableId = -1L;
        TableRequest tableRequest = CREATE_TABLE_REQUEST(0, true);

        given(orderTableRepository.findById(orderTableId)).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, tableRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findById(orderTableId);
//        then(orderRepository).should(never())
//                .existsByOrderTableIdAndOrderStatusIn(
//                        orderTableId,
//                        Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
//                );
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("테이블을 empty로 변경한다. - 실패, orderTable의 tableGroupId가 이미 등록되어있는 경우")
    @Test
    void changeEmptyFailedWhenTableGroupIdIsNull() {
        // given
        long orderTableId = 1L;
        TableRequest tableRequest = CREATE_TABLE_REQUEST(0, true);

        OrderTable savedOrderTable = new OrderTable(orderTableId, new TableGroup(1L, LocalDateTime.MIN), 10, false);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

        // when - then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, tableRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findById(orderTableId);
//        then(orderRepository).should(never())
//                .existsByOrderTableIdAndOrderStatusIn(
//                        orderTableId,
//                        Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
//                );
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("테이블을 empty로 변경한다. - 실패, COOKING, MEAL 상태인 주문이 포함된 orderTableId가 아닌 경우")
    @Test
    void changeEmptyFailedWhenStatusNotSatisfied() {
        // given
        long orderTableId = 1L;

        TableRequest tableRequest = CREATE_TABLE_REQUEST(0, true);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(any(OrderTable.class)));
//        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(
//                orderTableId,
//                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
//        ).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, tableRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findById(orderTableId);
//        then(orderRepository).should(times(1))
//                .existsByOrderTableIdAndOrderStatusIn(
//                        orderTableId,
//                        Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
//                );
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("방문 손님 수를 변경한다. - 실패, 방문 손님 수가 음수인 경우")
    @Test
    void changeNumberOfGuestsFailedWhenNegative() {
        // given
        long orderTableId = 1L;
        TableRequest tableRequest = CREATE_TABLE_REQUEST(-1, false);

        // when - then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, tableRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(never())
                .findById(orderTableId);
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("방문 손님 수를 변경한다. - 실패, orderTableId가 존재하지 않는 경우")
    @Test
    void changeNumberOfGuestsFailedWhenOrderTableIdNotFound() {
        // given
        long orderTableId = -1L;
        TableRequest tableRequest = CREATE_TABLE_REQUEST(100, false);
        given(orderTableRepository.findById(orderTableId)).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, tableRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findById(orderTableId);
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("방문 손님 수를 변경한다. - 실패, orderTableId로 조회한 테이블이 비어있는 경우")
    @Test
    void changeNumberOfGuestsFailedWhenTableIsEmpty() {
        // given
        TableRequest tableRequest = CREATE_TABLE_REQUEST(10, false);

        long orderTableId = 1L;
        OrderTable savedOrderTable = new OrderTable(1L, null, 10, true);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

        // when - then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, tableRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findById(orderTableId);
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }
}
