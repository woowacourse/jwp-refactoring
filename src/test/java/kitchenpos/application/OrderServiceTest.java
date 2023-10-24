package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderChangeRequest;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Nested
    @DisplayName("주문 목록을 생성할 때 ")
    class Create {

        @Test
        @DisplayName("정상적으로 생성된다.")
        void create() {
            // given
            final MenuGroup menuGroup = new MenuGroup("치킨");
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            final Menu menu = new Menu(savedMenuGroup, new ArrayList<>(), "후라이드 치킨", new BigDecimal("15000.00"));
            menuRepository.save(menu);

            final OrderTable orderTable = new OrderTable(null, 1, false);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(),
                    List.of(orderLineItemRequest));

            // when
            final Order savedOrder = orderService.create(orderCreateRequest);

            // then
            assertAll(
                    () -> assertThat(savedOrder.getId()).isEqualTo(1L),
                    () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
            );
        }

        @Test
        @DisplayName("메뉴의 개수와 주문 항목의 개수가 다른 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderLineItemsAndMenuHasDifferentCount() {
            // given
            final MenuGroup menuGroup = new MenuGroup("치킨");
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            final Menu menu = new Menu(savedMenuGroup, new ArrayList<>(), "후라이드 치킨", new BigDecimal("15000.00"));
            menuRepository.save(menu);

            final OrderLineItemRequest orderLineItemRequestA = new OrderLineItemRequest(menu.getId(), 1);
            final OrderLineItemRequest orderLineItemRequestB = new OrderLineItemRequest(menu.getId(), 1);

            final OrderTable orderTable = new OrderTable(null, 1, false);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            final OrderCreateRequest request = new OrderCreateRequest(savedOrderTable.getId(),
                    List.of(orderLineItemRequestA, orderLineItemRequestB));

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("요청한 주문 항목의 개수와 저장된 메뉴의 개수가 댜릅니다.");
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않는다면 예외가 발생한다.")
        void throwsExceptionWhenOrderTableIdNonExist() {
            // given
            final MenuGroup menuGroup = new MenuGroup("치킨");
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            final Menu menu = new Menu(savedMenuGroup, new ArrayList<>(), "후라이드 치킨", new BigDecimal("15000.00"));
            menuRepository.save(menu);

            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, List.of(orderLineItemRequest));

            // when, then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 주문 테이블이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderTableIsEmpty() {
            // given
            final MenuGroup menuGroup = new MenuGroup("치킨");
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            final Menu menu = new Menu(savedMenuGroup, new ArrayList<>(), "후라이드 치킨", new BigDecimal("15000.00"));
            menuRepository.save(menu);

            final OrderTable orderTable = new OrderTable(null, 1, true);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(),
                    List.of(orderLineItemRequest));

            // when, then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블은 비어있을 수 없습니다.");
        }
    }

    @Test
    @DisplayName("주문 목록은 정상적으로 조회된다.")
    void list() {
        // given
        final MenuGroup menuGroup = new MenuGroup("치킨");
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        final Menu menu = new Menu(savedMenuGroup, new ArrayList<>(), "후라이드 치킨", new BigDecimal("15000.00"));
        menuRepository.save(menu);

        final OrderTable orderTable = new OrderTable(null, 1, false);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);

        final OrderCreateRequest orderCreateRequestA = new OrderCreateRequest(savedOrderTable.getId(),
                List.of(orderLineItemRequest));
        final OrderCreateRequest orderCreateRequestB = new OrderCreateRequest(savedOrderTable.getId(),
                List.of(orderLineItemRequest));

        orderService.create(orderCreateRequestA);
        orderService.create(orderCreateRequestB);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(2);
    }

    @Nested
    @DisplayName("주문의 상태를 변경할 때 ")
    class ChangeOrderStatus {

        @Test
        @DisplayName("정상적으로 변경된다.")
        void changeOrderStatus() {
            // given
            final MenuGroup menuGroup = new MenuGroup("치킨");
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            final Menu menu = new Menu(savedMenuGroup, new ArrayList<>(), "후라이드 치킨", new BigDecimal("15000.00"));
            menuRepository.save(menu);

            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final OrderTable orderTable = new OrderTable(null, 1, false);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(),
                    List.of(orderLineItemRequest));
            final Order savedOrder = orderService.create(orderCreateRequest);

            final OrderChangeRequest orderChangeRequest = new OrderChangeRequest(OrderStatus.COMPLETION);

            // when
            final Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), orderChangeRequest);

            // then
            assertAll(
                    () -> assertThat(changedOrder.getId()).isEqualTo(1L),
                    () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION)
            );
        }

        @Test
        @DisplayName("주문이 존재하지 않는 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderNonExist() {
            // given
            final MenuGroup menuGroup = new MenuGroup("치킨");
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            final Menu menu = new Menu(savedMenuGroup, new ArrayList<>(), "후라이드 치킨", new BigDecimal("15000.00"));
            menuRepository.save(menu);

            final OrderTable orderTable = new OrderTable(null, 1, false);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(),
                    List.of(orderLineItemRequest));
            orderService.create(orderCreateRequest);

            final OrderChangeRequest orderChangeRequest = new OrderChangeRequest(OrderStatus.COMPLETION);

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(2L, orderChangeRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 주문이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("기존 주문의 상태가 결제완료라면 예외가 발생한다.")
        void throwsExceptionWhenOrderStatusIsCompletion() {
            // given
            final MenuGroup menuGroup = new MenuGroup("치킨");
            final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
            final Menu menu = new Menu(savedMenuGroup, new ArrayList<>(), "후라이드 치킨", new BigDecimal("15000.00"));
            menuRepository.save(menu);

            final OrderTable orderTable = new OrderTable(null, 1, false);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(),
                    List.of(orderLineItemRequest));
            final Order savedOrder = orderService.create(orderCreateRequest);
            savedOrder.changeOrderStatus(OrderStatus.COMPLETION);

            final OrderChangeRequest orderChangeRequest = new OrderChangeRequest(OrderStatus.COOKING);

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), orderChangeRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("결제완료 상태인 경우 주문상태를 변경할 수 없습니다.");
        }
    }
}
