package kitchenpos.order.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.order.dto.OrderChangeStatusRequest;
import kitchenpos.application.order.dto.OrderCreateRequest;
import kitchenpos.application.order.dto.OrderLineItemsResponse;
import kitchenpos.application.order.dto.OrderResponse;
import kitchenpos.application.order.dto.OrdersResponse;
import kitchenpos.common.ServiceTest;
import kitchenpos.common.fixtures.MenuFixtures;
import kitchenpos.common.fixtures.MenuGroupFixtures;
import kitchenpos.common.fixtures.MenuProductFixtures;
import kitchenpos.common.fixtures.OrderFixtures;
import kitchenpos.common.fixtures.OrderTableFixtures;
import kitchenpos.common.fixtures.ProductFixtures;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.exception.OrderException;
import kitchenpos.domain.orertable.OrderTable;
import kitchenpos.domain.orertable.OrderTableRepository;
import kitchenpos.domain.orertable.TableGroup;
import kitchenpos.domain.orertable.TableGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("주문 생성 시")
    class CreateOrder {

        @Test
        @DisplayName("생성에 성공한다.")
        void success() {
            // given
            final OrderCreateRequest orderCreateRequest = OrderFixtures.ORDER1_CREATE_REQUEST();
            final MenuGroup menuGroup = new MenuGroup(MenuGroupFixtures.MENU_GROUP1_NAME);
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            final Product savedProduct1 = productRepository.save(ProductFixtures.PRODUCT1());
            final Product savedProduct2 = productRepository.save(ProductFixtures.PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1.getId(), MenuProductFixtures.MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2.getId(), MenuProductFixtures.MENU_PRODUCT2_QUANTITY);
            menuRepository.save(new Menu(MenuFixtures.MENU1_NAME, MenuFixtures.MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));
            orderTableRepository.save(new OrderTable(OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS, false));

            // when
            final OrderResponse response = orderService.create(orderCreateRequest);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                softly.assertThat(response.getOrderTableId()).isNotNull();
                softly.assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
                softly.assertThat(response.getOrderedTime()).isNotNull();
                softly.assertThat(response.getOrderLineItems()).isNotNull();
            });
        }
    }

    @Nested
    @DisplayName("주문 조회 시")
    class FindAll {

        @Test
        @DisplayName("조회에 성공한다.")
        void success() {
            // given
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(MenuGroupFixtures.MENU_GROUP1_NAME));
            final Product savedProduct1 = productRepository.save(ProductFixtures.PRODUCT1());
            final Product savedProduct2 = productRepository.save(ProductFixtures.PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1.getId(), MenuProductFixtures.MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2.getId(), MenuProductFixtures.MENU_PRODUCT2_QUANTITY);
            final Menu savedMenu = menuRepository.save(
                    new Menu(MenuFixtures.MENU1_NAME, MenuFixtures.MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));
            final int orderLineItemSize = 1;

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(
                    OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
            savedOrderTable.updateTableGroup(savedTableGroup);
            OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getName(), savedMenu.getPrice(), 1L);
            Order order = Order.from(savedOrderTable.getId(), orderLineItemSize, orderLineItemSize, List.of(orderLineItem));

            orderRepository.save(order);

            // when
            OrdersResponse responses = orderService.list();
            OrderLineItemsResponse orderLineItemsResponse = responses.getOrders().get(0).getOrderLineItems();

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(orderLineItemsResponse.getOrderLineItems().get(0).getName())
                        .isEqualTo(orderLineItem.getName());
                softly.assertThat(orderLineItemsResponse.getOrderLineItems().get(0).getPrice())
                        .isEqualTo(orderLineItem.getPrice());
                softly.assertThat(orderLineItemsResponse.getOrderLineItems().get(0).getQuantity())
                        .isEqualTo(orderLineItem.getQuantity());
            });
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 시")
    class ChangeOrderStatus {

        @Test
        @DisplayName("변경에 성공한다.")
        void success() {
            // given
            final OrderChangeStatusRequest request = OrderFixtures.ORDER1_CHANGE_STATUS_REQUEST();

            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(MenuGroupFixtures.MENU_GROUP1_NAME));
            final Product savedProduct1 = productRepository.save(ProductFixtures.PRODUCT1());
            final Product savedProduct2 = productRepository.save(ProductFixtures.PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1.getId(), MenuProductFixtures.MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2.getId(), MenuProductFixtures.MENU_PRODUCT2_QUANTITY);
            final Menu savedMenu = menuRepository.save(
                    new Menu(MenuFixtures.MENU1_NAME, MenuFixtures.MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));
            final int orderLineItemSize = 1;

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(
                    OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
            savedOrderTable.updateTableGroup(savedTableGroup);
            OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getName(), savedMenu.getPrice(), 1L);
            Order order = Order.from(savedOrderTable.getId(), orderLineItemSize, orderLineItemSize, List.of(orderLineItem));

            orderRepository.save(order);

            // when
            final OrderResponse response = orderService.changeOrderStatus(order.getId(), request);

            // then
            assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.valueOf(request.getOrderStatus()));
        }

        @Test
        @DisplayName("주문 ID에 해당하는 주문이 없으면 예외가 발생한다.")
        void throws_NotFoundOrder() {
            // given
            final Long notExistOrderId = -1L;
            final OrderChangeStatusRequest request = OrderFixtures.ORDER1_CHANGE_STATUS_REQUEST();

            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(MenuGroupFixtures.MENU_GROUP1_NAME));
            final Product savedProduct1 = productRepository.save(ProductFixtures.PRODUCT1());
            final Product savedProduct2 = productRepository.save(ProductFixtures.PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1.getId(), MenuProductFixtures.MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2.getId(), MenuProductFixtures.MENU_PRODUCT2_QUANTITY);
            final Menu savedMenu = menuRepository.save(
                    new Menu(MenuFixtures.MENU1_NAME, MenuFixtures.MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));
            final int orderLineItemSize = 1;

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(
                    OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
            savedOrderTable.updateTableGroup(savedTableGroup);
            OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getName(), savedMenu.getPrice(), 1L);
            Order order = Order.from(savedOrderTable.getId(), orderLineItemSize, orderLineItemSize, List.of(orderLineItem));

            orderRepository.save(order);

            // when & then
            Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, request))
                    .isInstanceOf(OrderException.NotFoundOrderException.class)
                    .hasMessage("[ERROR] 주문을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("기존 주문 상태가 계산 완료이면 예외가 발생한다.")
        void throws_OrderStatusIsCompletion() {
            // given
            final OrderChangeStatusRequest request = OrderFixtures.ORDER1_CHANGE_STATUS_REQUEST();

            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(MenuGroupFixtures.MENU_GROUP1_NAME));
            final Product savedProduct1 = productRepository.save(ProductFixtures.PRODUCT1());
            final Product savedProduct2 = productRepository.save(ProductFixtures.PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1.getId(), MenuProductFixtures.MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2.getId(), MenuProductFixtures.MENU_PRODUCT2_QUANTITY);
            final Menu savedMenu = menuRepository.save(
                    new Menu(MenuFixtures.MENU1_NAME, MenuFixtures.MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));
            final int orderLineItemSize = 1;

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(
                    OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
            savedOrderTable.updateTableGroup(savedTableGroup);
            OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getName(), savedMenu.getPrice(), 1L);
            Order order = Order.from(savedOrderTable.getId(), orderLineItemSize, orderLineItemSize, List.of(orderLineItem));

            order.changeStatus(OrderStatus.COMPLETION);
            orderRepository.save(order);

            // when & then
            Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(OrderException.CannotChangeOrderStatusByCurrentOrderStatusException.class)
                    .hasMessage("[ERROR] 기존 주문의 주문 상태가 계산 완료인 주문은 상태를 변경할 수 없습니다.");
        }
    }
}
