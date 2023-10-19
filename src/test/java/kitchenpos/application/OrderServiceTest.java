//package kitchenpos.application;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import kitchenpos.dao.MenuDao;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderLineItemDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderLineItem;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.fixture.OrderTableFixture;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.AdditionalAnswers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@SuppressWarnings("NonAsciiCharacters")
//@ExtendWith(MockitoExtension.class)
//public class OrderServiceTest {
//
//    @Mock
//    private MenuDao menuDao;
//
//    @Mock
//    private OrderDao orderDao;
//
//    @Mock
//    private OrderLineItemDao orderLineItemDao;
//
//    @Mock
//    private OrderTableDao orderTableDao;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    private OrderTable savedOrderTable;
//    private Order savedOrder;
//    private OrderLineItem savedOrderLineItem;

//    @BeforeEach
//    void setUp() {
//        savedOrderTable = OrderTableFixture.신규_테이블1();
//
//        savedOrder = new Order();
//        savedOrder.setId(1L);
//        savedOrder.setOrderTableId(savedOrderTable.getId());
//        savedOrder.setOrderStatus(OrderStatus.COOKING.name());
//        savedOrder.setOrderedTime(LocalDateTime.now());
//
//        savedOrderLineItem = new OrderLineItem();
//        savedOrderLineItem.setSeq(1L);
//        savedOrderLineItem.setOrderId(savedOrder.getId());
//        savedOrderLineItem.setMenuId(1L);
//        savedOrderLineItem.setQuantity(2L);
//    }
//
//    @Test
//    void 주문테이블id_주문상태_주문한시간_주문항목을_받아서_주문_정보를_등록할_수_있다() {
//        //given
//        OrderLineItem orderLineItemRequest = new OrderLineItem();
//        orderLineItemRequest.setMenuId(1L);
//        orderLineItemRequest.setQuantity(2L);
//
//        Order orderRequest = new Order();
//        orderRequest.setOrderTableId(1L);
//        orderRequest.setOrderLineItems(List.of(orderLineItemRequest));
//
//        given(menuDao.countByIdIn(eq(List.of(1L)))).willReturn(1L);
//        given(orderTableDao.findById(eq(1L))).willReturn(Optional.of(savedOrderTable));
//        given(orderDao.save(any(Order.class))).willReturn(savedOrder);
//        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(savedOrderLineItem);
//
//        //when
//        Order result = orderService.create(orderRequest);
//
//        //then
//        assertThat(result.getId()).isEqualTo(1L);
//        assertThat(result.getOrderTableId()).isEqualTo(savedOrderTable.getId());
//        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
//        assertThat(result.getOrderLineItems().get(0).getOrderId()).isEqualTo(savedOrder.getId());
//        assertThat(result.getOrderLineItems().get(0).getMenuId()).isEqualTo(orderLineItemRequest.getMenuId());
//        assertThat(result.getOrderLineItems().get(0).getQuantity()).isEqualTo(orderLineItemRequest.getQuantity());
//    }
//
//    @Test
//    void 주문_항목이_입력되지_않으면_예외처리한다() {
//        //given
//        Order orderRequest = new Order();
//        orderRequest.setOrderTableId(1L);
//        orderRequest.setOrderLineItems(Collections.EMPTY_LIST);
//
//        //when, then
//        assertThatThrownBy(() -> orderService.create(orderRequest))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문한_메뉴_정보가_등록되어_있지_않으면_예외처리한다() {
//        OrderLineItem orderLineItemRequest = new OrderLineItem();
//        orderLineItemRequest.setMenuId(-1L);
//        orderLineItemRequest.setQuantity(2L);
//
//        Order orderRequest = new Order();
//        orderRequest.setOrderTableId(1L);
//        orderRequest.setOrderLineItems(List.of(orderLineItemRequest));
//
//        given(menuDao.countByIdIn(eq(List.of(-1L)))).willReturn(0L);
//
//        //when, then
//        assertThatThrownBy(() -> orderService.create(orderRequest))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문한_테이블_정보가_등록되어_있지_않으면_예외처리한다() {
//        //given
//        OrderLineItem orderLineItemRequest = new OrderLineItem();
//        orderLineItemRequest.setMenuId(1L);
//        orderLineItemRequest.setQuantity(2L);
//
//        Order orderRequest = new Order();
//        orderRequest.setOrderTableId(-1L);
//        orderRequest.setOrderLineItems(List.of(orderLineItemRequest));
//
//        given(menuDao.countByIdIn(eq(List.of(1L)))).willReturn(1L);
//        given(orderTableDao.findById(eq(-1L))).willReturn(Optional.empty());
//
//        //when, then
//        assertThatThrownBy(() -> orderService.create(orderRequest))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문이_등록되면_주문_상태는_조리중인_상태로_바뀌고_주문한_시간이_현재_시간으로_저장된다() {
//        //given
//        OrderLineItem orderLineItemRequest = new OrderLineItem();
//        orderLineItemRequest.setMenuId(1L);
//        orderLineItemRequest.setQuantity(2L);
//
//        Order orderRequest = new Order();
//        orderRequest.setOrderTableId(1L);
//        orderRequest.setOrderLineItems(List.of(orderLineItemRequest));
//
//        given(menuDao.countByIdIn(eq(List.of(1L)))).willReturn(1L);
//        given(orderTableDao.findById(eq(1L))).willReturn(Optional.of(savedOrderTable));
//        given(orderDao.save(any(Order.class))).willAnswer(AdditionalAnswers.returnsFirstArg());
//        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(savedOrderLineItem);
//
//        //when
//        Order result = orderService.create(orderRequest);
//
//        //then
//        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
//        assertThat(result.getOrderedTime()).isNotNull();
//    }
//
//    @Test
//    void 등록된_전체_주문_정보를_조회할_수_있다() {
//        //given
//        given(orderDao.findAll()).willReturn(List.of(savedOrder));
//        given(orderLineItemDao.findAllByOrderId(savedOrder.getId())).willReturn(List.of(savedOrderLineItem));
//
//        //when
//        List<Order> result = orderService.list();
//
//        //then
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    void 등록된_주문의_주문_상태를_변경할_수_있다() {
//        //given
//        OrderLineItem orderLineItemRequest = new OrderLineItem();
//        orderLineItemRequest.setMenuId(1L);
//        orderLineItemRequest.setQuantity(2L);
//
//        Order orderRequest = new Order();
//        orderRequest.setOrderTableId(1L);
//        orderRequest.setOrderLineItems(List.of(orderLineItemRequest));
//        orderRequest.setOrderStatus(OrderStatus.COMPLETION.name());
//
//        Long requestedOrderId = 1L;
//
//        given(orderDao.findById(requestedOrderId)).willReturn(Optional.of(savedOrder));
//        given(orderDao.save(any(Order.class))).willAnswer(AdditionalAnswers.returnsFirstArg());
//
//        //when
//        Order response = orderService.changeOrderStatus(requestedOrderId, orderRequest);
//
//        //then
//        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
//    }
//
//    @Test
//    void 등록된_주문의_주문_상태가_이미_완료처리된_상태라면_예외처리한다() {
//        //given
//        OrderLineItem orderLineItemRequest = new OrderLineItem();
//        orderLineItemRequest.setMenuId(1L);
//        orderLineItemRequest.setQuantity(2L);
//
//        Order orderRequest = new Order();
//        orderRequest.setOrderTableId(1L);
//        orderRequest.setOrderLineItems(List.of(orderLineItemRequest));
//
//        Long requestedOrderId = 1L;
//
//        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
//        given(orderDao.findById(requestedOrderId)).willReturn(Optional.of(savedOrder));
//
//        //when
//        assertThatThrownBy(() -> orderService.changeOrderStatus(requestedOrderId, orderRequest))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//}
