package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.response.OrderLineItemResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderCompletionException;
import kitchenpos.exception.OrderLineItemEmptyException;
import kitchenpos.exception.OrderLineItemSizeException;
import kitchenpos.exception.TableEmptyException;
import kitchenpos.support.fixtures.MenuFixtures;
import kitchenpos.support.fixtures.MenuGroupFixtures;
import kitchenpos.support.fixtures.OrderFixtures;
import kitchenpos.support.fixtures.OrderLineItemFixtures;
import kitchenpos.support.fixtures.OrderTableFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:truncate.sql")
@SpringBootTest
class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable notEmptyOrderTable = orderTableRepository.save(orderTable);

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(notEmptyOrderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        // when
        final OrderResponse saved = orderService.create(orderCreateRequest);
        final OrderLineItemResponse actual = OrderLineItemResponse.from(
                new OrderLineItem(null, null, savedMenu.getId(), 2));

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(saved.getOrderedTime()).isBeforeOrEqualTo(LocalDateTime.now()),
                () -> assertThat(saved.getOrderTableId()).isEqualTo(notEmptyOrderTable.getId()),
                () -> assertThat(saved.getOrderLineItems()).usingRecursiveFieldByFieldElementComparator()
                        .usingElementComparatorOnFields("menuId", "quantity")
                        .contains(actual)
        );
    }

    @Test
    @DisplayName("주문 항목이 비어있는 상태로 주문을 생성하면 예외가 발생한다")
    void createExceptionEmptyOrderLineItems() {
        // given
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isExactlyInstanceOf(OrderLineItemEmptyException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴들이 실제 존재하지 않은 메뉴로 주문을 생성하면 예외가 발생한다")
    void createExceptionWrongOrderLineItems() {
        // given
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(-1L, 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L,
                Collections.singletonList(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isExactlyInstanceOf(OrderLineItemSizeException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않은 테이블이면 주문을 생성할 때 예외가 발생한다")
    void createExceptionNotExistOrderTable() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(-1L,
                Collections.singletonList(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isExactlyInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블이 주문을 등록할 수 없는 상태로 주문을 생성할 때 예외가 발생한다")
    void createExceptionNotEmptyOrderTable() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isExactlyInstanceOf(TableEmptyException.class);
    }

    @Test
    @DisplayName("모든 주문을 조회한다")
    void list() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable notEmptyOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu.getId(), 2);
        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableIdAndOrderLineItems(
                notEmptyOrderTable.getId(), orderLineItem);
        final Order savedOrder = orderRepository.save(order);

        // when
        final List<OrderResponse> orders = orderService.list();

        // then
        assertAll(
                () -> assertThat(orders).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(orders).extracting("id")
                        .contains(savedOrder.getId()),
                () -> assertThat(orders).extracting("orderLineItems")
                        .isNotEmpty()
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경한다")
    void changeOrderStatus() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu.getId(), 2);
        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableIdAndOrderLineItems(savedOrderTable.getId(),
                orderLineItem);
        final Order saved = orderRepository.save(order);

        // when
        final OrderResponse changedOrder = orderService.changeOrderStatus(saved.getId(), OrderStatus.MEAL.name());

        // when, then
        assertAll(
                () -> assertThat(changedOrder.getId()).isEqualTo(saved.getId()),
                () -> assertThat(changedOrder.getOrderedTime()).isEqualTo(saved.getOrderedTime()),
                () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
        );
    }

    @Test
    @DisplayName("변경하려는 주문이 존재하지 않을 때 주문의 상태를 변경하려고 하면 예외가 발생한다.")
    void changeOrderStatusExceptionWrongOrderId() {
        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, OrderStatus.COOKING.name()))
                .isExactlyInstanceOf(NotFoundOrderException.class);
    }

    @Test
    @DisplayName("주문이 이미 완료된 상태에서 주문의 상태를 변경하려고 하면 예외가 발생한다.")
    void changeOrderStatusExceptionAlreadyCompletion() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu.getId(), 2);
        final Order order = OrderFixtures.COMPLETION_ORDER.createWithOrderTableIdAndOrderLineItems(
                savedOrderTable.getId(), orderLineItem);
        final Order completionOrder = orderRepository.save(order);

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrder.getId(), OrderStatus.MEAL.name()))
                .isExactlyInstanceOf(OrderCompletionException.class);
    }
}
