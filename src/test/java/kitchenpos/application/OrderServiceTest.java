package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@DisplayName("OrderService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private OrderLineItem 후라이드치킨_2마리;
    private OrderLineItem 양념치킨_1마리;
    private OrderLineItem 후라이드치킨_2마리_주문1;
    private OrderLineItem 양념치킨_1마리_주문1;
    private OrderLineItem 양념치킨_1마리_주문2;

    @BeforeEach
    void setUp() {
        // given
        후라이드치킨_2마리 = new OrderLineItem(1L, 2);
        양념치킨_1마리 = new OrderLineItem(2L, 1);

        후라이드치킨_2마리_주문1 = new OrderLineItem(1L, 1L, 1L, 2);
        양념치킨_1마리_주문1 = new OrderLineItem(2L, 1L, 2L, 1);
        양념치킨_1마리_주문2 = new OrderLineItem(3L, 2L, 2L, 1);
    }

    @Test
    @DisplayName("주문을 등록할 수 있다. - 해당 주문은 조리중(COOKING) 상태가 된다.")
    void create() {
        // given
        Order order = new Order(1L, Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        OrderTable table = new OrderTable(1L, null, 4, false);
        Order expected = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        given(menuRepository.countByIdIn(Arrays.asList(후라이드치킨_2마리.getMenuId(), 양념치킨_1마리.getMenuId()))).willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(table));
        given(orderDao.save(order)).willReturn(expected);
        given(orderLineItemRepository.save(후라이드치킨_2마리)).willReturn(후라이드치킨_2마리_주문1);
        given(orderLineItemRepository.save(양념치킨_1마리)).willReturn(양념치킨_1마리_주문1);

        // when
        Order actual = orderService.create(order);

        // then
        assertEquals(OrderStatus.COOKING.name(), actual.getOrderStatus());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("메뉴 목록은 하나이상 있어야한다.")
    void createWrongOrderLineItemsEmpty() {
        // given
        Order order = new Order(1L, Collections.emptyList());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(order));
        assertEquals("주문하려면 하나 이상의 메뉴가 필요합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("메뉴 목록에 포함된 메뉴들은 모두 등록된 메뉴여야한다.")
    void createWrongOrderLineItemsNotRegister() {
        // given
        Order order = new Order(1L, Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        given(menuRepository.countByIdIn(Arrays.asList(후라이드치킨_2마리.getMenuId(), 양념치킨_1마리.getMenuId()))).willReturn(1L);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(order));
        assertEquals("등록되지 않은 메뉴는 주문할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문하려는 테이블은 존재해야 한다.")
    void createWrongTableNotExist() {
        // given
        Order order = new Order(1L, Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        given(menuRepository.countByIdIn(Arrays.asList(후라이드치킨_2마리.getMenuId(), 양념치킨_1마리.getMenuId()))).willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(order));
        assertEquals("존재하지 않는 테이블은 주문할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문하려는 테이블은 비어있지 않아야한다.")
    void createWrongTableEmpty() {
        // given
        OrderTable table = new OrderTable(1L, null, 4, true);
        Order order = new Order(1L, Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        given(menuRepository.countByIdIn(Arrays.asList(후라이드치킨_2마리.getMenuId(), 양념치킨_1마리.getMenuId()))).willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(table));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(order));
        assertEquals("빈 테이블은 주문할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("전체 주문을 조회할 수 있다.")
    void list() {
        // given
        Order order1 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        Order order2 = new Order(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(양념치킨_1마리));
        List<Order> expected = Arrays.asList(order1, order2);
        given(orderDao.findAll()).willReturn(expected);
        given(orderLineItemRepository.findAllByOrderId(order1.getId())).willReturn(Arrays.asList(후라이드치킨_2마리_주문1, 양념치킨_1마리_주문1));
        given(orderLineItemRepository.findAllByOrderId(order2.getId())).willReturn(Collections.singletonList(양념치킨_1마리_주문2));

        // when
        List<Order> actual = orderService.list();

        // then
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        Long orderId = 1L;
        Order changeStatusOrder = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        Order expected = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), Arrays.asList(후라이드치킨_2마리_주문1, 양념치킨_1마리_주문1));
        given(orderDao.findById(orderId)).willReturn(Optional.of(order));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemRepository.findAllByOrderId(orderId)).willReturn(Arrays.asList(후라이드치킨_2마리_주문1, 양념치킨_1마리_주문1));

        // when
        Order actual = orderService.changeOrderStatus(orderId, changeStatusOrder);

        // then
        assertEquals(OrderStatus.MEAL.name(), actual.getOrderStatus());
        assertThat(actual).usingRecursiveComparison().ignoringFields("orderedTime").isEqualTo(expected);
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문은 존재해야 한다.")
    void changeOrderStatusWrongOrderNotExist() {
        // given
        Long orderId = 1L;
        Order changeStatusOrder = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        given(orderDao.findById(orderId)).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(orderId, changeStatusOrder));
        assertEquals("존재하지 않는 주문의 상태는 변경할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문 상태는 조리중(COOKING)이나 식사중(MEAL)이어야한다.")
    void changeOrderStatusWrongOrderStatus() {
        // given
        Long orderId = 1L;
        Order changeStatusOrder = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        given(orderDao.findById(orderId)).willReturn(Optional.of(order));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(orderId, changeStatusOrder));
        assertEquals("계산 완료된 주문의 상태는 변경할 수 없습니다.", exception.getMessage());
    }
}
