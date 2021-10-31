package kitchenpos.dao;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.domain.*;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderLineItemDao 테스트")
@SpringBootTest
@Transactional
class OrderLineItemDaoTest {
    @Autowired
    private OrderLineItemDao orderLineItemDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;

    private Menu menu;
    private Order order;
    private OrderTable orderTable;
    private OrderLineItem orderLineItem;

    private static Stream<Arguments> saveFailureWhenDbLimit() {
        return Stream.of(
                Arguments.of(0L, OrderLineItemFixture.FIRST_FIRST_ORDERLINE.getMenuId()),
                Arguments.of(null, OrderLineItemFixture.FIRST_FIRST_ORDERLINE.getMenuId()),
                Arguments.of(OrderLineItemFixture.FIRST_FIRST_ORDERLINE.getOrderId(), 0L),
                Arguments.of(OrderLineItemFixture.FIRST_FIRST_ORDERLINE.getOrderId(), null)
        );
    }

    @BeforeEach
    void init() {
        menu = menuDao.save(MenuFixture.후라이드치킨_NO_KEY);
        orderTable = orderTableDao.save(OrderTableFixture.TABLE_BEFORE_SAVE);

        final Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        this.order = orderDao.save(order);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setOrderId(this.order.getId());
        orderLineItem.setQuantity(2);
        this.orderLineItem = orderLineItemDao.save(orderLineItem);
    }

    @DisplayName("주문항목 저장 - 실패 - DB제약사항")
    @CustomParameterizedTest
    @MethodSource("saveFailureWhenDbLimit")
    void saveFailureWhenDbLimit(@AggregateWith(OrderLineItemAggregator.class) OrderLineItem orderLineItem) {
        //given
        //when
        //then
        assertThatThrownBy(() -> orderLineItemDao.save(orderLineItem))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("주문항목 조회 - 성공 - id 기반 조회")
    @Test
    void findById() {
        //given
        //when
        final Optional<OrderLineItem> actual = orderLineItemDao.findById(orderLineItem.getSeq());
        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getOrderId()).isEqualTo(orderLineItem.getOrderId());
        assertThat(actual.get().getMenuId()).isEqualTo(orderLineItem.getMenuId());
        assertThat(actual.get().getQuantity()).isEqualTo(orderLineItem.getQuantity());
    }

    @DisplayName("주문항목 조회 - 성공 - 저장된 id가 없을때")
    @Test
    void findByIdFailureWhenNotFound() {
        //given
        //when
        final Optional<OrderLineItem> actual = orderLineItemDao.findById(0L);
        //then
        assertThat(actual).isEmpty();
    }

    @DisplayName("주문항목 조회 - 성공 - 전체 주문항목 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<OrderLineItem> actual = orderLineItemDao.findAll();
        //then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("주문항목 조회 - 성공 - orderId기반 전체 조회")
    @Test
    void findAllByOrderId() {
        //given
        //when
        final List<OrderLineItem> actual = orderLineItemDao.findAllByOrderId(order.getId());
        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get(0).getOrderId()).isEqualTo(order.getId());
        assertThat(actual.get(0).getMenuId()).isEqualTo(menu.getId());
    }

    @DisplayName("주문항목 조회 - 성공 - 저장된 id가 없을때")
    @Test
    void findAllByOrderIdFailureWhenNotFound() {
        //given
        //when
        final List<OrderLineItem> actual = orderLineItemDao.findAllByOrderId(0L);
        //then
        assertThat(actual).isEmpty();
    }

    private static class OrderLineItemAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrderId(accessor.getLong(0));
            orderLineItem.setMenuId(accessor.getLong(1));
            return orderLineItem;
        }
    }
}