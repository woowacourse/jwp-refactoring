//package kitchenpos.application;
//
//import kitchenpos.dao.MenuDao;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderLineItemDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderLineItem;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.fixture.OrderFixture;
//import kitchenpos.fixture.OrderLineItemFixture;
//import kitchenpos.fixture.OrderTableFixture;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertIterableEquals;
//import static org.mockito.BDDMockito.given;
//
//@DisplayName("OrderService 테스트")
//@ExtendWith(MockitoExtension.class)
//public class OrderServiceTest {
//
//    @Mock
//    private MenuDao menuDao;
//    @Mock
//    private OrderDao orderDao;
//    @Mock
//    private OrderLineItemDao orderLineItemDao;
//    @Mock
//    private OrderTableDao orderTableDao;
//    @InjectMocks
//    private OrderService orderService;
//
//    private final OrderFixture orderFixture = new OrderFixture();
//    private final OrderLineItemFixture orderLineItemFixture = new OrderLineItemFixture();
//    private final OrderTableFixture orderTableFixture = new OrderTableFixture();
//
//    private OrderLineItem 주문_메뉴1;
//    private OrderLineItem 주문_메뉴2;
//    private List<OrderLineItem> 주문_메뉴들;
//
//    @BeforeEach
//    void setup() {
//        주문_메뉴1 = orderLineItemFixture.주문_메뉴_생성(1L, 1L, 1);
//        주문_메뉴2 = orderLineItemFixture.주문_메뉴_생성(1L, 2L, 1);
//        주문_메뉴들 = orderLineItemFixture.주문_메뉴_리스트_생성(주문_메뉴1, 주문_메뉴2);
//    }
//
//    @Test
//    @DisplayName("주문 생성 테스트 - 성공")
//    void createTest() {
//        // given
//        Order 주문1 = orderFixture.주문_생성(1L, OrderStatus.COOKING.name(), 주문_메뉴들);
//        List<Long> 주문1_메뉴_Ids = 주문1.getOrderLineItems().stream()
//                .map(OrderLineItem::getMenuId)
//                .collect(Collectors.toList());
//        OrderTable 주문1_테이블 = orderTableFixture.주문_테이블_생성(1L, 2, false);
//        given(menuDao.countByIdIn(주문1_메뉴_Ids))
//                .willReturn(Long.valueOf(주문1.getOrderLineItems().size()));
//        given(orderTableDao.findById(주문1.getOrderTableId())).willReturn(Optional.of(주문1_테이블));
//
//        Order expected = orderFixture.주문_생성(1L, 1L, OrderStatus.COOKING.name(), 주문_메뉴들);
//        given(orderDao.save(주문1)).willReturn(expected);
//
//        OrderLineItem 주문_메뉴1_expected = orderLineItemFixture.주문_메뉴_생성(1L, 1L, 1L, 1);
//        OrderLineItem 주문_메뉴2_expected = orderLineItemFixture.주문_메뉴_생성(1L, 1L, 2L, 1);
//
//        given(orderLineItemDao.save(주문_메뉴1)).willReturn(주문_메뉴1_expected);
//        given(orderLineItemDao.save(주문_메뉴2)).willReturn(주문_메뉴2_expected);
//
//        // when
//        Order actual = orderService.create(주문1);
//
//        // then
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @DisplayName("주문 리스트 조회 테스트 - 성공")
//    void listTest() {
//        // given
//        Order 주문1 = orderFixture.주문_생성(1L, 1L, OrderStatus.COOKING.name(), 주문_메뉴들);
//        given(orderDao.findAll()).willReturn(Collections.singletonList(주문1));
//        given(orderLineItemDao.findAllByOrderId(주문1.getId())).willReturn(주문_메뉴들);
//        List<Order> expected = Arrays.asList(주문1);
//        // when
//        List<Order> actual = orderService.list();
//
//        // then
//        assertIterableEquals(expected, actual);
//    }
//
//    @Test
//    @DisplayName("주문 상태 변경 테스트 - 성공")
//    void changeOrderStatus() {
//        // given
//        Long 주문1_id = 1L;
//        Order 주문1 = orderFixture.주문_생성(1L, 1L, OrderStatus.COOKING.name(), 주문_메뉴들);
//        Order 주문1_상태변경 = orderFixture.주문_생성(1L, OrderStatus.MEAL.name(), 주문_메뉴들);
//        given(orderDao.findById(주문1_id)).willReturn(Optional.of(주문1));
//        Order expected = orderFixture.주문_생성(1L, 1L, OrderStatus.MEAL.name(), 주문_메뉴들);
//        given(orderDao.save(주문1)).willReturn(expected);
//        given(orderLineItemDao.findAllByOrderId(주문1_id)).willReturn(주문_메뉴들);
//
//        // when
//        Order actual = orderService.changeOrderStatus(주문1_id, 주문1_상태변경);
//
//        // then
//        assertEquals(expected.getOrderStatus(), actual.getOrderStatus());
//    }
//}
