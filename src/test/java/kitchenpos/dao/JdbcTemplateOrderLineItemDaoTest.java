package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;
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

import static kitchenpos.dao.DaoUtils.asList;
import static kitchenpos.fixture.OrderLineItemFixture.newOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class JdbcTemplateOrderLineItemDaoTest {

    private final JdbcTemplateOrderLineItemDao orderLineItemDao;

    @Autowired
    public JdbcTemplateOrderLineItemDaoTest(final DataSource dataSource) {
        this.orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
    }

    @DisplayName("Insert 쿼리 테스트")
    @Nested
    class InsertTest {

        @Test
        void save() {
            final var orderLineItem = newOrderLineItem(1L, 2L, 30);
            final var actual = orderLineItemDao.save(orderLineItem);

            assertThat(actual.getSeq()).isPositive();
            assertThat(actual.getOrderId()).isEqualTo(orderLineItem.getOrderId());
            assertThat(actual.getMenuId()).isEqualTo(orderLineItem.getMenuId());
            assertThat(actual.getQuantity()).isEqualTo(orderLineItem.getQuantity());
        }
    }


    @DisplayName("Select 쿼리 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class SelectTest {

        private final Map<Long, OrderLineItem> savedOrderLineItems = saveAll(
                newOrderLineItem(1L, 2L, 30),
                newOrderLineItem(1L, 3L, 40),
                newOrderLineItem(2L, 4L, 0)
        );

        private Map<Long, OrderLineItem> saveAll(final OrderLineItem... orderLineItems) {
            return Stream.of(orderLineItems)
                    .map(orderLineItemDao::save)
                    .collect(Collectors.toMap(OrderLineItem::getSeq, orderLineItem -> orderLineItem));
        }

        @Test
        void findAll() {
            final var actual = orderLineItemDao.findAll();
            final var expected = asList(savedOrderLineItems);
            DaoUtils.assertAllEquals(actual, expected, JdbcTemplateOrderLineItemDaoTest.this::assertEquals);
        }

        @Nested
        class findAllByOrderId {

            @ParameterizedTest(name = "result is not empty")
            @ValueSource(longs = 1L)
            void resultExist(final long orderId) {
                assert existByOrderId(savedOrderLineItems, orderId);

                final var actual = orderLineItemDao.findAllByOrderId(orderId);
                final var expected = asListByOrderId(savedOrderLineItems, orderId);
                DaoUtils.assertAllEquals(actual, expected, JdbcTemplateOrderLineItemDaoTest.this::assertEquals);
            }

            @ParameterizedTest(name = "result is empty")
            @ValueSource(longs = 10L)
            void resultEmpty(final long orderId) {
                assert !existByOrderId(savedOrderLineItems, orderId);

                final var actual = orderLineItemDao.findAllByOrderId(orderId);
                assertThat(actual).isEmpty();
            }

            private List<OrderLineItem> asListByOrderId(final Map<Long, OrderLineItem> orderLineItems,
                                                       final long menuId) {
                return orderLineItems.values()
                        .stream()
                        .filter(menuProduct -> menuProduct.getOrderId().equals(menuId))
                        .collect(Collectors.toUnmodifiableList());
            }

            private boolean existByOrderId(final Map<Long, OrderLineItem> orderLineItems, final long menuId) {
                return orderLineItems.values()
                        .stream()
                        .anyMatch(menuProduct -> menuProduct.getOrderId().equals(menuId));
            }
        }

        @Nested
        class findById {

            @ParameterizedTest(name = "success")
            @ValueSource(longs = 1L)
            void success(final long id) {
                assert savedOrderLineItems.containsKey(id);

                final var actual = orderLineItemDao.findById(id);
                final var expected = savedOrderLineItems.get(id);

                assertThat(actual).hasValueSatisfying(menuProduct ->
                        assertEquals(menuProduct, expected)
                );
            }

            @ParameterizedTest(name = "fail")
            @ValueSource(longs = 10L)
            void fail(final long id) {
                assert !savedOrderLineItems.containsKey(id);

                final var actual = orderLineItemDao.findById(id);
                assertThat(actual).isEmpty();
            }
        }
    }

    private void assertEquals(final OrderLineItem actual, final OrderLineItem expected) {
        assertThat(actual.getSeq()).isEqualTo(expected.getSeq());
        assertThat(actual.getOrderId()).isEqualTo(expected.getOrderId());
        assertThat(actual.getMenuId()).isEqualTo(expected.getMenuId());
        assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity());
    }
}
