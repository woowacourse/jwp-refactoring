package kitchenpos.application.verifier;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.model.order.OrderRepository;
import kitchenpos.domain.model.order.OrderStatus;
import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.ordertable.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class ChangeOrderTableEmptyVerifierTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ChangeOrderTableEmptyVerifier changeOrderTableEmptyVerifier;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(1L, 0, false);
    }

    @DisplayName("테이블 주문 여부 변경")
    @TestFactory
    Stream<DynamicTest> toOrderTable() {
        return Stream.of(
                dynamicTest("테이블 주문 여부를 변경한다.", this::changeEmptySuccess),
                dynamicTest("테이블에 모든 주문이 완료 상태가 아닐때 IllegalArgumentException 발생",
                        this::invalidOrderStatus)
        );
    }

    private void changeEmptySuccess() {
        given(orderTableRepository.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        assertDoesNotThrow(() -> changeOrderTableEmptyVerifier.toOrderTable(orderTable.getId()));
    }

    private void invalidOrderStatus() {
        given(orderTableRepository.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> changeOrderTableEmptyVerifier.toOrderTable(orderTable.getId()));
    }
}