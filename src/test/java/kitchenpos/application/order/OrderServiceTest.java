package kitchenpos.application.order;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.order.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

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

    @Test
    void 주문테이블id_주문상태_주문한시간_주문항목을_받아서_주문_정보를_등록할_수_있다() {
        //given
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        OrderTable 단일_신규_테이블 = OrderTableFixture.단일_신규_테이블();
        OrderLineItem 후라이드_두마리_주문 = OrderLineItemFixture.후라이드_두마리_주문();
        Order 조리_상태의_주문 = OrderFixture.조리_상태의_주문();

        given(menuDao.countByIdIn(eq(List.of(1L)))).willReturn(1L);
        given(menuDao.findById(eq(메뉴_후라이드.getId()))).willReturn(Optional.of(메뉴_후라이드));
        given(orderTableDao.findById(eq(3L))).willReturn(Optional.of(단일_신규_테이블));
        given(orderDao.save(any(Order.class))).willReturn(조리_상태의_주문);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(후라이드_두마리_주문);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드_두마리_주문.getMenuId(),
                후라이드_두마리_주문.getQuantity());
        OrderRequest orderRequest = new OrderRequest(단일_신규_테이블.getId(), List.of(
                orderLineItemRequest));

        //when
        OrderResponse result = orderService.create(orderRequest);

        //then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getOrderTableId()).isEqualTo(단일_신규_테이블.getId());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderLineItemResponses().get(0).getOrderId()).isEqualTo(조리_상태의_주문.getId());
        assertThat(result.getOrderLineItemResponses().get(0).getMenuId()).isEqualTo(후라이드_두마리_주문.getMenuId());
        assertThat(result.getOrderLineItemResponses().get(0).getQuantity()).isEqualTo(후라이드_두마리_주문.getQuantity());
    }

    @Test
    void 주문_항목이_입력되지_않으면_예외처리한다() {
        //given
        OrderRequest orderRequest = new OrderRequest(1L, Collections.EMPTY_LIST);

        //when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문한_메뉴_정보가_등록되어_있지_않으면_예외처리한다() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(-1L, 2L);
        OrderRequest orderRequest = new OrderRequest(1L, List.of(orderLineItemRequest));

        given(menuDao.countByIdIn(eq(List.of(-1L)))).willReturn(0L);

        //when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문한_테이블_정보가_등록되어_있지_않으면_예외처리한다() {
        //given
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2L);
        OrderRequest orderRequest = new OrderRequest(-1L, List.of(orderLineItemRequest));

        given(menuDao.countByIdIn(eq(List.of(1L)))).willReturn(1L);
        given(orderTableDao.findById(eq(-1L))).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문이_등록되면_주문_상태는_조리중인_상태로_바뀌고_주문한_시간이_현재_시간으로_저장된다() {
        //given
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        OrderTable 단일_신규_테이블 = OrderTableFixture.단일_신규_테이블();
        OrderLineItem 후라이드_두마리_주문 = OrderLineItemFixture.후라이드_두마리_주문();

        given(menuDao.countByIdIn(eq(List.of(1L)))).willReturn(1L);
        given(menuDao.findById(eq(메뉴_후라이드.getId()))).willReturn(Optional.of(메뉴_후라이드));
        given(orderTableDao.findById(eq(단일_신규_테이블.getId()))).willReturn(Optional.of(단일_신규_테이블));
        given(orderDao.save(any(Order.class))).willAnswer(AdditionalAnswers.returnsFirstArg());
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(후라이드_두마리_주문);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드_두마리_주문.getMenuId(),
                후라이드_두마리_주문.getQuantity());
        OrderRequest orderRequest = new OrderRequest(단일_신규_테이블.getId(), List.of(orderLineItemRequest));


        //when
        OrderResponse result = orderService.create(orderRequest);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderedTime()).isNotNull();
    }

    @Test
    void 등록된_전체_주문_정보를_조회할_수_있다() {
        //given
        Order 조리_상태의_주문 = OrderFixture.조리_상태의_주문();
        OrderLineItem 후라이드_두마리_주문 = OrderLineItemFixture.후라이드_두마리_주문();

        given(orderDao.findAll()).willReturn(List.of(조리_상태의_주문));
        given(orderLineItemDao.findAllByOrderId(조리_상태의_주문.getId())).willReturn(List.of(후라이드_두마리_주문));

        //when
        List<OrderResponse> result = orderService.list();

        //then
        assertThat(result).hasSize(1);
    }

    @Test
    void 등록된_주문의_주문_상태를_변경할_수_있다() {
        //given
        Order 식사_상태의_주문 = OrderFixture.식사_상태의_주문();
        OrderLineItem 후라이드_두마리_주문 = OrderLineItemFixture.후라이드_두마리_주문();

        given(orderDao.findById(식사_상태의_주문.getId())).willReturn(Optional.of(식사_상태의_주문));
        given(orderDao.save(any(Order.class))).willAnswer(AdditionalAnswers.returnsFirstArg());

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드_두마리_주문.getMenuId(),
                후라이드_두마리_주문.getQuantity());
        OrderRequest orderRequest = new OrderRequest(식사_상태의_주문.getOrderTableId(),
                OrderStatus.COMPLETION.name(), List.of(orderLineItemRequest));

        Long requestedOrderId = 1L;


        //when
        OrderResponse response = orderService.changeOrderStatus(requestedOrderId, orderRequest);

        //then
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    void 등록된_주문의_주문_상태가_이미_완료처리된_상태라면_예외처리한다() {
        //given
        Order 계산완료_상태의_주문 = OrderFixture.계산완료_상태의_주문();
        OrderTable 단일_신규_테이블 = OrderTableFixture.단일_신규_테이블();
        OrderLineItem 후라이드_두마리_주문 = OrderLineItemFixture.후라이드_두마리_주문();

        given(orderDao.findById(계산완료_상태의_주문.getId())).willReturn(Optional.of(계산완료_상태의_주문));

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드_두마리_주문.getMenuId(),
                후라이드_두마리_주문.getQuantity());
        OrderRequest orderRequest = new OrderRequest(단일_신규_테이블.getId(), List.of(orderLineItemRequest));
        Long requestedOrderId = 1L;

        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(requestedOrderId, orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
