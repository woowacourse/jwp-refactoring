package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixtures.한마리_메뉴;
import static kitchenpos.fixture.ProductFixtures.양념치킨_17000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.request.OrderCreateRequest;
import kitchenpos.order.request.OrderLineItemDto;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    OrderTableRepository orderTableRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(5, false));

        Menu savedMenu = menuRepository.save(createMenu("양념치킨", 17_000));
        OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getId(), 1);
        OrderCreateRequest request = getOrderCreateRequest(savedOrderTable.getId(), List.of(orderLineItem));
        // when
        Order savedOrder = orderService.create(request);

        // then
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
        );
    }

    @DisplayName("주문 항목이 비어 있는 주문을 생성하면 예외가 발생한다.")
    @Test
    void create_EmptyOrderLineItem_ExceptionThrown() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(5, false));
        OrderCreateRequest request = getOrderCreateRequest(savedOrderTable.getId(), Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 포함되어 있지 않습니다.");
    }

    @DisplayName("빈 테이블의 주문을 생성하면 예외가 발생한다.")
    @Test
    void create_EmptyTable_ExceptionThrown() {
        // given
        OrderTable savedEmptyTable = orderTableRepository.save(new OrderTable(5, true));

        Menu savedMenu = menuRepository.save(createMenu("양념치킨", 17_000));
        OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getId(), 1);
        OrderCreateRequest request = getOrderCreateRequest(savedEmptyTable.getId(), List.of(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어 있는 테이블은 주문을 생성할 수 없습니다.");
    }

    @DisplayName("저장된 주문 목록을 전체 조회한다.")
    @Test
    void list() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(5, false));

        Menu savedMenu = menuRepository.save(createMenu("양념치킨", 17_000));
        OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getId(), 1);
        OrderCreateRequest request = getOrderCreateRequest(savedOrderTable.getId(), List.of(orderLineItem));
        orderService.create(request);
        orderService.create(request);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(2);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(5, false));

        Menu savedMenu = menuRepository.save(createMenu("양념치킨", 17_000));
        OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getId(), 1);
        OrderCreateRequest request = getOrderCreateRequest(savedOrderTable.getId(), List.of(orderLineItem));
        Order savedOrder = orderService.create(request);

        // when
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.MEAL);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("계산 완료된 주문의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeOrderStatus_AlreadyCompleted_ExceptionThrown() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(5, false));

        Menu savedMenu = menuRepository.save(createMenu("양념치킨", 17_000));
        OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getId(), 1);
        OrderCreateRequest request = getOrderCreateRequest(savedOrderTable.getId(), List.of(orderLineItem));
        Order savedOrder = orderService.create(request);
        orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.MEAL);
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(changedOrder.getId(), OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다음 상태가 존재하지 않습니다.");
    }

    private Menu createMenu(String name, int price) {
        Product product = productRepository.save(양념치킨_17000원);
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1);
        MenuGroup menuGroup = menuGroupRepository.save(한마리_메뉴);
        return new Menu(null, name, new Price(BigDecimal.valueOf(price)), menuGroup.getId(), List.of(menuProduct));
    }

    private OrderCreateRequest getOrderCreateRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemDto> orderLineItemDtos = orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemDto(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());

        return new OrderCreateRequest(orderTableId, orderLineItemDtos);
    }
}
