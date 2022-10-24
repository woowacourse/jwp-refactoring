package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.MenuFixture.MENU_PRICE_10000;
import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static kitchenpos.support.OrderFixture.ORDER_COMPLETION_1;
import static kitchenpos.support.OrderFixture.ORDER_COOKING_1;
import static kitchenpos.support.OrderLineItemFixture.ORDER_LINE_ITEM_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_EMPTY_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_NOT_EMPTY_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Test
    void 주문을_저장한다() {
        // given
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Long menuId = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId))
                .getId();

        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성()).getId();
        final Order order = ORDER_COOKING_1.생성(orderTableId, List.of(ORDER_LINE_ITEM_1.생성(menuId)));

        // when
        final Order savedOrder = orderService.create(order);

        //then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    void 주문을_저장할_때_주문할_메뉴가_1개_이상이_아니면_예외를_반환한다() {
        // given
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성()).getId();
        final Order order = ORDER_COOKING_1.생성(orderTableId);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_저장할_때_존재하지_않는_메뉴를_주문하면_예외를_발생한다() {
        // given
        final long notExistMenuId = Long.MAX_VALUE;
        final OrderLineItem orderLineItem = ORDER_LINE_ITEM_1.생성(notExistMenuId);

        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성()).getId();
        final Order order = ORDER_COOKING_1.생성(orderTableId, List.of(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_저장할_때_존재하지_않는_테이블_번호로_주문하면_예외를_발생한다() {
        // given
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Long menuId = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId))
                .getId();

        final long notExistOrderTableId = Long.MAX_VALUE;
        final Order order = ORDER_COOKING_1.생성(notExistOrderTableId, List.of(ORDER_LINE_ITEM_1.생성(menuId)));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_저장할_때_테이블이_비어있다면_예외를_발생한다() {
        // given
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Long menuId = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId))
                .getId();

        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성()).getId();
        final Order order = ORDER_COOKING_1.생성(orderTableId, List.of(ORDER_LINE_ITEM_1.생성(menuId)));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_주문을_조회할_때_주문한_메뉴도_함께_조회한다() {
        // given
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Long menuId = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId))
                .getId();

        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성()).getId();
        final Order savedOrder = 주문을_저장한다(ORDER_COOKING_1.생성(orderTableId));
        final OrderLineItem savedOrderLineItem = 주문항목을_저장한다(ORDER_LINE_ITEM_1.생성(savedOrder.getId(), menuId));

        // when
        final List<Order> orders = orderService.list();

        // then
        final Optional<Order> foundOrder = orders.stream()
                .filter(order -> order.getId().equals(savedOrder.getId()))
                .findFirst();

        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getOrderLineItems()).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(savedOrderLineItem);
    }

    @Test
    void 주문의_상태를_변경할_수_있다() {
        // given
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성()).getId();
        final Order savedOrder = 주문을_저장한다(ORDER_COOKING_1.생성(orderTableId));

        final Order updateFor = new Order(savedOrder.getOrderTableId(), COMPLETION.name(), savedOrder.getOrderedTime());

        // when
        final Order changedOrderStatus = orderService.changeOrderStatus(savedOrder.getId(), updateFor);

        // then
        assertThat(changedOrderStatus.getOrderStatus()).isEqualTo(COMPLETION.name());
    }

    @Test
    void 주문을_변경할_때_존재하지_않는_주문이면_예외를_반환한다() {
        // given
        final long notExistOrderId = Long.MAX_VALUE;

        final Order updateFor = new Order(1L, COMPLETION.name(), LocalDateTime.now());

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, updateFor))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_변경할_때_이미_완료된_주문이면_예외를_반환한다() {
        // given
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성()).getId();
        final Order savedOrder = 주문을_저장한다(ORDER_COMPLETION_1.생성(orderTableId));

        final Order updateFor = new Order(savedOrder.getOrderTableId(), MEAL.name(), savedOrder.getOrderedTime());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), updateFor))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
