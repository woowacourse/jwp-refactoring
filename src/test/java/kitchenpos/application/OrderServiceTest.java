package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.DomainFixture.createMenu;
import static kitchenpos.fixture.DomainFixture.createMenuGroup;
import static kitchenpos.fixture.DomainFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotConvertableStatusException;
import kitchenpos.exception.NotFoundMenuException;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderMenusCountException;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.request.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends ServiceTest {

    private OrderTable orderTable;
    private Menu menu;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup());
        orderTable = orderTableDao.save(createOrderTable());
        menu = menuDao.save(createMenu(menuGroup.getId()));
    }

    @Test
    void 주문을_생성한다() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(new OrderLineItemDto(menu.getId(), 1)));

        Order savedOrder = orderService.create(orderCreateRequest);

        assertThat(orderDao.findById(savedOrder.getId())).isPresent();
    }

    @Test
    void 주문을_생성할때_orderLineItems가_비었으면_예외를_발생한다() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of());

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(OrderMenusCountException.class);
    }

    @Test
    void 주문을_생성할때_메뉴의수가_다르면_예외를_발생한다() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(new OrderLineItemDto(0L, 1)));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(NotFoundMenuException.class);
    }

    @Test
    void 주문을_생성할때_orderTableId가_존재하지않으면_예외를_발생한다() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(0L,
                List.of(new OrderLineItemDto(menu.getId(), 1)));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    void 주문_목록을_반환한다() {
        int beforeSize = orderService.list().size();
        orderDao.save(new Order(orderTable.getId(), COOKING, LocalDateTime.now()));

        assertThat(orderService.list().size()).isEqualTo(beforeSize + 1);
    }

    @Test
    void 주문_상태를_변경한다() {
        Order savedOrder = orderDao.save(new Order(orderTable.getId(), COOKING, LocalDateTime.now()));

        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(MEAL.name());
        orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusRequest);

        assertThat(orderDao.findById(savedOrder.getId()).get().getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    void 주문_상태를_변경할때_유효하지않은_아이디면_예외를_반환한다() {
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, changeOrderStatusRequest))
                .isInstanceOf(NotFoundOrderException.class);
    }

    @Test
    void 주문_상태를_변경할때_완료상태면_예외를_반환한다() {
        Order savedOrder = orderDao.save(new Order(orderTable.getId(), COMPLETION, LocalDateTime.now()));
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusRequest))
                .isInstanceOf(NotConvertableStatusException.class);
    }
}
