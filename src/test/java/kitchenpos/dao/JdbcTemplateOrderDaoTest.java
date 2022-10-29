package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kitchenpos.fixture.OrderFixture.newOrder;
import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class JdbcTemplateOrderDaoTest {

    private final JdbcTemplateOrderDao orderDao;

    @Autowired
    public JdbcTemplateOrderDaoTest(final DataSource dataSource) {
        this.orderDao = new JdbcTemplateOrderDao(dataSource);
    }

    @DisplayName("Insert 쿼리 테스트")
    @Nested
    class InsertTest {

        @Test
        void save() {
            final var order = newOrder(1L, OrderStatus.MEAL);
            final var actual = orderDao.save(order);

            assertThat(actual.getId()).isPositive();
            assertThat(actual.getOrderTableId()).isEqualTo(order.getOrderTableId());
            assertThat(actual.getOrderStatus()).isEqualTo(order.getOrderStatus());
            assertThat(actual.getOrderedTime()).isEqualTo(order.getOrderedTime());
        }
    }

    @DisplayName("Select 쿼리 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class SelectTest {

        final Map<Long, Order> savedOrders = saveAll(
                newOrder(1L, OrderStatus.COOKING),
                newOrder(1L, OrderStatus.COMPLETION),
                newOrder(2L, OrderStatus.COMPLETION)
        );

        private Map<Long, Order> saveAll(final Order... orders) {
            return Stream.of(orders)
                    .map(orderDao::save)
                    .collect(Collectors.toMap(Order::getId, order -> order));
        }

        @Test
        void findAll() {
            final var actual = orderDao.findAll();
            final var expected = DaoUtils.asList(savedOrders);

            DaoUtils.assertAllEquals(actual, expected, JdbcTemplateOrderDaoTest.this::assertEquals);
        }

        @Nested
        class findById {

            @ParameterizedTest(name = "success")
            @ValueSource(longs = 1L)
            void success(final long id) {
                assert savedOrders.containsKey(id);

                final var actual = orderDao.findById(id);
                final var expected = savedOrders.get(id);

                assertThat(actual).hasValueSatisfying(menuGroups ->
                        assertEquals(menuGroups, expected)
                );
            }

            @ParameterizedTest(name = "fail")
            @ValueSource(longs = 10L)
            void fail(final long id) {
                assert !savedOrders.containsKey(id);

                final var actual = orderDao.findById(id);
                assertThat(actual).isEmpty();
            }
        }

        @Nested
        class existsByOrderTableIdAndOrderStatusIn {

            @Test
            void exist() {
                final var orderTableId = 1L;
                final var orderStatuses = List.of("COOKING");
                assert containsBy(savedOrders, orderTableId, orderStatuses);

                final var actual = orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
                assertThat(actual).isTrue();
            }

            @Test
            void notExist() {
                final var orderTableId = 2L;
                final var orderStatuses = List.of("COOKING");
                assert !containsBy(savedOrders, orderTableId, orderStatuses);

                final var actual = orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
                assertThat(actual).isFalse();
            }

            private boolean containsBy(final Map<Long, Order> orders, final Long orderTableId,
                                       final List<String> orderStatuses) {
                return orders.values()
                        .stream()
                        .anyMatch(order -> order.getOrderTableId().equals(orderTableId)
                                && orderStatuses.contains(order.getOrderStatus()));
            }
        }

        @Nested
        class existsByOrderTableIdInAndOrderStatusIn {

            @Test
            void exist() {
                final var orderTableIds = List.of(1L, 2L);
                final var orderStatuses = List.of("COMPLETION");
                assert containsBy(savedOrders, orderTableIds, orderStatuses);

                final var actual = orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
                assertThat(actual).isTrue();
            }

            @Test
            void notExist() {
                final var orderTableIds = List.of(1L, 2L);
                final var orderStatuses = List.of("MEAL");
                assert !containsBy(savedOrders, orderTableIds, orderStatuses);

                final var actual = orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
                assertThat(actual).isFalse();
            }

            private boolean containsBy(final Map<Long, Order> orders, final List<Long> orderTableIds,
                                       final List<String> orderStatuses) {
                return orders.values()
                        .stream()
                        .anyMatch(order -> orderTableIds.contains(order.getOrderTableId())
                                && orderStatuses.contains(order.getOrderStatus()));
            }
        }
    }

    private void assertEquals(final Order actual, final Order expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId());
        assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
        assertThat(actual.getOrderedTime()).isEqualTo(expected.getOrderedTime());
    }
}
