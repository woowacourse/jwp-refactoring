package kitchenpos.core.table.application;

import static kitchenpos.fixture.TableFixture.getOrderTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.common.ServiceTest;
import kitchenpos.core.order.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderCreationValidatorTest extends ServiceTest {

    @Autowired
    private OrderCreationValidator validator;

    @Test
    @DisplayName("주문 테이블이 null 이고 주문을 생성할 경우 예외가 발생한다.")
    void createWithNullOrderTables() {
        assertThatThrownBy(() -> validator.validate(Order.of(1L, null, it -> {})))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상품 목록이 없으면 주문을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 테이블이 비어있고 주문을 생성할 경우 예외가 발생한다.")
    void createWithEmptyOrderTables() {
        when(orderTableDao.findById(any())).thenReturn(Optional.of(getOrderTable(true)));

        final Order order = Order.of(1L, Arrays.asList(), it -> {});
        assertThatThrownBy(() -> validator.validate(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상품 목록이 없으면 주문을 생성할 수 없습니다.");
    }
}
