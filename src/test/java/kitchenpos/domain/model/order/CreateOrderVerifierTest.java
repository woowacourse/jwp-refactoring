package kitchenpos.domain.model.order;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.model.menu.MenuRepository;
import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.ordertable.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class CreateOrderVerifierTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private CreateOrderVerifier createOrderVerifier;

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
        Order order = createOrder();
        given(menuRepository.countByIdIn(any())).willReturn((long)order.getOrderLineItems().size());
        given(orderTableRepository.findById(order.getOrderTableId()))
                .willReturn(Optional.of(new OrderTable(1L, null, 1, false)));

        assertDoesNotThrow(
                () -> createOrderVerifier.toOrder(ORDER_CREATE_REQUEST.getOrderLineItems(),
                        ORDER_CREATE_REQUEST.getOrderTableId()));
    }

    private void orderLineItemsAndMenuMismatch() {
        Order order = createOrder();
        given(menuRepository.countByIdIn(any())).willReturn(
                (long)order.getOrderLineItems().size() + 1);

        throwIllegalArgumentException();
    }

    private void noOrderTable() {
        Order order = createOrder();
        given(menuRepository.countByIdIn(any())).willReturn((long)order.getOrderLineItems().size());
        given(orderTableRepository.findById(order.getOrderTableId()))
                .willReturn(Optional.empty());

        throwIllegalArgumentException();
    }

    private void emptyOrderTable() {
        Order order = createOrder();
        given(menuRepository.countByIdIn(any())).willReturn((long)order.getOrderLineItems().size());
        given(orderTableRepository.findById(order.getOrderTableId()))
                .willReturn(Optional.of(new OrderTable(1L, null, 1, true)));

        throwIllegalArgumentException();
    }

    private Order createOrder() {
        return new Order(null, ORDER_CREATE_REQUEST.getOrderTableId(), null, null,
                ORDER_CREATE_REQUEST.getOrderLineItems());
    }

    private void throwIllegalArgumentException() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createOrderVerifier.toOrder(ORDER_CREATE_REQUEST.getOrderLineItems(),
                        ORDER_CREATE_REQUEST.getOrderTableId()));
    }
}
