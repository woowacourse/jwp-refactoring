package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.치킨;
import static kitchenpos.application.fixture.ProductFixture.양념_치킨;
import static kitchenpos.application.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTestBase {

    @Autowired
    private OrderService orderService;

    private Menu originalAndSeasonedChicken;
    private Menu originalChicken;


    @DisplayName("메뉴 및 메뉴 그룹 생성")
    @BeforeEach
    void setUp() {
        Product productChicken1 = productRepository.save(후라이드_치킨());
        Product productChicken2 = productRepository.save(양념_치킨());
        MenuGroup chickenMenuGroup = menuGroupRepository.save(치킨());

        MenuProduct menuProductChicken1 = createMenuProduct(productChicken1.getId(), 1, productChicken1.getPrice());
        MenuProduct menuProductChicken2 = createMenuProduct(productChicken2.getId(), 1, productChicken2.getPrice());

        Menu twoChickens = createMenu("후라이드+양념",
                BigDecimal.valueOf(35000),
                chickenMenuGroup.getId(),
                Arrays.asList(menuProductChicken1, menuProductChicken2));

        Menu oneChicken = createMenu("후라이드",
                BigDecimal.valueOf(18000),
                chickenMenuGroup.getId(),
                Collections.singletonList(menuProductChicken1));

        originalAndSeasonedChicken = menuRepository.save(twoChickens);
        originalChicken = menuRepository.save(oneChicken);
    }


    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블_생성());

        Order order1 = 주문_생성_및_저장(orderTable1, originalAndSeasonedChicken, 1);
        Order order2 = 주문_생성_및_저장(orderTable1, originalAndSeasonedChicken, 1);

        // when
        List<OrderResponse> orders = orderService.list();

        //then
        assertThat(orders).hasSize(2);
    }

    @DisplayName("주문 시 주문 항목이 비어있으면 예외를 발생한다.")
    @Test
    void orderWithEmptyOrderLineItem() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블_생성());

        OrderCreateRequest orderRequest = createOrderCreateRequest(orderTable1.getId(),
                Collections.emptyList());

        // when & then
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 비어있습니다.");
    }

    @DisplayName("주문 시 등록되어있는 메뉴의 수와 다르면 예외를 발생한다")
    @Test
    void orderWithDifferentMenuSize() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블_생성());

        OrderLineItemRequest orderLineItem1 = createOrderLineItemRequest(originalAndSeasonedChicken.getId(), 1);
        OrderLineItemRequest orderLineItem2 = createOrderLineItemRequest(originalAndSeasonedChicken.getId(), 1);
        OrderCreateRequest orderRequest = createOrderCreateRequest(orderTable1.getId(),
                Arrays.asList(orderLineItem1, orderLineItem2));

        // when & then
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 수가 부족합니다.");
    }

    @DisplayName("주문 시 order table id로 ordertable을 찾아서 없으면 예외를 발생한다.")
    @Test
    void orderWithNotExistedOrderTableId() {
        // given
        OrderLineItemRequest orderLineItem1 = createOrderLineItemRequest(originalAndSeasonedChicken.getId(), 1);
        OrderCreateRequest orderRequest = createOrderCreateRequest(0L,
                Collections.singletonList(orderLineItem1));

        // when & then
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 존재하지 않습니다.");
    }

    @DisplayName("주문 시 order table의 emtry가 true이면 예외를 발생한다.")
    @Test
    void orderWithEmptyOrderTableId() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(빈_주문_테이블_생성());

        OrderLineItemRequest orderLineItem1 = createOrderLineItemRequest(originalAndSeasonedChicken.getId(), 1);
        OrderCreateRequest orderRequest = createOrderCreateRequest(orderTable1.getId(),
                Collections.singletonList(orderLineItem1));

        // when & then
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 주문 테이블입니다.");
    }

    @DisplayName("주문 시 정상적으로 주문된다.")
    @Test
    void orderSuccessful() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블_생성());

        OrderLineItemRequest orderLineItem1 = createOrderLineItemRequest(originalAndSeasonedChicken.getId(), 1);
        OrderLineItemRequest orderLineItem2 = createOrderLineItemRequest(originalChicken.getId(), 2);
        OrderCreateRequest orderRequest = createOrderCreateRequest(orderTable1.getId(),
                Arrays.asList(orderLineItem1, orderLineItem2));

        // when
        OrderResponse savedOrder = orderService.create(orderRequest);

        // then
        assertAll(
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderLineItems()).hasSize(2)
        );
    }

    @DisplayName("주문 상태 변경 시 order id 에 대하여 order가 존재하지 않으면 예외를 발생한다.")
    @Test
    void updateStatusWithNotExistedOrder() {
        // given
        OrderStatusRequest orderStatusRequest = createOrderStatusRequest(OrderStatus.MEAL);

        // when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(1L, orderStatusRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문번호입니다.");
    }

    @DisplayName("주문 상태 변경 시 주문 상태가 이미 완료이면 예외를 발생한다.")
    @Test
    void updateStatusWithCompletedOrder() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블_생성());

        Order order1 = 주문_생성_및_저장(orderTable1, originalAndSeasonedChicken, 1);

        OrderStatusRequest orderStatusRequest = createOrderStatusRequest(OrderStatus.COMPLETION);
        orderService.changeOrderStatus(order1.getId(), orderStatusRequest);

        // when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(order1.getId(), orderStatusRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다.");
    }

    @DisplayName("주문 상태 변경시 정상 저장")
    @Test
    void updateOrderStatus() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블_생성());

        Order order1 = 주문_생성_및_저장(orderTable1, originalAndSeasonedChicken, 1);

        // when
        OrderStatusRequest orderStatusRequest = createOrderStatusRequest(OrderStatus.COMPLETION);
        OrderResponse changedOrder = orderService.changeOrderStatus(order1.getId(), orderStatusRequest);

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    private OrderStatusRequest createOrderStatusRequest(OrderStatus orderStatus) {
        return new OrderStatusRequest(orderStatus.name());
    }
}
