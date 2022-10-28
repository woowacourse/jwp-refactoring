package kitchenpos.domain;

import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE1;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE2;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE3;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    void throw_exception_when_orderTables_empty() {
        assertThatThrownBy(() -> new OrderTables(new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateIsSameSize_fail_when_different_size() {
        OrderTables orderTables = new OrderTables(List.of(ORDER_TABLE1, ORDER_TABLE2, ORDER_TABLE3));

        assertThatThrownBy(() ->orderTables.validateIsSameSize(List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}