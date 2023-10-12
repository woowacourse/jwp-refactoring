package kitchenpos.application.order;

import kitchenpos.application.OrderService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderServiceTest extends ApplicationTestConfig {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("새로운 주문 등록")
    @Nested
    class CreateNestedTest {

        @DisplayName("[SUCCESS] 새로운 주문을 등록한다.")
        @Test
        void success_create() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트용 메뉴 그룹명"));
            final Menu savedMenu = menuDao.save(new Menu(
                    "테스트용 메뉴명",
                    BigDecimal.ZERO,
                    savedMenuGroup.getId(),
                    Collections.emptyList()
            ));
            final OrderTable savedOrderTable = orderTableDao.save(new OrderTable(null, 5, false));

            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, savedMenu.getId(), 10));

            final Order expected = new Order(
                    savedOrderTable.getId(),
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    orderLineItems
            );

            // when
            final Order actual = orderService.create(expected);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId());
                softly.assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
                softly.assertThat(actual.getOrderedTime()).isEqualTo(expected.getOrderedTime());
                softly.assertThat(actual.getOrderLineItems())
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(expected.getOrderLineItems());
            });
        }

        @DisplayName("[EXCEPTION] 주문 항목 목록이 비어있는 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderLineItemsIsEmpty() {
            // given
            final OrderTable savedOrderTable = orderTableDao.save(new OrderTable(null, 5, false));

            // when
            final List<OrderLineItem> wrongOrderLineItems = Collections.emptyList();
            final Order expected = new Order(
                    savedOrderTable.getId(),
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    wrongOrderLineItems
            );

            // then
            assertThatThrownBy(() -> orderService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 항목 목록 수량이 실제 메뉴의 수량과 다를 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderLineItemsSize_IsNotEqualTo_menuCountsSize() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트용 메뉴 그룹명"));
            final Menu savedMenu = menuDao.save(new Menu(
                    "테스트용 메뉴명",
                    BigDecimal.ZERO,
                    savedMenuGroup.getId(),
                    Collections.emptyList()
            ));
            final OrderTable savedOrderTable = orderTableDao.save(new OrderTable(null, 5, false));

            // when
            final Long wrongMenuId = null;
            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, wrongMenuId, 10));

            final Order expected = new Order(
                    savedOrderTable.getId(),
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    orderLineItems
            );

            // then
            assertThatThrownBy(() -> orderService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 테이블이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderTableIsNotExists() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트용 메뉴 그룹명"));
            final Menu savedMenu = menuDao.save(new Menu(
                    "테스트용 메뉴명",
                    BigDecimal.ZERO,
                    savedMenuGroup.getId(),
                    Collections.emptyList()
            ));

            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, savedMenu.getId(), 10));

            // when
            final Long wrongOrderTableId = null;
            final Order expected = new Order(
                    wrongOrderTableId,
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    orderLineItems
            );

            // then
            assertThatThrownBy(() -> orderService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class ChangeOrderStatus {

        @DisplayName("[SUCCESS] 주문 상태를 변경한다.")
        @Test
        void success_changeOrderStatus() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트용 메뉴 그룹명"));
            final Menu savedMenu = menuDao.save(new Menu(
                    "테스트용 메뉴명",
                    BigDecimal.ZERO,
                    savedMenuGroup.getId(),
                    Collections.emptyList()
            ));
            final OrderTable savedOrderTable = orderTableDao.save(new OrderTable(null, 5, false));

            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, savedMenu.getId(), 10));

            final Order order = new Order(
                    savedOrderTable.getId(),
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    orderLineItems
            );
            final Order savedOrder = orderService.create(order);

            // when
            final Order expected = new Order(
                    savedOrder.getOrderTableId(),
                    OrderStatus.COMPLETION.name(),
                    savedOrder.getOrderedTime(),
                    savedOrder.getOrderLineItems()
            );

            final Order actual = orderService.changeOrderStatus(savedOrder.getId(), expected);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId());
                softly.assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
                softly.assertThat(actual.getOrderedTime()).isEqualTo(expected.getOrderedTime());
                softly.assertThat(actual.getOrderLineItems())
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(expected.getOrderLineItems());
            });
        }

        @DisplayName("[EXCEPTION] 주문이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void throwException_when_order_isNotExists() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트용 메뉴 그룹명"));
            final Menu savedMenu = menuDao.save(new Menu(
                    "테스트용 메뉴명",
                    BigDecimal.ZERO,
                    savedMenuGroup.getId(),
                    Collections.emptyList()
            ));
            final OrderTable savedOrderTable = orderTableDao.save(new OrderTable(null, 5, false));

            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, savedMenu.getId(), 10));

            final Order order = new Order(
                    savedOrderTable.getId(),
                    OrderStatus.COOKING.name(),
                    LocalDateTime.now(),
                    orderLineItems
            );
            final Order savedOrder = orderService.create(order);

            final Order updateOrder = new Order(
                    savedOrder.getOrderTableId(),
                    OrderStatus.COMPLETION.name(),
                    savedOrder.getOrderedTime(),
                    savedOrder.getOrderLineItems()
            );

            // then
            final Long wrongOrderId = null;
            assertThatThrownBy(() -> orderService.changeOrderStatus(wrongOrderId, updateOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문이 완료 상태에서 완료 상태로 변경할 경우 예외가 발생한다.")
        @Test
        void throwException_when_orderStatus_is_COMPLETION() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트용 메뉴 그룹명"));
            final Menu savedMenu = menuDao.save(new Menu(
                    "테스트용 메뉴명",
                    BigDecimal.ZERO,
                    savedMenuGroup.getId(),
                    Collections.emptyList()
            ));
            final OrderTable savedOrderTable = orderTableDao.save(new OrderTable(null, 5, false));

            final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, savedMenu.getId(), 10));

            // when
            final Order order = new Order(
                    savedOrderTable.getId(),
                    OrderStatus.MEAL.name(),
                    LocalDateTime.now(),
                    orderLineItems
            );
            final Order savedOrder = orderService.create(order);

            final OrderStatus completion = OrderStatus.COMPLETION;
            final Order updateOrder = new Order(
                    savedOrder.getOrderTableId(),
                    completion.name(),
                    savedOrder.getOrderedTime(),
                    savedOrder.getOrderLineItems()
            );
            orderService.changeOrderStatus(savedOrder.getId(), updateOrder);

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), updateOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }
}
