package kitchenpos.application;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_CHANGE_EMPTY_REQUEST;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_CHANGE_GUEST_REQUEST;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_CREATE_REQUEST;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static kitchenpos.common.fixtures.TableGroupFixtures.TABLE_GROUP1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import kitchenpos.common.ServiceTest;
import kitchenpos.common.fixtures.OrderTableFixtures;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.table.OrderTableChangeEmptyRequest;
import kitchenpos.dto.table.OrderTableChangeGuestRequest;
import kitchenpos.dto.table.OrderTableCreateRequest;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.OrderTableException.CannotChangeEmptyStateByOrderStatusException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 테이블 생성 시 테이블 그룹 ID를 null로 설정하여 저장한다.")
    void create() {
        // given
        final OrderTableCreateRequest request = ORDER_TABLE1_CREATE_REQUEST();
        int numberOfGuests = request.getNumberOfGuests();
        boolean empty = request.isEmpty();

        // when
        final OrderTableResponse response = tableService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.getId()).isNotNull();
            softly.assertThat(response.getNumberOfGuests()).isEqualTo(numberOfGuests);
            softly.assertThat(response.isEmpty()).isEqualTo(empty);
            softly.assertThat(response.getTableGroup()).isNull();
        });
    }

    @Nested
    @DisplayName("주문 테이블의 빈 테이블 여부 변경 시")
    class ChangeEmpty {

        @Test
        @DisplayName("빈 테이블 여부 변경에 성공한다.")
        void success() {
            // given
            final OrderTableChangeEmptyRequest request = ORDER_TABLE1_CHANGE_EMPTY_REQUEST();
            final OrderTable orderTable = OrderTableFixtures.ORDER_TABLE1();
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            boolean beforeEmpty = savedOrderTable.isEmpty();

            // when
            final OrderTableResponse response = tableService.changeEmpty(savedOrderTable.getId(), request);

            // then
            assertThat(response.isEmpty()).isNotEqualTo(beforeEmpty);
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
        void throws_notFoundOrderTable() {
            // given
            final Long notExistOrderTableId = -1L;
            final OrderTableChangeEmptyRequest request = ORDER_TABLE1_CHANGE_EMPTY_REQUEST();
            final OrderTable orderTable = OrderTableFixtures.ORDER_TABLE1();
            orderTableRepository.save(orderTable);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(notExistOrderTableId, request))
                    .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                    .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("주문 테이블의 테이블 그룹이 존재하면 예외가 발생한다.")
        void throws_ExistTableGroup() {
            // given
            final OrderTableChangeEmptyRequest request = ORDER_TABLE1_CHANGE_EMPTY_REQUEST();
            final OrderTable orderTable = OrderTableFixtures.ORDER_TABLE1();
            final TableGroup tableGroup = TABLE_GROUP1();
            final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
            orderTable.confirmTableGroup(savedTableGroup);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), request))
                    .isInstanceOf(OrderTableException.AlreadyExistTableGroupException.class)
                    .hasMessage("[ERROR] 이미 Table Group이 존재합니다.");
        }

        @Test
        @DisplayName("주문 테이블 ID에 해당하는 주문이 존재하고 주문 상태가 조리 or 식사면 예외가 발생한다.")
        void throws_existsByOrderTableIdAndOrderStatusIn() {
            // given
            final OrderTableChangeEmptyRequest request = ORDER_TABLE1_CHANGE_EMPTY_REQUEST();
            final OrderTable orderTable = ORDER_TABLE1();

            final OrderTable savedOrderTable1 = orderTableRepository.save(orderTable);

            final Order order = new Order(savedOrderTable1, OrderStatus.MEAL, LocalDateTime.now());
            orderRepository.save(order);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(CannotChangeEmptyStateByOrderStatusException.class)
                    .hasMessage("[ERROR] 주문 테이블의 주문 상태가 조리중이거나 식사중일 때 주문 테이블의 상태를 변경할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("주문 테이블의 손님 수 변경 시")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("변경에 성공한다.")
        void success() {
            // given
            final OrderTableChangeGuestRequest request = ORDER_TABLE1_CHANGE_GUEST_REQUEST();
            final OrderTable orderTable = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            int beforeNumberOfGuests = savedOrderTable.getNumberOfGuests();

            // when
            OrderTableResponse response = tableService.changeNumberOfGuests(orderTable.getId(), request);
            // then
            assertSoftly(softly -> {
                softly.assertThat(response.getNumberOfGuests()).isNotEqualTo(beforeNumberOfGuests);
                softly.assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
            });
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
        void throws_orderTableNotExist() {
            // given
            final Long notExistOrderTableId = -1L;
            final OrderTableChangeGuestRequest request = ORDER_TABLE1_CHANGE_GUEST_REQUEST();
            final OrderTable orderTable = OrderTableFixtures.ORDER_TABLE1();
            orderTableRepository.save(orderTable);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistOrderTableId, request))
                    .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                    .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("주문 테이블이 비어있는 상태면 예외가 발생한다.")
        void throws_orderTableIsEmpty() {
            // given
            final OrderTableChangeGuestRequest request = ORDER_TABLE1_CHANGE_GUEST_REQUEST();
            final OrderTable orderTable = OrderTableFixtures.ORDER_TABLE1();
            orderTableRepository.save(orderTable);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(OrderTableException.CannotChangeNumberOfGuestsStateInEmptyException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있는 상태에서 손님 수를 변경할 수 없습니다.");
        }
    }
}
