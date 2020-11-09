package kitchenpos.domain.entity;

import static java.util.Collections.*;
import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.service.OrderCreateService;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderCreateService orderCreateService;

    @DisplayName("주문 생성")
    @TestFactory
    Stream<DynamicTest> create() {
        return Stream.of(
                dynamicTest("주문을 생성한다.", this::createSuccess),
                dynamicTest("주문 항목은 하나의 메뉴를 가진다.", this::orderLineItemsAndMenuMismatch),
                dynamicTest("테이블이 존재해야 한다.", this::noOrderTable),
                dynamicTest("테이블은 빈 테이블 일 수 없다.", this::emptyOrderTable)
        );
    }

    private void createSuccess() {
        Order order = ORDER_CREATE_REQUEST.toEntity();
        given(menuRepository.countByIdIn(any())).willReturn((long)order.getOrderLineItems().size());
        given(orderTableRepository.findById(order.getOrderTableId()))
                .willReturn(Optional.of(new OrderTable(1L, null, 1, false)));

        Order created = order.create(orderCreateService);

        assertAll(
                () -> assertThat(created.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(created.getOrderedTime()).isNotNull()
        );
    }

    private void orderLineItemsAndMenuMismatch() {
        Order order = ORDER_CREATE_REQUEST.toEntity();
        given(menuRepository.countByIdIn(any())).willReturn(
                (long)order.getOrderLineItems().size() + 1);

        assertThatIllegalArgumentException().isThrownBy(() -> order.create(orderCreateService));
    }

    private void noOrderTable() {
        Order order = ORDER_CREATE_REQUEST.toEntity();
        given(menuRepository.countByIdIn(any())).willReturn((long)order.getOrderLineItems().size());
        given(orderTableRepository.findById(order.getOrderTableId()))
                .willReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.create(orderCreateService));
    }

    private void emptyOrderTable() {
        Order order = ORDER_CREATE_REQUEST.toEntity();
        given(menuRepository.countByIdIn(any())).willReturn((long)order.getOrderLineItems().size());
        given(orderTableRepository.findById(order.getOrderTableId()))
                .willReturn(Optional.of(new OrderTable(1L, null, 1, true)));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.create(orderCreateService));
    }

    @DisplayName("주문 상태 변경")
    @TestFactory
    Stream<DynamicTest> changeOrderStatus() {
        return Stream.of(
                dynamicTest("주문 상태를 변경한다.", this::changeOrderStatusSuccess),
                dynamicTest("주문의 상태가 완료일 수 없다.", this::invalidOrder)
        );
    }

    private void changeOrderStatusSuccess() {
        Order order = new Order(1L, 1L, "MEAL", LocalDateTime.now(),
                singletonList(new OrderLineItem(1L, 1L, 1L, 1L)));

        order.changeOrderStatus(ORDER_STATUS_CHANGE_REQUEST2.getOrderStatus());

        assertThat(order.getOrderStatus()).isEqualTo(ORDER_STATUS_CHANGE_REQUEST2.getOrderStatus());
    }

    private void invalidOrder() {
        Order order = new Order(1L, 1L, "COMPLETION", LocalDateTime.now(),
                singletonList(new OrderLineItem(1L, 1L, 1L, 1L)));

        assertThatIllegalArgumentException().isThrownBy(
                () -> order.changeOrderStatus(ORDER_STATUS_CHANGE_REQUEST1.getOrderStatus()));
    }
}
