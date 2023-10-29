package kitchenpos.domain.service;

import kitchenpos.OrderTableFixtures;
import kitchenpos.application.OrderService;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemCreateRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class OrderCreateServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("주문을 생성할 수 있다")
    void create() {
        //given
        final OrderTable orderTable = orderTableRepository.save(OrderTableFixtures.createWithNotEmpty());
        final OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(1L, 2);

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem));

        //when
        final OrderResponse order = orderService.create(request);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(order.getId()).isNotNull();
            softAssertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            softAssertions.assertThat(order.getOrderedTime()).isNotNull();
        });
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 항목이 없으면 예외가 발생한다")
    void create_fail1() {
        //given
        final OrderTable orderTable = orderTableRepository.save(OrderTableFixtures.createWithNotEmpty());

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), emptyList());

        //when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 필요합니다.");
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 항목 메뉴의 개수와 실제 존재하는 메뉴의 개수가 다르면 예외가 발생한다")
    void create_fail2() {
        //given
        final OrderTable orderTable = orderTableRepository.save(OrderTableFixtures.createWithNotEmpty());
        final OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(0L, 2);

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem));

        //when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 항목입니다.");
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 테이블이 존재하지 않으면 예외가 발생한다")
    void create_fail3() {
        //given
        final OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(1L, 2);

        final OrderCreateRequest request = new OrderCreateRequest(0L, List.of(orderLineItem));

        //when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 테이블이 빈 테이블이면 예외가 발생한다")
    void create_fail4() {
        //given
        final OrderTable orderTable = orderTableRepository.save(OrderTableFixtures.create());
        final OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(1L, 2);

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem));

        //when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 주문 테이블입니다.");
    }

}
