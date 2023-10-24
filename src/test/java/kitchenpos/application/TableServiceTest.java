package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import support.fixture.OrderBuilder;
import support.fixture.TableBuilder;
import support.fixture.TableGroupBuilder;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;

    private static Stream<List<OrderTable>> listTest() {
        final OrderTable table1 = new TableBuilder().build();
        final OrderTable table2 = new TableBuilder().build();
        final OrderTable table3 = new TableBuilder().build();

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
        final OrderTable table = new TableBuilder().build();

        // when
        final OrderTable expect = tableService.create(table);

        // then
        final OrderTable actual = orderTableRepository.findById(expect.getId()).get();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("주문 테이블 목록을 조회한다.")
    void listTest(final List<OrderTable> tables) {
        // given
        final List<OrderTable> expect = orderTableRepository.findAll();
        expect.addAll(tables);

        orderTableRepository.saveAll(tables);

        // when & then
        final List<OrderTable> actual = tableService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
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
            final OrderTable notExistsTable = new TableBuilder().build();

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeEmpty(notExistsTableId, notExistsTable));
        }

        @Test
        @DisplayName("Table의 그룹 id가 존재할 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_tableGroupId_is_exists() {
            // given
            final TableGroup tableGroup = tableGroupRepository.save(new TableGroupBuilder().build());

            final OrderTable table = orderTableRepository.save(new TableBuilder()
                    .setTableGroup(tableGroup)
                    .build());

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeEmpty(table.getId(), table));
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("Table의 주문 상태가 COOKING 또는 MEAL일 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_order_status_is_cooking_or_meal(final OrderStatus orderStatus) {
            // given
            final OrderTable table = orderTableRepository.save(new TableBuilder().build());

            orderRepository.save(new OrderBuilder()
                    .setOrderTable(table)
                    .setOrderStatus(orderStatus)
                    .build());

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeEmpty(table.getId(), table));
        }

        @Test
        @DisplayName("Table의 주문 상태가 COMPLETION일 경우 Table의 비움 상태를 true로 변경한다.")
        void should_change_empty_when_order_status_is_completed() {
            // given
            final boolean emptyStatus = true;
            final OrderTable table = orderTableRepository.save(new TableBuilder()
                    .setEmpty(!emptyStatus)
                    .build());

            orderRepository.save(new OrderBuilder()
                    .setOrderTable(table)
                    .setOrderStatus(OrderStatus.COMPLETION)
                    .build());

            // when
            table.setEmpty(emptyStatus);
            tableService.changeEmpty(table.getId(), table);

            // then
            final OrderTable actual = orderTableRepository.findById(table.getId()).get();

            assertEquals(emptyStatus, actual.isEmpty());
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
            final OrderTable table = orderTableRepository.save(new TableBuilder()
                    .setEmpty(false)
                    .build());

            // when
            table.setNumberOfGuests(expectNumberOfGuests);
            tableService.changeNumberOfGuests(table.getId(), table);

            // then
            final OrderTable actual = orderTableRepository.findById(table.getId()).get();

            assertEquals(expectNumberOfGuests, actual.getNumberOfGuests());
        }

        @Test
        @DisplayName("테이블이 비어있지 않고 테이블이 존재하지 않을 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_table_does_not_exists() {
            // given
            final OrderTable table = new TableBuilder()
                    .setEmpty(false)
                    .build();

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(table.getId(), table));
        }

        @Test
        @DisplayName("비어있는 테이블의 인원수를 변경하면 IllegalArgumentException이 발생한다.")
        void should_throw_when_table_is_empty() {
            // given
            final OrderTable table = orderTableRepository.save(new TableBuilder()
                    .setEmpty(true)
                    .build());

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(table.getId(), table));
        }
    }
}
