package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

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

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(List.of(
            new OrderLineItem() {{
                setMenuId(1L);
                setQuantity(1L);
            }},
            new OrderLineItem() {{
                setMenuId(2L);
                setQuantity(2L);
            }}
        ));

        given(menuDao.countByIdIn(any()))
            .willReturn(2L);
        given(orderTableDao.findById(any()))
            .willReturn(Optional.of(new OrderTable() {{
                setId(1L);
                setEmpty(false);
            }}));
        given(orderDao.save(any()))
            .willReturn(new Order() {{
                setId(1L);
                setOrderTableId(1L);
                setOrderStatus(OrderStatus.COOKING.name());
                setOrderedTime(LocalDateTime.now());
            }});

        // when
        final Order created = orderService.create(order);

        // then
        assertThat(created.getId()).isEqualTo(order.getId());
        assertThat(created.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(created.getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(created.getOrderedTime()).isNotNull();
    }

    @DisplayName("주문의 주문 항목이 없으면 예외가 발생한다.")
    @Test
    void create_emptyOrderLineItems() {
        // given
        final Order order = new Order();
        order.setOrderLineItems(List.of());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 개수가 저장된 메뉴의 개수와 다르면 예외가 발생한다.")
    @Test
    void create_differentMenuSize() {
        // given
        final Order order = new Order();
        order.setOrderLineItems(List.of(
            new OrderLineItem() {{
                setMenuId(1L);
                setQuantity(1L);
            }},
            new OrderLineItem() {{
                setMenuId(2L);
                setQuantity(2L);
            }}
        ));

        given(menuDao.countByIdIn(any()))
            .willReturn(1L);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_failNotExistOrderTable() {
        // given
        final Order order = new Order();
        order.setOrderLineItems(List.of(
            new OrderLineItem() {{
                setMenuId(1L);
                setQuantity(1L);
            }},
            new OrderLineItem() {{
                setMenuId(2L);
                setQuantity(2L);
            }}
        ));
        order.setOrderTableId(0L);
        given(menuDao.countByIdIn(any()))
            .willReturn(2L);
        given(orderTableDao.findById(any()))
            .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어 있으면 예외가 발생한다.")
    @Test
    void create_failEmptyOrderTable() {
        // given
        final Order order = new Order();
        order.setOrderLineItems(List.of(
            new OrderLineItem() {{
                setMenuId(1L);
                setQuantity(1L);
            }},
            new OrderLineItem() {{
                setMenuId(2L);
                setQuantity(2L);
            }}
        ));
        order.setOrderTableId(1L);
        given(menuDao.countByIdIn(any()))
            .willReturn(2L);
        given(orderTableDao.findById(any()))
            .willReturn(Optional.of(new OrderTable() {{
                setId(1L);
                setEmpty(true);
            }}));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
