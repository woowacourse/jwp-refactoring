package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.TestFixture;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderStatusRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.repository.OrderTableRepository;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class OrderServiceTest {

    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;
    private final TestFixture testFixture;

    OrderTable 주문_테이블;
    Menu 고기_메뉴;

    public OrderServiceTest(OrderService orderService, OrderTableRepository orderTableRepository,
                            TestFixture testFixture) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
        this.testFixture = testFixture;
    }

    @BeforeEach
    void setUp() {
        주문_테이블 = testFixture.주문_테이블을_생성한다(100, false);
        Product 삼겹살 = testFixture.상품을_생성한다("삼겹살", 1000L);
        MenuGroup 고기류 = testFixture.메뉴_분류를_생성한다("고기류");
        고기_메뉴 = testFixture.메뉴를_각_상품당_한개씩_넣어서_생성한다("고기 메뉴", BigDecimal.valueOf(1000L), List.of(삼겹살), 고기류.getId());
    }

    @DisplayName("주문 내에 메뉴가 비어있다면 예외가 발생한다.")
    @Test
    public void orderSaveWithOrderLineItemsIsEmpty() {
        OrderRequest request = new OrderRequest(주문_테이블.getId(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문한 메뉴가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void orderSaveWithOrderTableNotSaved() {
        OrderLineItemRequest 없는_메뉴_주문 = new OrderLineItemRequest(-1L, 10L);
        OrderRequest request = new OrderRequest(주문_테이블.getId(), List.of(없는_메뉴_주문));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 EMPTY 상태인 경우 예외가 발생한다.")
    @Test
    void orderSaveWithOrderTableIsEmpty() {
        OrderTable EMPTY_상태인_주문_테이블 = testFixture.주문_테이블을_생성한다(100, true);
        OrderLineItemRequest 주문_메뉴 = new OrderLineItemRequest(고기_메뉴.getId(), 10L);
        OrderRequest request = new OrderRequest(EMPTY_상태인_주문_테이블.getId(), List.of(주문_메뉴));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 성공적으로 저장되는 경우를 테스트한다")
    @Test
    void orderSave() {
        OrderLineItemRequest 주문_메뉴 = new OrderLineItemRequest(고기_메뉴.getId(), 10L);
        OrderRequest request = new OrderRequest(주문_테이블.getId(), List.of(주문_메뉴));
        Order savedOrder = orderService.create(request);

        assertThat(savedOrder.getId()).isNotNull();
    }

    @DisplayName("주문 리스트를 출력한다.")
    @Test
    void list() {
        OrderLineItemRequest 주문_메뉴 = new OrderLineItemRequest(고기_메뉴.getId(), 10L);
        OrderRequest request = new OrderRequest(주문_테이블.getId(), List.of(주문_메뉴));
        orderService.create(request);

        assertThat(orderService.list()).hasSize(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeStatus() {
        OrderLineItemRequest 주문_메뉴 = new OrderLineItemRequest(고기_메뉴.getId(), 10L);
        OrderRequest request = new OrderRequest(주문_테이블.getId(), List.of(주문_메뉴));
        Order order = orderService.create(request);

        Order 완료된_상태의_주문 = orderService.changeOrderStatus(order.getId(), new OrderStatusRequest("COMPLETION"));

        assertThat(완료된_상태의_주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}
