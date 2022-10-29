package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
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
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId,
                List.of(orderLineItemRequest));

        OrderResponse orderResponse = orderService.create(orderRequest);

        assertAll(
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @Test
    void 생성하려는_주문에_주문_항목이_없으면_예외를_반환한다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId, List.of());

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(0L,
                List.of(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_빈_주문_테이블이면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId,
                List.of(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_주문_목록을_조회할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId,
                List.of(orderLineItemRequest));
        Long orderId = orderService.create(orderRequest)
                .getId();

        List<OrderResponse> actual = orderService.list();

        assertThat(actual).hasSize(1)
                .extracting("id")
                .containsExactly(orderId);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId,
                List.of(orderLineItemRequest));
        Long orderId = orderService.create(orderRequest)
                .getId();

        OrderResponse changed = orderService.changeOrderStatus(orderId, new OrderStatusChangeRequest(MEAL.name()));

        assertThat(changed.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    void 상태를_변경하려는_주문이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, new OrderStatusChangeRequest(MEAL.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상태를_변경하려는_주문이_완료된_주문이면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menuId, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId,
                List.of(orderLineItemRequest));
        Long orderId = orderService.create(orderRequest)
                .getId();
        orderService.changeOrderStatus(orderId, new OrderStatusChangeRequest(COMPLETION.name()));

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, new OrderStatusChangeRequest(MEAL.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
