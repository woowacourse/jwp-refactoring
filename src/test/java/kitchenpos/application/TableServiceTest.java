package kitchenpos.application;

import static kitchenpos.domain.exception.OrderTableExceptionType.TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP;
import static kitchenpos.fixture.OrderFixture.createOrderLineItem;
import static kitchenpos.fixture.TableFixture.*;
import static kitchenpos.fixture.TableFixture.비어있는_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exception.OrderTableException;
import kitchenpos.fixture.TableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceIntegrationTest {

    @Test
    @DisplayName("table을 생성한다.")
    void create() {
        final OrderTable orderTable = 비어있는_주문_테이블();

        final OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable)
            .usingRecursiveComparison()
            .ignoringFields("id", "tableGroupId")
            .isEqualTo(orderTable);
    }

    @Test
    @DisplayName("table 목록을 조회한다.")
    void list() {
        final List<OrderTable> orderTables = 비어있는_전체_주문_테이블();
        orderTables.forEach(tableService::create);

        final List<OrderTable> foundedOrderTables = tableService.list();

        assertThat(foundedOrderTables)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .isEqualTo(orderTables);
    }

    @Nested
    @DisplayName("table의 상태를 empty로 바꾼다.")
    class ChangeEmpty {

        @Test
        @DisplayName("성공적으로 바꾼다.")
        void success() {
            final OrderTable orderTable = tableService.create(비어있는_주문_테이블());

            final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(),
                orderTable);

            assertTrue(changedOrderTable.isEmpty());
        }

        @Test
        @DisplayName("tableGroupId가 null이 아닌 경우 예외처리한다.")
        void throwExceptionTableGroupIdIsNonNull() {
            final OrderTable orderTable = 비어있는_주문_테이블();
            final OrderTable savedOrderTable = tableService.create(orderTable);
            saveTableGroup(savedOrderTable);

            final Long orderTableId = savedOrderTable.getId();
            assertThatThrownBy(
                () -> tableService.changeEmpty(orderTableId, savedOrderTable)
            ).isInstanceOf(OrderTableException.class)
                .hasMessage(TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP.getMessage());
        }

        @Test
        @DisplayName("저장된 table에 속한 order가 cooking이나 meal 상태인 경우 예외처리")
        void throwExceptionTableIsEmpty() {
            //given
            final OrderTable orderTable = 비어있는_주문_테이블();
            orderTable.setEmpty(false);
            final OrderTable savedOrderTable = tableService.create(orderTable);
            createOrderSuccessfully(savedOrderTable);

            //when
            final Long orderTableId = savedOrderTable.getId();
            assertThatThrownBy(
                () -> tableService.changeEmpty(orderTableId, savedOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        private void createOrderSuccessfully(final OrderTable orderTable) {
            final Menu menu = createMenu();
            final OrderLineItem orderLineItem = createOrderLineItem(menu.getId(), 1L);

            final Order order = new Order();
            order.setOrderLineItems(List.of(orderLineItem));
            order.setOrderTableId(orderTable.getId());
            orderService.create(order);
        }
    }

    @Nested
    @DisplayName("게스트의 수를 변경할 수 있다.")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("정상적으로 변경한다.")
        void success() {
            //given
            final OrderTable orderTable = 비어있는_주문_테이블();
            orderTable.setEmpty(false);

            final OrderTable savedOrderTable = tableService.create(orderTable);
            savedOrderTable.changeNumberOfGuests(10);

            //when
            final OrderTable changedOrderTable =
                tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable);

            //then
            assertThat(changedOrderTable.getNumberOfGuests())
                .isEqualTo(savedOrderTable.getNumberOfGuests());
        }

        //파라미터를 dto로 바꾸면 예외처리
//        @Test
//        @DisplayName("numberOfGuest가 0미만인 경우 예외처리")
//        void throwExceptionNumberOfGuestLowerThan0() {
//            //given
//            final OrderTable orderTable = 주문_테이블();
//            orderTable.setEmpty(false);
//
//            final OrderTable savedOrderTable = tableService.create(orderTable);
//            savedOrderTable.changeNumberOfGuests(-1);
//
//            //when
//            assertThatThrownBy(() ->
//                tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable)
//            ).isInstanceOf(IllegalArgumentException.class);
//        }

        //TODO: 추후 DTO로 변경하면서 리팩터링
//        @Test
//        @DisplayName("orderTable이 비어있는 경우 예외처리")
//        void throwExceptionOrderTableIsEmpty() {
//            //given
//            final OrderTable orderTable = 비어있는_주문_테이블();
//            orderTable.setEmpty(true);
//
//            final OrderTable savedOrderTable = tableService.create(orderTable);
//            savedOrderTable.changeNumberOfGuests(10);
//
//            //when
//            final Long tableId = savedOrderTable.getId();
//            assertThatThrownBy(() ->
//                tableService.changeNumberOfGuests(tableId, savedOrderTable)
//            ).isInstanceOf(OrderTableException.class)
//                .hasMessage(TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP.getMessage());
//        }
    }
}
