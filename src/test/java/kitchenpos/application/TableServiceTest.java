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
            OrderTable orderTable = OrderTable.of(null, 0, true);

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
            OrderTable orderTable = OrderTable.of(null, 0, true);
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
            OrderTable orderTable = OrderTable.of(null, 0, true);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            OrderTable newOrderTable = OrderTable.of(null, 0, false);

            // when
            OrderTable updatedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), newOrderTable);

            // then
            assertThat(updatedOrderTable.isEmpty()).isFalse();
        }

        @Test
        void 존재하지_않는_테이블_상태_변경_시_예외_발생() {
            // given
            long invalidOrderTableId = -1;

            OrderTable newOrderTable = OrderTable.of(null, 0, false);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeEmpty(invalidOrderTableId, newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹이_있는_테이블_상태_변경_시_예외_발생() {
            // given
            TableGroup newTableGroup = TableGroup.of(LocalDateTime.now());

            TableGroup tableGroup = tableGroupRepository.save(newTableGroup);

            OrderTable orderTable = OrderTable.of(tableGroup, 10, false);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            OrderTable newOrderTable = OrderTable.of(null, 0, false);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeEmpty(savedOrderTable.getId(), newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COOKING"})
        void 상태_변경할_테이블_주문의_상태가_COMPLETION인_경우_예외_발생(final OrderStatus status) {
            // given
            OrderTable orderTable = OrderTable.of(null, 0, true);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            Order order = createOrder(savedOrderTable, status);
            orderRepository.save(order);

            OrderTable newOrderTable = OrderTable.of(null, 0, false);

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
            OrderTable orderTable = OrderTable.of(null, 0, false);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            OrderTable newOrderTable = OrderTable.of(null, 10, false);

            // when
            OrderTable updatedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(updatedOrderTable.getId()).isEqualTo(savedOrderTable.getId());
                        softly.assertThat(updatedOrderTable.getNumberOfGuests())
                                .isEqualTo(newOrderTable.getNumberOfGuests());
                    }
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -100, -1000})
        void 변경할_인원수가_0미만이면_예외_발생(int numberOfGuests) {
            // given
            OrderTable orderTable = OrderTable.of(null, 0, false);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            OrderTable newOrderTable = OrderTable.of(null, numberOfGuests, false);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블_인원수_변경_시_예외_발생() {
            // given
            long invalidOrderTableId = -1;
            OrderTable newOrderTable = OrderTable.of(null, 10, false);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(invalidOrderTableId, newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_테이블_인원수_변경_시_예외_발생() {
            // given
            OrderTable orderTable = OrderTable.of(null, 0, true);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            OrderTable newOrderTable = OrderTable.of(null, 10, true);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Order createOrder(final OrderTable orderTable,
                              final OrderStatus status) {
        return Order.of(orderTable, status.name(), LocalDateTime.now());
    }
}
