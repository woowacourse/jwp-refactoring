package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.order.apllication.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.test.fixtures.OrderFixtures;
import kitchenpos.test.fixtures.OrderTableFixtures;
import kitchenpos.test.fixtures.TableGroupFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest {
    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문 테이블을 생성한다")
    void createOrderTable() {
        // given
        final OrderTable orderTable = OrderTableFixtures.BASIC.get();

        // when
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrderTable.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
            softly.assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        });
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다")
    void getOrderTables() {
        // given
        tableService.create(OrderTableFixtures.EMPTY.get());

        // when
        final List<OrderTable> actualOrderTables = tableService.list();

        // then
        assertThat(actualOrderTables).isNotEmpty();
    }

    @Nested
    @DisplayName("주문 테이블의 빈 테이블인지 여부를 수정할 시, ")
    class ChangeEmptyOrderTable {
        @Test
        @DisplayName("정상적으로 수정한다")
        void changeEmpty() {
            // given
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.EMPTY.get());
            savedOrderTable.setEmpty(false);

            // when
            final OrderTable updatedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable);

            // then
            assertThat(updatedOrderTable.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않을 시 예외 발생")
        void notExistOrderTableException() {
            // given
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.EMPTY.get());

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeEmpty(-1L, savedOrderTable));
        }

        @Test
        @DisplayName("테이블 그룹 아이디가 null이 아닐 시 예외 발생")
        void alreadyExistTableGroupException() {
            // given
            final TableGroup tableGroup = TableGroupFixtures.BASIC.get();
            final OrderTable firstOrderTable = tableService.create(OrderTableFixtures.BASIC.get());
            final OrderTable secondOrderTable = tableService.create(OrderTableFixtures.BASIC.get());

            final List<OrderTable> orderTables = tableGroup.getOrderTables();
            orderTables.get(0).setId(firstOrderTable.getId());
            orderTables.get(1).setId(secondOrderTable.getId());

            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.EMPTY.get());
            savedOrderTable.setTableGroupId(savedTableGroup.getId());
            orderTableDao.save(savedOrderTable);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable));
        }

        @Test
        @DisplayName("주문 테이블의 상태가 요리 또는 식사중일 시 예외 발생")
        void orderStatusNotCompletionException() {
            // given
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.NOT_EMPTY.get());

            final Order order = OrderFixtures.BASIC.get();
            order.setOrderTableId(savedOrderTable.getId());
            orderService.create(order);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable));
        }
    }

    @Nested
    @DisplayName("주문 테이블의 손님 수를 수정할 때, ")
    class UpdateGuestCount {
        @Test
        @DisplayName("정상적으로 수정할 수 있다")
        void updateGuestCount() {
            // given
            final OrderTable orderTable = OrderTableFixtures.BASIC.get();
            orderTable.setEmpty(false);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            final OrderTable newOrderTable = OrderTableFixtures.BASIC.get();
            newOrderTable.setNumberOfGuests(3);

            // when
            tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable);

            // then
            final int numberOfGuests = orderTableDao.findById(savedOrderTable.getId())
                    .orElse(OrderTableFixtures.EMPTY.get())
                    .getNumberOfGuests();
            assertThat(numberOfGuests).isEqualTo(newOrderTable.getNumberOfGuests());
        }

        @Test
        @DisplayName("주문 테이블의 손님 수가 0 미만일 시 예외 발생")
        void guestCountLessThanZeroException() {
            // given
            final OrderTable orderTable = OrderTableFixtures.BASIC.get();
            final OrderTable newOrderTable = OrderTableFixtures.BASIC.get();
            newOrderTable.setNumberOfGuests(-1);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable));
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않을 시 예외 발생")
        void orderTableNotExistException() {
            // given
            final OrderTable orderTable = OrderTableFixtures.BASIC.get();
            final OrderTable newOrderTable = OrderTableFixtures.BASIC.get();

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable));
        }

        @Test
        @DisplayName("주문 테이블이 비어있을 시 예외 발생")
        void orderTableEmptyException() {
            // given
            final OrderTable savedOrderTable = tableService.create(OrderTableFixtures.EMPTY.get());
            final OrderTable newOrderTable = OrderTableFixtures.BASIC.get();

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable));
        }
    }
}
