package kitchenpos.application;

import kitchenpos.domain.dto.OrderTableRequest;
import kitchenpos.domain.dto.OrderTableResponse;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    private static Stream<List<OrderTable>> listTest() {
        final OrderTable table1 = new OrderTable(0);
        final OrderTable table2 = new OrderTable(0);
        final OrderTable table3 = new OrderTable(0);

        return Stream.of(
                List.of(),
                List.of(table1),
                List.of(table2, table3)
        );
    }

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void createTest() {
        // given
        final OrderTableRequest request = new OrderTableRequest(null, 0, true);

        // when
        final OrderTableResponse expect = tableService.create(request);

        // then
        final OrderTable orderTable = orderTableRepository.findById(expect.getId()).get();
        final OrderTableResponse actual = OrderTableResponse.from(orderTable);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("주문 테이블 목록을 조회한다.")
    void listTest(final List<OrderTable> tables) {
        // given
        orderTableRepository.saveAll(tables);

        final List<OrderTable> orderTables = orderTableRepository.findAll();
        final List<OrderTableResponse> expect = orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());

        // when & then
        final List<OrderTableResponse> actual = tableService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Nested
    @DisplayName("Table을 비우기 테스트")
    class TableEmptyTest {

        @Test
        @DisplayName("Table이 존재하지 않을 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_table_does_not_exists() {
            // given
            final long notExistsTableId = -1L;

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeEmpty(notExistsTableId, null));
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("Table의 주문 상태가 COOKING 또는 MEAL일 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_order_status_is_cooking_or_meal(final OrderStatus orderStatus) {
            // given
            final OrderTable table = orderTableRepository.save(new OrderTable(0));

            final Order order = new Order(table, new OrderLineItems(List.of()));
            order.updateOrderStatus(orderStatus);
            orderRepository.save(order);

            final OrderTableRequest request = new OrderTableRequest(null, 0, true);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeEmpty(table.getId(), request));
        }

        @Test
        @DisplayName("Table의 주문 상태가 COMPLETION일 경우 Table의 비움 상태를 true로 변경한다.")
        void should_change_empty_when_order_status_is_completed() {
            // given
            final boolean emptyStatus = true;

            final OrderTable table = new OrderTable(0);
            table.setEmpty(!emptyStatus);
            orderTableRepository.save(table);

            final Order order = new Order(table, new OrderLineItems(List.of()));
            order.updateOrderStatus(OrderStatus.COMPLETION);
            orderRepository.save(order);

            // when
            final OrderTableRequest request = new OrderTableRequest(null, 0, emptyStatus);
            tableService.changeEmpty(table.getId(), request);

            // then
            final OrderTable actual = orderTableRepository.findById(table.getId()).get();

            Assertions.assertEquals(emptyStatus, actual.isEmpty());
        }
    }

    @Nested
    @DisplayName("주문 테이블 인원수 변경 테스트")
    class ChangeNumberOfGuestsTest {

        @ParameterizedTest
        @CsvSource(value = {"0", "1", "100"})
        @DisplayName("테이블이 비어있지 않고 손님의 수가 0 이상일 경우 정상적으로 업데이트된다.")
        void changeEmptyTest(final int expectNumberOfGuests) {
            // given
            final OrderTable table = orderTableRepository.save(new OrderTable(1));

            // when
            final OrderTableRequest request = new OrderTableRequest(null, expectNumberOfGuests, false);

            tableService.changeNumberOfGuests(table.getId(), request);

            // then
            final OrderTable actual = orderTableRepository.findById(table.getId()).get();

            Assertions.assertEquals(expectNumberOfGuests, actual.getNumberOfGuests());
        }

        @Test
        @DisplayName("테이블이 비어있지 않고 테이블이 존재하지 않을 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_table_does_not_exists() {
            // given
            final OrderTable table = new OrderTable(1);

            final OrderTableRequest request = new OrderTableRequest(null, 0, false);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(table.getId(), request));
        }

        @Test
        @DisplayName("비어있는 테이블의 인원수를 변경하면 IllegalArgumentException이 발생한다.")
        void should_throw_when_table_is_empty() {
            // given
            final OrderTable table = orderTableRepository.save(new OrderTable(0));

            final OrderTableRequest request = new OrderTableRequest(null, 0, false);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(table.getId(), request));
        }
    }
}
