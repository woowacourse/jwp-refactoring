package kitchenpos.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.product.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.exception.IllegalOrderStatusException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest extends ServiceBaseTest {

    @Autowired
    protected OrderService orderService;

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void create() {
        //given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Menu menu = menuRepository.save(new Menu("메뉴1", new Price(new BigDecimal(1000)), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, false));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 3L);

        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));

        //when
        final OrderResponse orderResponse = orderService.create(orderRequest);

        //then
        assertThat(orderResponse.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문 항목의 메뉴가 모두 존재해야한다.")
    void createValidOrderItem() {
        //given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, false));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(999L, 3L);

        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));

        //when&then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("메뉴를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 시 주문 테이블이 존재해야한다.")
    void createValidOrderTable() {
        //given
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(999L, 3L);
        final OrderRequest orderRequest = new OrderRequest(999L, List.of(orderLineItemRequest));

        //when&then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(OrderTableNotFoundException.class)
                .hasMessage("OrderTable을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다.")
    void list() {
        //given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Menu menu = menuRepository.save(new Menu("메뉴1", new Price(new BigDecimal(1000)), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, false));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 3L);
        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));
        final OrderResponse orderResponse = orderService.create(orderRequest);

        //when
        final List<OrderResponse> orderResponses = orderService.list();

        //then
        assertAll(
                () -> assertThat(orderResponses).hasSize(1),
                () -> assertThat(orderResponses.get(0).getOrderLineItems()).usingRecursiveComparison()
                        .ignoringFields("id", "orderLineItems.seq")
                        .isEqualTo(orderResponse.getOrderLineItems())
        );
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        //given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Menu menu = menuRepository.save(new Menu("메뉴1", new Price(new BigDecimal(1000)), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, false));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 3L);
        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));
        final OrderResponse orderResponse = orderService.create(orderRequest);
        final OrderStatusRequest orderStatusRequest = new OrderStatusRequest(MEAL);

        //when
        final OrderResponse chageOrderResponse = orderService.changeOrderStatus(orderResponse.getId(), orderStatusRequest);

        //then
        assertThat(chageOrderResponse.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    @DisplayName("상태를 변경하려는 주문은 존재해야한다.")
    void changeOrderStatusValidOrder() {
        //when&then
        assertThatThrownBy(() -> orderService.changeOrderStatus(999L, null))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("주문을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("완료된 주문은 상태를 변경할 수 없다.")
    void changeOrderStatusValidComplitionOrder() {
        //given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Menu menu = menuRepository.save(new Menu("메뉴1", new Price(new BigDecimal(1000)), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, false));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 3L);
        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));
        final OrderResponse orderResponse = orderService.create(orderRequest);
        final OrderStatusRequest orderStatusRequest = new OrderStatusRequest(COMPLETION);
        final OrderStatusRequest orderStatusRequest2 = new OrderStatusRequest(MEAL);

        //when
        final OrderResponse orderResponse2 = orderService.changeOrderStatus(orderResponse.getId(), orderStatusRequest);

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), orderStatusRequest2))
                .isInstanceOf(IllegalOrderStatusException.class)
                .hasMessage("잘못된 주문 상태입니다.");
    }
}
