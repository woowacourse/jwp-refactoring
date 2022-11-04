package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.repository.OrderTableDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTablesTest {

    @DisplayName("주문 테이블의 원소 개수와 매개변수의 원소 개수가 다르면 예외가 발생한다.")
    @ValueSource(ints = {2, 3, 4, 5})
    @ParameterizedTest
    void validateOrderTableSize(int size) {
        OrderTables orderTables = new OrderTables(List.of(new OrderTable(0, true)));
        assertThatThrownBy(
                () -> orderTables.validateOrderTableSize(size)
        );
    }

    @DisplayName("주문 테이블을 순서대로 저장하고 리스트로 반환한다.")
    @Test
    void mapToTableGroup() {
        OrderTableDao orderTableDao = mock(OrderTableDao.class);

        OrderTables orderTables = new OrderTables(
                List.of(new OrderTable(1, true), new OrderTable(0, true)));
        List<OrderTable> orderTables1 = orderTables.mapToOrderTables(orderTableDao::save);

        assertThat(orderTables1).hasSize(2);
    }
}
