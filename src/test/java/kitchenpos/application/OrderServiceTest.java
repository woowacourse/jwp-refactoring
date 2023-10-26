package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    private List<Menu> menus;
    private OrderTable orderTable;

    @BeforeEach
    void beforeEach() {
        MenuGroup menuGroup = new MenuGroup("menu group");
        menuGroup = testFixtureBuilder.buildMenuGroup(menuGroup);

        menus = new ArrayList<>();
        Menu menu1 = new Menu("name", new BigDecimal(0), menuGroup.getId(), Collections.emptyList());
        menus.add(testFixtureBuilder.buildMenu(menu1));
        Menu menu2 = new Menu("name", new BigDecimal(0), menuGroup.getId(), Collections.emptyList());
        menus.add(testFixtureBuilder.buildMenu(menu2));

        orderTable = new OrderTable(null, 3, false);
        orderTable = testFixtureBuilder.buildOrderTable(orderTable);
    }

    @DisplayName("주문 생성 테스트")
    @Nested
    class OrderCreateTest {

        @DisplayName("주문을 생성한다.")
        @Test
        void orderCreate() {
            //given
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                    menus.stream()
                            .map(menu -> new OrderCreateRequest.MenuSnapShot(
                                    menu.getId(),
                                    menu.getName(),
                                    menu.getPrice(),
                                    menu.getMenuProducts()
                                            .stream()
                                            .map(menuProduct -> new OrderCreateRequest.MenuSnapShot.ProductSnapShot(menuProduct.getSeq(), menuProduct.getProduct().getId(), menuProduct.getProduct().getName(), menuProduct.getProduct().getPrice(), menuProduct.getQuantity()))
                                            .collect(Collectors.toList()),
                                    2L))
                            .collect(Collectors.toList()));

            //when
            final Long id = orderService.create(request);

            //then
            assertSoftly(softly -> {
                softly.assertThat(id).isNotNull();
            });
        }

        @DisplayName("주문 도중 메뉴의 정보가 바뀌면 실패한다.")
        @Test
        void orderCreateFailWhenChangeMenuInformation() throws IllegalAccessException, NoSuchFieldException {
            //given
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                    menus.stream()
                            .map(menu -> new OrderCreateRequest.MenuSnapShot(
                                    menu.getId(),
                                    menu.getName(),
                                    menu.getPrice(),
                                    menu.getMenuProducts()
                                            .stream()
                                            .map(menuProduct -> new OrderCreateRequest.MenuSnapShot.ProductSnapShot(menuProduct.getSeq(), menuProduct.getProduct().getId(), menuProduct.getProduct().getName(), menuProduct.getProduct().getPrice(), menuProduct.getQuantity()))
                                            .collect(Collectors.toList()),
                                    2L))
                            .collect(Collectors.toList()));

            final Menu menu = menus.get(0);
            Field nameField = menu.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(menu, "change menu name");
            testFixtureBuilder.buildMenu(menu);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }


        @DisplayName("주문 항목이 비어있으면 실패한다.")
        @Test
        void orderCreateFailWhenOrderLineItemsIsEmpty() {
            //given
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문할 메뉴가 존재하지 않으면 실패한다.")
        @Test
        void orderCreateFailWhenNotExistMenu() {
            //given
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                    List.of(new OrderCreateRequest.MenuSnapShot(
                            -1L,
                            "not exists menu",
                            new BigDecimal(0),
                            Collections.emptyList(),
                            1L
                    )));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void orderCreateFailWhenNotExistOrderTable() {
            //given
            final Menu menu = menus.get(0);
            final OrderCreateRequest request = new OrderCreateRequest(-1L,
                    List.of(new OrderCreateRequest.MenuSnapShot(
                            menu.getId(),
                            menu.getName(),
                            menu.getPrice(),
                            menu.getMenuProducts().stream()
                                    .map(menuProduct -> new OrderCreateRequest.MenuSnapShot.ProductSnapShot(menuProduct.getSeq(), menuProduct.getProduct().getId(), menuProduct.getProduct().getName(), menuProduct.getProduct().getPrice(), menuProduct.getQuantity()))
                                    .collect(Collectors.toList()),
                            1L
                    )));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있으면 실패한다.")
        @Test
        void orderCreateFailWhenOrderTableIsEmpty() {
            //given
            orderTable = new OrderTable(null, 3, true);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final Menu menu = menus.get(0);
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                    List.of(new OrderCreateRequest.MenuSnapShot(
                            menu.getId(),
                            menu.getName(),
                            menu.getPrice(),
                            menu.getMenuProducts().stream()
                                    .map(menuProduct -> new OrderCreateRequest.MenuSnapShot.ProductSnapShot(menuProduct.getSeq(), menuProduct.getProduct().getId(), menuProduct.getProduct().getName(), menuProduct.getProduct().getPrice(), menuProduct.getQuantity()))
                                    .collect(Collectors.toList()),
                            1L
                    )));
            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 조회 테스트")
    @Nested
    class OrderFindTest {

        @DisplayName("주문을 전체 조회한다.")
        @Test
        void orderFindAll() {
            //given
            Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());
            order = testFixtureBuilder.buildOrder(order);

            //when
            final List<OrderResponse> actual = orderService.list();

            //then
            final Long orderId = order.getId();
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(orderId);
            });
        }

        @DisplayName("주문을 생성후 메뉴 정보를 바꿔도 주문 항목의 메뉴 이름과 가격은 유지된다.")
        @Test
        void orderCreate() throws NoSuchFieldException, IllegalAccessException {
            //given
            final Menu menu = menus.get(0);
            final String beforeMenuName = menu.getName();
            final BigDecimal beforeMenuPrice = menu.getPrice();

            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                    List.of(new OrderCreateRequest.MenuSnapShot(
                            menu.getId(),
                            menu.getName(),
                            menu.getPrice(),
                            menu.getMenuProducts()
                                    .stream()
                                    .map(menuProduct -> new OrderCreateRequest.MenuSnapShot.ProductSnapShot(menuProduct.getSeq(), menuProduct.getProduct().getId(), menuProduct.getProduct().getName(), menuProduct.getProduct().getPrice(), menuProduct.getQuantity()))
                                    .collect(Collectors.toList()),
                            2L)));
            orderService.create(request);

            //when
            final List<OrderResponse> actual = orderService.list();

            final Field nameField = menu.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(menu, "change menu name");

            final Field priceField = menu.getClass().getDeclaredField("price");
            priceField.setAccessible(true);
            final Price afterPrice = new Price(menu.getPrice().add(new BigDecimal(10000)));
            priceField.set(menu, afterPrice);
            final Menu afterMenu = testFixtureBuilder.buildMenu(menu);

            //then
            final OrderResponse orderResponse = actual.get(0);
            final Long orderLineItemId = orderResponse.getOrderLineItemIds().get(0);
            final OrderLineItem orderLineItem = testFixtureBuilder.getEntitySupporter().getOrderLineItemRepository().findById(orderLineItemId).orElseThrow(IllegalAccessError::new);
            assertSoftly(softly -> {
                softly.assertThat(orderLineItem.getMenuName()).isNotEqualTo(afterMenu.getName());
                softly.assertThat(orderLineItem.getMenuName()).isEqualTo(beforeMenuName);
                softly.assertThat(orderLineItem.getMenuPrice().getValue()).isNotEqualByComparingTo(afterPrice.getValue());
                softly.assertThat(orderLineItem.getMenuPrice().getValue()).isEqualByComparingTo(beforeMenuPrice);
            });
        }
    }

    @DisplayName("주문 상태 변경 테스트")
    @Nested
    class OrderStatusChangeTest {

        @DisplayName("주문 상태를 변경한다.")
        @Test
        void orderStatusChange() {
            //given
            Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());
            order = testFixtureBuilder.buildOrder(order);

            //when
            final Long actual = orderService.changeOrderStatus(order.getId(), OrderStatus.MEAL.name());

            //then
            final Long orderId = order.getId();
            assertSoftly(softly -> {
                softly.assertThat(actual).isEqualTo(orderId);
            });
        }

        @DisplayName("존재하지 않는 주문은 변경할 수 없다.")
        @Test
        void orderStatusChangeFailWhenNotExistOrder() {
            //given
            final String changeStatus = OrderStatus.MEAL.name();

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, changeStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 완료면 변경할 수 없다.")
        @Test
        void orderStatusChangeFailWhenStatusIsCompletion() {
            //given
            Order completionOrder = new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.emptyList());
            completionOrder = testFixtureBuilder.buildOrder(completionOrder);

            final String changeStatus = OrderStatus.MEAL.name();

            // when & then
            final Long completionOrderId = completionOrder.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrderId, changeStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
