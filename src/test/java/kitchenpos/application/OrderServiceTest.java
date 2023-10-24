package kitchenpos.application;

import static kitchenpos.common.fixtures.MenuFixtures.MENU1_NAME;
import static kitchenpos.common.fixtures.MenuFixtures.MENU1_PRICE;
import static kitchenpos.common.fixtures.MenuGroupFixtures.MENU_GROUP1_NAME;
import static kitchenpos.common.fixtures.MenuProductFixtures.MENU_PRODUCT1_QUANTITY;
import static kitchenpos.common.fixtures.MenuProductFixtures.MENU_PRODUCT2_QUANTITY;
import static kitchenpos.common.fixtures.OrderFixtures.ORDER1_CHANGE_STATUS_REQUEST;
import static kitchenpos.common.fixtures.OrderFixtures.ORDER1_CREATE_REQUEST;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT2;
import static kitchenpos.common.fixtures.TableGroupFixtures.TABLE_GROUP1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderChangeStatusRequest;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemsResponse;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrdersResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.OrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.OrderTableException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
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
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("주문 생성 시")
    class CreateOrder {

        @Test
        @DisplayName("생성에 성공한다.")
        void success() {
            // given
            final OrderCreateRequest orderCreateRequest = ORDER1_CREATE_REQUEST();
            final MenuGroup menuGroup = new MenuGroup(MENU_GROUP1_NAME);
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            final Product savedProduct1 = productRepository.save(PRODUCT1());
            final Product savedProduct2 = productRepository.save(PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1, MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2, MENU_PRODUCT2_QUANTITY);
            menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));
            orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));

            // when
            final OrderResponse response = orderService.create(orderCreateRequest);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                softly.assertThat(response.getOrderTable().getId()).isNotNull();
                softly.assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
                softly.assertThat(response.getOrderedTime()).isNotNull();
                softly.assertThat(response.getOrderLineItems()).isNotNull();
            });
        }

        @Test
        @DisplayName("주문 테이블 ID에 해당하는 주문 테이블이 존재하지 않으면 예외가 발생한다.")
        void throws_notFoundOrderTable() {
            // given
            final OrderCreateRequest orderCreateRequest = ORDER1_CREATE_REQUEST();
            final MenuGroup menuGroup = new MenuGroup(MENU_GROUP1_NAME);
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            final Product savedProduct1 = productRepository.save(PRODUCT1());
            final Product savedProduct2 = productRepository.save(PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1, MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2, MENU_PRODUCT2_QUANTITY);
            menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));

            // when & then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                    .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("주문 조회 시")
    class FindAll {

        @Test
        @DisplayName("조회에 성공한다.")
        void success() {
            // given
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP1_NAME));
            final Product savedProduct1 = productRepository.save(PRODUCT1());
            final Product savedProduct2 = productRepository.save(PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1, MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2, MENU_PRODUCT2_QUANTITY);
            final Menu savedMenu = menuRepository.save(
                    new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TABLE_GROUP1());
            savedOrderTable.confirmTableGroup(savedTableGroup);
            Order order = Order.from(savedOrderTable);

            OrderLineItem orderLineItem = new OrderLineItem(savedMenu, 1L);
            orderLineItem.confirmOrder(order);
            orderRepository.save(order);

            // when
            OrdersResponse responses = orderService.list();
            OrderLineItemsResponse orderLineItemsResponse = responses.getOrders().get(0).getOrderLineItems();

            // then
            assertSoftly(softly -> {
                softly.assertThat(orderLineItemsResponse.getOrderLineItems().get(0).getMenuId())
                        .isEqualTo(orderLineItem.getMenu().getId());
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
            final OrderChangeStatusRequest request = ORDER1_CHANGE_STATUS_REQUEST();

            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP1_NAME));
            final Product savedProduct1 = productRepository.save(PRODUCT1());
            final Product savedProduct2 = productRepository.save(PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1, MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2, MENU_PRODUCT2_QUANTITY);
            final Menu savedMenu = menuRepository.save(
                    new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TABLE_GROUP1());
            savedOrderTable.confirmTableGroup(savedTableGroup);
            Order order = Order.from(savedOrderTable);

            OrderLineItem orderLineItem = new OrderLineItem(savedMenu, 1L);
            orderLineItem.confirmOrder(order);
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
            final OrderChangeStatusRequest request = ORDER1_CHANGE_STATUS_REQUEST();

            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP1_NAME));
            final Product savedProduct1 = productRepository.save(PRODUCT1());
            final Product savedProduct2 = productRepository.save(PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1, MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2, MENU_PRODUCT2_QUANTITY);
            final Menu savedMenu = menuRepository.save(
                    new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TABLE_GROUP1());
            savedOrderTable.confirmTableGroup(savedTableGroup);
            Order order = Order.from(savedOrderTable);

            OrderLineItem orderLineItem = new OrderLineItem(savedMenu, 1L);
            orderLineItem.confirmOrder(order);
            orderRepository.save(order);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, request))
                    .isInstanceOf(OrderException.NotFoundOrderException.class)
                    .hasMessage("[ERROR] 주문을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("기존 주문 상태가 계산 완료이면 예외가 발생한다.")
        void throws_OrderStatusIsCompletion() {
            // given
            final OrderChangeStatusRequest request = ORDER1_CHANGE_STATUS_REQUEST();

            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP1_NAME));
            final Product savedProduct1 = productRepository.save(PRODUCT1());
            final Product savedProduct2 = productRepository.save(PRODUCT2());
            final MenuProduct menuProduct1 = new MenuProduct(savedProduct1, MENU_PRODUCT1_QUANTITY);
            final MenuProduct menuProduct2 = new MenuProduct(savedProduct2, MENU_PRODUCT2_QUANTITY);
            final Menu savedMenu = menuRepository.save(
                    new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TABLE_GROUP1());
            savedOrderTable.confirmTableGroup(savedTableGroup);
            Order order = Order.from(savedOrderTable);
            order.changeStatus(OrderStatus.COMPLETION);

            OrderLineItem orderLineItem = new OrderLineItem(savedMenu, 1L);
            orderLineItem.confirmOrder(order);
            orderRepository.save(order);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(OrderException.CannotChangeOrderStatusByCurrentOrderStatusException.class)
                    .hasMessage("[ERROR] 기존 주문의 주문 상태가 계산 완료인 주문은 상태를 변경할 수 없습니다.");
        }
    }
}
