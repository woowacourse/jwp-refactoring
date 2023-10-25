package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.config.ServiceTest;
import kitchenpos.table.application.request.OrderTableCreateRequest;
import kitchenpos.table.application.request.OrderTableUpdateRequest;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    void 주문_테이블을_생성한다() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(10, false);

        OrderTableResponse response = tableService.create(orderTableCreateRequest);

        assertAll(
                () -> assertThat(response.getId()).isPositive(),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(10)
        );
    }

    @Test
    void 모든_주문_테이블을_조회한다() {
        orderTableRepository.save(OrderTable.builder().numberOfGuests(10).empty(false).build());
        orderTableRepository.save(OrderTable.builder().numberOfGuests(5).empty(false).build());

        List<OrderTableResponse> orderTableResponses = tableService.list();

        assertThat(orderTableResponses).hasSizeGreaterThanOrEqualTo(2);
    }

    @Nested
    class 주문_테이블_EMPTY_상태를_변경할_때 {

        @Test
        void 정상적으로_변경한다() {
            OrderTable orderTable = orderTableRepository.save(
                    OrderTable.builder().empty(false).numberOfGuests(4).build());
            OrderTableUpdateRequest orderTableUpdateRequest = new OrderTableUpdateRequest(10, true);

            OrderTableResponse orderTableResponse = tableService.changeEmpty(orderTable.getId(),
                    orderTableUpdateRequest);

            assertThat(orderTableResponse.isEmpty()).isTrue();
        }

        @Test
        void 주문_테이블을_찾을_수_없으면_예외가_발생한다() {
            Long wrongOrderTableId = -1L;
            OrderTableUpdateRequest orderTableUpdateRequest = new OrderTableUpdateRequest(10, true);

            assertThatThrownBy(() -> tableService.changeEmpty(wrongOrderTableId, orderTableUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("일치하는 주문 테이블이 존재하지 않습니다.");
        }

        @Test
        void 주문_테이블이_그룹에_포함되어_있으면_예외가_발생한다() {
            OrderTable savedOrderTable = orderTableRepository.save(
                    OrderTable.builder()
                            .numberOfGuests(4)
                            .empty(false)
                            .build()
            );
            tableGroupRepository.save(TableGroup.builder()
                    .orderTables(List.of(savedOrderTable))
                    .build()
            );
            OrderTableUpdateRequest orderTableUpdateRequest = new OrderTableUpdateRequest(10, true);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTableUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 그룹에 속한 주문 테이블의 상태를 변경할 수 없습니다.");
        }

        @Test
        void 주문_테이블의_주문_중_COMPLETION_상태가_아닌_주문이_존재하면_예외가_발생한다() {
            OrderTable savedOrderTable = orderTableRepository.save(
                    OrderTable.builder()
                            .numberOfGuests(4)
                            .empty(false)
                            .build()
            );
            orderRepository.save(Order.builder()
                    .orderTable(savedOrderTable)
                    .orderStatus(OrderStatus.MEAL)
                    .build()
            );
            OrderTableUpdateRequest orderTableUpdateRequest = new OrderTableUpdateRequest(10, true);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTableUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블에 진행 중인 주문이 존재합니다.");
        }
    }

    @Nested
    class 주문_테이블에_손님_수를_변경할_때 {

        @Test
        void 정상적으로_변경한다() {
            OrderTable orderTable = orderTableRepository.save(OrderTable.builder().numberOfGuests(2).build());
            OrderTableUpdateRequest orderTableUpdateRequest = new OrderTableUpdateRequest(10, false);

            OrderTableResponse response = tableService.changeNumberOfGuests(orderTable.getId(),
                    orderTableUpdateRequest);

            assertThat(response.getNumberOfGuests()).isEqualTo(10);
        }

        @Test
        void 존재하지_않는_주문_테이블이면_예외가_발생한다() {
            Long wrongOrderTableId = -1L;
            OrderTableUpdateRequest orderTableUpdateRequest = new OrderTableUpdateRequest(10, false);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(wrongOrderTableId, orderTableUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("일치하는 주문 테이블이 존재하지 않습니다.");
        }

        @Test
        void 변경하는_주문_테이블이_EMTPY_상태이면_예외가_발생한다() {
            OrderTable orderTable = orderTableRepository.save(
                    OrderTable.builder().numberOfGuests(2).empty(true).build());
            OrderTableUpdateRequest orderTableUpdateRequest = new OrderTableUpdateRequest(5, false);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("EMPTY 상태인 주문 테이블의 손님 수를 변경할 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -1_000})
        void 변경하는_손님_수가_1명_미만_이면_예외가_발생한다(int value) {
            OrderTable orderTable = orderTableRepository.save(OrderTable.builder().numberOfGuests(2).build());
            OrderTableUpdateRequest orderTableUpdateRequest = new OrderTableUpdateRequest(value, false);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("손님 수는 음수가 될 수 없습니다.");
        }
    }
}
