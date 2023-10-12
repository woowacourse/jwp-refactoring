package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Nested
    class 주문을_생성할_때 {

        @Test
        void 정상적으로_생성한다() {
            Order 주문_엔티티_A = OrderFixture.주문_엔티티_A;
            OrderTable 주문_테이블_A = OrderTableFixture.주문_테이블_A;
            OrderLineItem 주문_아이템_엔티티_A = OrderLineItemFixture.주문_아이템_엔티티_A;
            given(menuDao.countByIdIn(any()))
                    .willReturn(Long.valueOf(주문_엔티티_A.getOrderLineItems().size()));
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(주문_테이블_A));
            given(orderDao.save(any(Order.class)))
                    .willReturn(주문_엔티티_A);
            given(orderLineItemDao.save(any(OrderLineItem.class)))
                    .willReturn(주문_아이템_엔티티_A);

            Order response = orderService.create(주문_엔티티_A);

            assertThat(response).usingRecursiveComparison().isEqualTo(주문_엔티티_A);
        }

        @Test
        void 주문_아이템이_없으면_예외가_발생한다() {
            Order 주문_엔티티_B = OrderFixture.주문_엔티티_B_주문_아이템_없음;

            assertThatThrownBy(() -> orderService.create(주문_엔티티_B))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_메뉴와_주문_아이템의_메뉴와_일치하지_않으면_예외가_발생한다() {
            Order 주문_엔티티_A = OrderFixture.주문_엔티티_A;
            given(menuDao.countByIdIn(anyList()))
                    .willReturn(0L);

            assertThatThrownBy(() -> orderService.create(주문_엔티티_A))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블을_찾을_수_없으면_예외가_발생한다() {
            Order 주문_엔티티_A = OrderFixture.주문_엔티티_A;
            given(menuDao.countByIdIn(any()))
                    .willReturn(Long.valueOf(주문_엔티티_A.getOrderLineItems().size()));
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.create(주문_엔티티_A))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_EMPTY_상태이면_예외가_발생한다() {
            Order 주문_엔티티_A = OrderFixture.주문_엔티티_A;
            OrderTable 주문_테이블_B = OrderTableFixture.주문_테이블_B_EMPTY_상태;
            given(menuDao.countByIdIn(any()))
                    .willReturn(Long.valueOf(주문_엔티티_A.getOrderLineItems().size()));
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(주문_테이블_B));

            assertThatThrownBy(() -> orderService.create(주문_엔티티_A))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 모든_주문_엔티티를_조회한다() {
        Order 주문_엔티티_A = OrderFixture.주문_엔티티_A;
        given(orderDao.findAll())
                .willReturn(List.of(주문_엔티티_A));

        List<Order> orders = orderService.list();

        assertThat(orders).contains(주문_엔티티_A);
    }

    @Nested
    class 주문_상태를_변경할_때 {

        @Test
        void 정상적으로_변경한다() {
            Order 주문_엔티티_A = OrderFixture.주문_엔티티_A;
            Order 주문_엔티티_B_식사_완료 = OrderFixture.주문_엔티티_B_완료;
            OrderLineItem 주문_아이템_엔티티_A = OrderLineItemFixture.주문_아이템_엔티티_A;
            given(orderDao.findById(anyLong()))
                    .willReturn(Optional.of(주문_엔티티_A));
            given(orderLineItemDao.findAllByOrderId(anyLong()))
                    .willReturn(List.of(주문_아이템_엔티티_A));

            Order changeOrder = orderService.changeOrderStatus(주문_엔티티_A.getId(), 주문_엔티티_B_식사_완료);

            assertThat(changeOrder.getOrderStatus()).isEqualTo(주문_엔티티_B_식사_완료.getOrderStatus());
        }

        @Test
        void 주문_상태가_완료인_주문의_상태를_변경하면_예외가_발생한다() {
            Order 주문_엔티티_B_식사_완료 = OrderFixture.주문_엔티티_B_완료;
            given(orderDao.findById(anyLong()))
                    .willReturn(Optional.of(주문_엔티티_B_식사_완료));

            assertThatThrownBy(() -> orderService.changeOrderStatus(주문_엔티티_B_식사_완료.getId(), 주문_엔티티_B_식사_완료))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
