package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Nested
    class 테이블_생성 {

        @Test
        void 정상_요청() {
            // given
            OrderTable orderTable = createOrderTable(true, null, 0);

            // when
            OrderTable savedOrderTable = tableService.create(orderTable);

            // then
            assertThat(savedOrderTable)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(orderTable);
        }
    }

    @Nested
    class 테이블_조회 {

        @Test
        void 정상_요청() {
            // given
            OrderTable orderTable = createOrderTable(true, null, 0);
            OrderTable savedOrderTable = tableService.create(orderTable);

            // when
            List<OrderTable> orderTables = tableService.readAll();

            // then
            assertThat(orderTables)
                    .extracting(OrderTable::getId)
                    .contains(savedOrderTable.getId());
        }
    }

    @Nested
    class 테이블_상태_변경 {

        @Test
        void 정상_요청() {
            OrderTable orderTable = createOrderTable(true, null, 0);
            OrderTable savedOrderTable = tableDao.save(orderTable);

            OrderTable newOrderTable = createOrderTable(false, null, 0);

            // when
            OrderTable updatedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), newOrderTable);

            // then
            assertThat(updatedOrderTable.isEmpty()).isFalse();
        }

        @Test
        void 존재하지_않는_테이블_상태_변경_시_예외_발생() {
            // given
            long invalidOrderTableId = -1;

            OrderTable newOrderTable = createOrderTable(false, null, 0);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeEmpty(invalidOrderTableId, newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹이_있는_테이블_상태_변경_시_예외_발생() {
            // given
            TableGroup newTableGroup = new TableGroup();
            newTableGroup.setCreatedDate(LocalDateTime.now());

            TableGroup tableGroup = tableGroupDao.save(newTableGroup);

            OrderTable orderTable = createOrderTable(false, tableGroup.getId(), 10);
            OrderTable savedOrderTable = tableDao.save(orderTable);

            OrderTable newOrderTable = createOrderTable(false, null, 0);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeEmpty(savedOrderTable.getId(), newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COOKING"})
        void 상태_변경할_테이블_주문의_상태가_COMPLETION인_경우_예외_발생(final OrderStatus status) {
            // given
            OrderTable orderTable = createOrderTable(true, null, 0);
            OrderTable savedOrderTable = tableDao.save(orderTable);
            Order order = createOrder(savedOrderTable.getId(), status);
            orderDao.save(order);

            OrderTable newOrderTable = createOrderTable(false, null, 0);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeEmpty(savedOrderTable.getId(), newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_인원수_변경 {

        @Test
        void 정상_요청() {
            // given
            OrderTable orderTable = createOrderTable(false, null, 0);
            OrderTable savedOrderTable = tableDao.save(orderTable);

            OrderTable newOrderTable = createOrderTable(false, null, 10);

            // when
            OrderTable updatedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(updatedOrderTable.getId()).isEqualTo(savedOrderTable.getId());
                        softly.assertThat(updatedOrderTable.getNumberOfGuests())
                                .isNotEqualTo(savedOrderTable.getNumberOfGuests())
                                .isEqualTo(newOrderTable.getNumberOfGuests());
                    }
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -100, -1000})
        void 변경할_인원수가_0미만이면_예외_발생(int numberOfGuests) {
            // given
            OrderTable orderTable = createOrderTable(false, null, 0);
            OrderTable savedOrderTable = tableDao.save(orderTable);

            OrderTable newOrderTable = createOrderTable(false, null, numberOfGuests);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블_인원수_변경_시_예외_발생() {
            // given
            long invalidOrderTableId = -1;
            OrderTable newOrderTable = createOrderTable(false, null, 10);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(invalidOrderTableId, newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_테이블_인원수_변경_시_예외_발생() {
            // given
            OrderTable orderTable = createOrderTable(true, null, 0);
            OrderTable savedOrderTable = tableDao.save(orderTable);

            OrderTable newOrderTable = createOrderTable(true, null, 10);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderTable createOrderTable(final boolean empty,
                                        final Long tableGroupId,
                                        final Integer numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    private Order createOrder(final Long orderTableId,
                              final OrderStatus status) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(status.name());
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }
}
