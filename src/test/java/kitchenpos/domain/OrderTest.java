package kitchenpos.domain;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderTest {

    @ParameterizedTest
    @CsvSource(value = {"COMPLETION:true", "MEAL:false", "COOKING:false"}, delimiterString = ":")
    void isComplete(OrderStatus orderStatus, boolean result) {
        Order order = createOrder(null, null, orderStatus.name(), LocalDateTime.now(), Collections.emptyList());

        assertThat(order.isComplete()).isEqualTo(result);
    }
}