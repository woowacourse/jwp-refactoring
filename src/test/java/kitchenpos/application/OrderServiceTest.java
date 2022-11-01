package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderCreateResponse;
import kitchenpos.dto.OrderFindResponse;
import kitchenpos.dto.OrderStatusChangeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends ServiceTest {
    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        final MenuProducts menuProducts = new MenuProducts(new ArrayList<>());
        final Menu menu = new Menu(1L, "치킨메뉴", BigDecimal.valueOf(20_000L), 1L, menuProducts);
        final MenuProduct menuProduct = saveAndGetMenuProduct(1L, 1L, 1L);

        final OrderCreateResponse actual = orderService.create(
                generateCustomOrder(OrderStatus.COOKING.name(), menu, menu.getId(), menuProduct)
        );

        assertThat(actual)
                .extracting("id", "orderStatus")
                .containsExactly(1L, OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("존재하지 않는 메뉴로 주문을 생성하면 예외를 반환한다")
    void create_notExistMenuException() {
        final MenuProducts menuProducts = new MenuProducts(new ArrayList<>());
        final Menu menu = new Menu(1L, "치킨메뉴", BigDecimal.valueOf(20_000L), 1L, menuProducts);

        final MenuProduct invalidMenuProduct = saveAndGetMenuProduct(1L, 999999999L, 1L);

        assertThatThrownBy(
                () -> orderService.create(
                        generateCustomOrder(OrderStatus.COOKING.name(), menu, invalidMenuProduct.getMenuId(),
                                invalidMenuProduct)
                )
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 전체를 조회한다")
    void list() {
        saveAndGetOrder(1L, OrderStatus.COOKING.name());

        final List<OrderFindResponse> actual = orderService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual)
                        .extracting("orderTableId")
                        .containsExactly(1L)
        );
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        final Order order = saveAndGetOrder(1L, OrderStatus.COOKING.name());

        final OrderStatusChangeResponse actual =
                orderService.changeOrderStatus(order.getId(), OrderStatus.MEAL.name());

        assertThat(actual.getOrderStatus())
                .isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("`계산 완료`인 주문 상태를 변경하면 예외를 반환한다")
    void changeOrderStatus_completionException() {
        final Order order = saveAndGetOrder(1L, OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }


    private OrderCreateRequest generateCustomOrder(final String orderStatus,
                                                   final Menu menu, final Long orderMenuId,
                                                   final MenuProduct... menuProducts) {
        saveAndGetMenuGroup(1L);

        menu.addMenuProduct(menuProducts);
        menuDao.save(menu);

        final OrderTable orderTable = saveAndGetNotEmptyOrderTable(1L);
        final OrderLineItems orderLineItems = new OrderLineItems(new ArrayList<>());
        final Order order = new Order(1L, orderTable.getId(), orderStatus, LocalDateTime.now(), orderLineItems);
        order.addOrderLineItem(saveAndGetOrderLineItem(1L, orderMenuId, order.getId()));

        return new OrderCreateRequest(
                order.getOrderTableId(),
                orderLineItems.getOrderLineItems()
        );
    }
}
