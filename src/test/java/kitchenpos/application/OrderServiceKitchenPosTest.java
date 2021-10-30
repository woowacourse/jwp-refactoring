package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceKitchenPosTest extends KitchenPosTestFixture {

    public static final long MENU_ID = 1L;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private MenuProduct menuProduct;
    private Product product;
    private Menu menu;

    private OrderTable orderTable;
    private OrderLineItem orderLineItem;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        product = 상품을_저장한다(
                1L,
                "강정치킨",
                BigDecimal.valueOf(17000)
        );
        menuProduct = 메뉴_상품을_저장한다(
                1L,
                MENU_ID,
                product.getId(),
                2L
        );
        menu = 메뉴를_저장한다(
                MENU_ID,
                "후라이드+후라이드",
                BigDecimal.valueOf(2000),
                null,
                Collections.singletonList(menuProduct)
        );
        orderTable = 주문_테이블을_저장한다(
                1L,
                null,
                2,
                false
        );
        orderLineItem = 주문_항목을_저장한다(
                1L,
                orderTable.getId(),
                menu.getId(),
                menuProduct.getQuantity()
        );
        orderLineItems = Collections.singletonList(orderLineItem);
    }

    @DisplayName("메뉴를 주문할 수 있다.")
    @Test
    void create() {
        // given
        Order order = 주문을_저장한다(
                null,
                orderTable.getId(),
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                orderLineItems
        );

        given(menuDao.countByIdIn(Collections.singletonList(menu.getId()))).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        Order expected = 주문을_저장한다(
                1L,
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                order.getOrderLineItems()
        );
        given(orderDao.save(order)).willReturn(expected);

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder).isEqualTo(expected);
        verify(menuDao, times(1)).countByIdIn(Collections.singletonList(menuProduct.getProductId()));
        verify(orderTableDao, times(1)).findById(order.getOrderTableId());
        verify(orderDao, times(1)).save(order);
    }

    @DisplayName("주문 내역을 조회할 수 있다.")
    @Test
    void list() {
        // given
        Order saveOrder1 = 주문을_저장한다(
                1L,
                orderTable.getId(),
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                orderLineItems
        );
        Order saveOrder2 = 주문을_저장한다(
                2L,
                orderTable.getId(),
                OrderStatus.MEAL.name(),
                LocalDateTime.now(),
                orderLineItems
        );

        given(orderDao.findAll()).willReturn(Arrays.asList(saveOrder1, saveOrder2));
        given(orderLineItemDao.findAllByOrderId(any(Long.class))).willReturn(orderLineItems);

        // when
        List<Order> results = orderService.list();

        // then
        assertThat(results).containsExactly(saveOrder1, saveOrder2);

        verify(orderDao, times(1)).findAll();
        verify(orderLineItemDao, times(2)).findAllByOrderId(any(Long.class));
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        Order saveOrder = 주문을_저장한다(
                1L,
                orderTable.getId(),
                OrderStatus.MEAL.name(),
                LocalDateTime.now(),
                orderLineItems
        );

        given(orderDao.findById(saveOrder.getId())).willReturn(Optional.of(saveOrder));
        given(orderLineItemDao.findAllByOrderId(saveOrder.getId())).willReturn(saveOrder.getOrderLineItems());

        Order expected = 주문을_저장한다(
                saveOrder.getId(),
                saveOrder.getOrderTableId(),
                OrderStatus.COOKING.name(),
                saveOrder.getOrderedTime(),
                saveOrder.getOrderLineItems()
        );

        // when
        Order changedOrder = orderService.changeOrderStatus(saveOrder.getId(), expected);

        // then
        assertThat(changedOrder).usingRecursiveComparison().isEqualTo(expected);

        verify(orderDao, times(1)).findById(saveOrder.getId());
        verify(orderDao, times(1)).save(saveOrder);
        verify(orderLineItemDao, times(1)).findAllByOrderId(saveOrder.getId());
    }
}