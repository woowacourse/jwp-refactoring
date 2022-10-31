package kitchenpos.application;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.fixture.Fixture.삼인테이블_생성요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.request.MenuCreateRequest;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.request.OrderCreateRequest;
import kitchenpos.application.request.OrderLineItemRequest;
import kitchenpos.application.request.OrderTableUpdateRequest;
import kitchenpos.application.request.OrderUpdateRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductPrice;
import kitchenpos.fixture.Fixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class OrderLineItem이_없는_경우 extends ServiceTest {

            private final OrderCreateRequest request = new OrderCreateRequest(1L, Collections.emptyList());

            @Test
            void 예외가_발생한다() {
                final OrderTable orderTable = tableService.create(삼인테이블_생성요청);
                tableService.changeEmpty(orderTable.getId(), new OrderTableUpdateRequest(5, false));

                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 상품이 존재하지 않습니다.");
            }
        }

        @Nested
        class 입력받은_Menu가_존재하지_않는_경우 extends ServiceTest {

            private static final long NOT_EXIST_MENU_ID = -1L;

            private final OrderCreateRequest request = new OrderCreateRequest(1L,
                    List.of(new OrderLineItemRequest(NOT_EXIST_MENU_ID, 1L)));

            @Test
            void 예외가_발생한다() {
                final OrderTable orderTable = tableService.create(삼인테이블_생성요청);
                tableService.changeEmpty(orderTable.getId(), new OrderTableUpdateRequest(5, false));

                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("해당 메뉴가 존재하지 않습니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable이_존재하지_않는_경우 extends ServiceTest {

            private static final long NOT_EXIST_ORDER_TABLE_ID = -1L;

            private final OrderCreateRequest request = new OrderCreateRequest(NOT_EXIST_ORDER_TABLE_ID,
                    List.of(new OrderLineItemRequest(1L, 1L)));

            @Test
            void 예외가_발생한다() {
                final OrderTable orderTable = tableService.create(삼인테이블_생성요청);
                tableService.changeEmpty(orderTable.getId(), new OrderTableUpdateRequest(5, false));

                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 OrderTable 입니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable의_상태가_empty인_경우 extends ServiceTest {

            private final OrderCreateRequest request = new OrderCreateRequest(1L,
                    List.of(new OrderLineItemRequest(1L, 1L)));

            @Test
            void 예외가_발생한다() {
                tableService.create(삼인테이블_생성요청);

                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("해당 OrderTable이 empty 상태 입니다.");
            }
        }

        @Nested
        class 정상적인_입력을_받을_경우 extends ServiceTest {

            private Menu menu1;
            private Menu menu2;

            @BeforeEach
            void setUp() {
                menu1 = createMenu("후라이드치킨", BigDecimal.valueOf(16000));
                menu2 = createMenu("양념치킨", BigDecimal.valueOf(16000));
            }

            @Test
            void Order를_생성하고_반환한다() {
                final OrderTable orderTable = tableService.create(삼인테이블_생성요청);
                tableService.changeEmpty(orderTable.getId(), new OrderTableUpdateRequest(5, false));
                final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                        List.of(new OrderLineItemRequest(menu1.getId(), 1L),
                                new OrderLineItemRequest(menu2.getId(), 1L)));
                final Order actual = orderService.create(request);

                assertAll(() -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual.getOrderTableId()).isEqualTo(1L),
                        () -> assertThat(actual.getOrderedTime()).isBefore(LocalDateTime.now()),
                        () -> assertThat(actual.getOrderStatus()).isEqualTo(COOKING),
                        () -> assertThat(actual.getOrderLineItems().getValue()).extracting("menuId", "quantity")
                                .containsExactly(tuple(1L, 1L), tuple(2L, 1L)));
            }

            private Menu createMenu(final String name, final BigDecimal price) {
                final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("한마리메뉴"));
                Product product1 = productRepository.save(
                        new Product("후라이드치킨", new ProductPrice(BigDecimal.valueOf(16000))));
                Product product2 = productRepository.save(
                        new Product("양념치킨", new ProductPrice(BigDecimal.valueOf(17000))));

                List<MenuProductRequest> menuProducts = List.of(new MenuProductRequest(product1.getId(), 1L),
                        new MenuProductRequest(product2.getId(), 1L));
                return menuService.create(new MenuCreateRequest(name, price, menuGroup.getId(), menuProducts));
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출하는_경우 extends ServiceTest {

            private static final int EXPECT_SIZE = 1;

            @Test
            void Order의_목록을_반환한다() {
                final OrderTable orderTable = tableService.create(삼인테이블_생성요청);
                tableService.changeEmpty(orderTable.getId(), new OrderTableUpdateRequest(5, false));
                final MenuGroup menuGroup = menuGroupService.create(Fixture.한마리메뉴_생성요청);
                final Menu menu1 = menuService.create(
                        new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(0), menuGroup.getId(),
                                new ArrayList<>()));
                final Menu menu2 = menuService.create(
                        new MenuCreateRequest("양념치킨", BigDecimal.valueOf(0), menuGroup.getId(), new ArrayList<>()));
                final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                        List.of(new OrderLineItemRequest(menu1.getId(), 1L),
                                new OrderLineItemRequest(menu2.getId(), 1L)));

                orderService.create(request);

                final List<Order> actual = orderService.list();

                assertThat(actual).hasSize(EXPECT_SIZE);
            }
        }
    }

    @Nested
    class changeOrderStatus_메서드는 {

        @Nested
        class 입력받은_Order가_존재하지_않는_경우 extends ServiceTest {

            private static final long NOT_EXIST_ORDER_ID = -1;

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.changeOrderStatus(NOT_EXIST_ORDER_ID, null)).isInstanceOf(
                        IllegalArgumentException.class).hasMessage("존재하지 않는 Order 입니다.");
            }
        }

        @Nested
        class 입력받은_Order의_상태가_COMPLETION인_경우 extends ServiceTest {

            @Test
            void 예외가_발생한다() {
                final Order actual = orderRepository.save(
                        new Order(1L, COMPLETION, List.of(new OrderLineItem(1L, 1L))));

                assertThatThrownBy(() -> orderService.changeOrderStatus(actual.getId(),
                        new OrderUpdateRequest(COOKING.name()))).isInstanceOf(IllegalStateException.class)
                        .hasMessage("이미 완료된 Order의 상태는 변경할 수 없습니다.");
            }
        }

        @Nested
        class 정상적인_입력일_경우 extends ServiceTest {

            private final OrderUpdateRequest request = new OrderUpdateRequest(COMPLETION.name());

            @Test
            void Order의_상태를_변경한다() {
                final OrderTable orderTable = tableService.create(삼인테이블_생성요청);
                tableService.changeEmpty(orderTable.getId(), new OrderTableUpdateRequest(5, false));
                final MenuGroup menuGroup = menuGroupService.create(Fixture.한마리메뉴_생성요청);
                final Menu menu1 = menuService.create(
                        new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(0), menuGroup.getId(),
                                new ArrayList<>()));
                final Menu menu2 = menuService.create(
                        new MenuCreateRequest("양념치킨", BigDecimal.valueOf(0), menuGroup.getId(), new ArrayList<>()));
                final Order order = orderService.create(new OrderCreateRequest(orderTable.getId(),
                        List.of(new OrderLineItemRequest(menu1.getId(), 1L),
                                new OrderLineItemRequest(menu2.getId(), 1L))));

                final Order actual = orderService.changeOrderStatus(order.getId(), request);

                assertThat(actual.getOrderStatus()).isEqualTo(COMPLETION);
            }
        }
    }
}
