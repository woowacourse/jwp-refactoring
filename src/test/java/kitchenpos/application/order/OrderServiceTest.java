package kitchenpos.application.order;

import static kitchenpos.domain.common.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.common.OrderStatus;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.request.OrderCreateRequest;
import kitchenpos.dto.order.request.OrderLineItemCreateRequest;
import kitchenpos.dto.order.request.OrderStatusChangeRequest;
import kitchenpos.dto.order.response.OrderResponse;
import kitchenpos.exception.badrequest.DuplicateOrderLineItemException;
import kitchenpos.exception.badrequest.MenuNotExistsException;
import kitchenpos.exception.badrequest.OrderNotExistsException;
import kitchenpos.exception.badrequest.OrderTableEmptyException;
import kitchenpos.exception.badrequest.OrderTableNotExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderService orderService;

    @Test
    void 주문을_생성할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(new Menu("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId, List.of(orderLineItemRequest));

        OrderResponse orderResponse = orderService.create(orderRequest);

        assertAll(
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @Test
    void 생성하려는_주문의_주문_항목에_중복된_메뉴가_들어갈_경우_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(new Menu("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId,
                List.of(orderLineItemRequest, orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(DuplicateOrderLineItemException.class);
    }

    @Test
    void 생성하려는_주문의_주문_항목에_존재하지_않는_메뉴가_포함될_경우_예외를_반환한다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(0L, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId, List.of(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(MenuNotExistsException.class);
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(new Menu("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(0L,
                List.of(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(OrderTableNotExistsException.class);
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_빈_주문_테이블이면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(new Menu("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(new OrderTable(1, true))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId, List.of(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(OrderTableEmptyException.class);
    }

    @Test
    void 모든_주문_목록을_조회할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(new Menu("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId, List.of(orderLineItemRequest));
        Long orderId = orderService.create(orderRequest)
                .getId();

        List<OrderResponse> actual = orderService.list();

        assertThat(actual).hasSize(1)
                .extracting("id")
                .containsExactly(orderId);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(new Menu("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId, List.of(orderLineItemRequest));
        Long orderId = orderService.create(orderRequest)
                .getId();

        OrderResponse changed = orderService.changeOrderStatus(orderId, new OrderStatusChangeRequest(MEAL.name()));

        assertThat(changed.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    void 상태를_변경하려는_주문이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, new OrderStatusChangeRequest(MEAL.name())))
                .isInstanceOf(OrderNotExistsException.class);
    }
}
