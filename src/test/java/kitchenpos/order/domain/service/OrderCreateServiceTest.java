package kitchenpos.order.domain.service;

import kitchenpos.EntityFactory;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemCreateRequest;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderCreateServiceTest {

    @Autowired
    private EntityFactory entityFactory;
    @Autowired
    private OrderCreateService orderCreateService;

    @Test
    @DisplayName("주문을 생성할 수 있다")
    void create() {
        //given
        final OrderTable orderTable = entityFactory.saveOrderTableWithNotEmpty();
        final Menu menu = entityFactory.saveMenu();
        final OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(menu.getId(), 2);

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem));

        //when
        final Order order = orderCreateService.create(request);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 항목이 없으면 예외가 발생한다")
    void create_fail1() {
        //given
        final OrderTable orderTable = entityFactory.saveOrderTableWithNotEmpty();

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), emptyList());

        //when, then
        assertThatThrownBy(() -> orderCreateService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 필요합니다.");
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 항목 메뉴의 개수와 실제 존재하는 메뉴의 개수가 다르면 예외가 발생한다")
    void create_fail2() {
        //given
        final OrderTable orderTable = entityFactory.saveOrderTableWithNotEmpty();
        final OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(0L, 2);

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem));

        //when, then
        assertThatThrownBy(() -> orderCreateService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 항목입니다.");
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 테이블이 존재하지 않으면 예외가 발생한다")
    void create_fail3() {
        //given
        final Menu menu = entityFactory.saveMenu();
        final OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(menu.getId(), 2);

        final OrderCreateRequest request = new OrderCreateRequest(0L, List.of(orderLineItem));

        //when, then
        assertThatThrownBy(() -> orderCreateService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 테이블이 빈 테이블이면 예외가 발생한다")
    void create_fail4() {
        //given
        final OrderTable orderTable = entityFactory.saveOrderTable();
        final Menu menu = entityFactory.saveMenu();
        final OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(menu.getId(), 2);

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem));

        //when, then
        assertThatThrownBy(() -> orderCreateService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 주문 테이블입니다.");
    }

}
