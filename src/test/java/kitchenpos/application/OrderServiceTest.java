package kitchenpos.application;

import static kitchenpos.common.fixtures.MenuFixtures.MENU1_NAME;
import static kitchenpos.common.fixtures.MenuFixtures.MENU1_PRICE;
import static kitchenpos.common.fixtures.MenuGroupFixtures.MENU_GROUP1_NAME;
import static kitchenpos.common.fixtures.OrderFixtures.ORDER1_CHANGE_STATUS_REQUEST;
import static kitchenpos.common.fixtures.OrderFixtures.ORDER1_CREATE_REQUEST;
import static kitchenpos.common.fixtures.OrderFixtures.ORDER1_ORDER_TABLE_ID;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static kitchenpos.common.fixtures.TableGroupFixtures.TABLE_GROUP1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.menu.Menu;
import kitchenpos.dto.order.OrderChangeStatusRequest;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderLineItemsResponse;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrdersResponse;
import kitchenpos.exception.OrderException;
import kitchenpos.exception.OrderTableException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
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
            menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup));
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
        @DisplayName("요청한 주문 항목 목록의 개수와 메뉴 아이디로 조회한 메뉴의 개수가 다르면 예외가 발생한다.")
        void throws_notSameOrderLineItemsSize() {
            // given
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ORDER1_ORDER_TABLE_ID,
                    List.of(new OrderLineItemRequest(1L, 1L),
                            new OrderLineItemRequest(2L, 1L)
                    )
            );
            final MenuGroup menuGroup = new MenuGroup(MENU_GROUP1_NAME);
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup));
            orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));

            // when & then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(OrderException.NotFoundOrderLineItemMenuExistException.class)
                    .hasMessage("[ERROR] 주문 항목 목록에 메뉴가 누락된 주문 항목이 존재합니다.");
        }

        @Test
        @DisplayName("주문 테이블 ID에 해당하는 주문 테이블이 존재하지 않으면 예외가 발생한다.")
        void throws_notFoundOrderTable() {
            // given
            final OrderCreateRequest orderCreateRequest = ORDER1_CREATE_REQUEST();
            final MenuGroup menuGroup = new MenuGroup(MENU_GROUP1_NAME);
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup));

            // when & then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                    .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("주문 테이블이 비어있는 상태면 예외가 발생한다")
        void throws_OrderTableIsEmpty() {
            // given
            final OrderCreateRequest orderCreateRequest = ORDER1_CREATE_REQUEST();
            final MenuGroup menuGroup = new MenuGroup(MENU_GROUP1_NAME);
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup));
            orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, true));

            // when & then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(OrderException.CannotOrderStateByOrderTableEmptyException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있는 상태일 때 주문할 수 없습니다.");
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
            menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup));

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TABLE_GROUP1());
            savedOrderTable.confirmTableGroup(savedTableGroup);
            Order order = new Order(savedOrderTable, OrderStatus.COOKING, LocalDateTime.now());

            OrderLineItem orderLineItem = new OrderLineItem(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup), 1L);
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
            menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup));

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TABLE_GROUP1());
            savedOrderTable.confirmTableGroup(savedTableGroup);
            Order order = new Order(savedOrderTable, OrderStatus.COOKING, LocalDateTime.now());

            OrderLineItem orderLineItem = new OrderLineItem(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup), 1L);
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
            menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup));

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TABLE_GROUP1());
            savedOrderTable.confirmTableGroup(savedTableGroup);
            Order order = new Order(savedOrderTable, OrderStatus.COOKING, LocalDateTime.now());

            OrderLineItem orderLineItem = new OrderLineItem(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup), 1L);
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
            menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup));

            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));
            TableGroup savedTableGroup = tableGroupRepository.save(TABLE_GROUP1());
            savedOrderTable.confirmTableGroup(savedTableGroup);
            Order order = new Order(savedOrderTable, OrderStatus.COMPLETION, LocalDateTime.now());

            OrderLineItem orderLineItem = new OrderLineItem(new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup), 1L);
            orderLineItem.confirmOrder(order);
            orderRepository.save(order);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(OrderException.CannotChangeOrderStatusByCurrentOrderStatusException.class)
                    .hasMessage("[ERROR] 기존 주문의 주문 상태가 계산 완료인 주문은 상태를 변경할 수 없습니다.");
        }
    }
}
