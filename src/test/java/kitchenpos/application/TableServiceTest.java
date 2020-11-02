package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("OrderTable을 생성하고 DB에 저장한다.")
    @Test
    void createTest() {
        OrderTable orderTable = new OrderTable(10, false);

        OrderTable result = tableService.create(orderTable);
        OrderTable savedOrderTable = orderTableRepository.findById(result.getId())
                .orElseThrow(() -> new NoSuchElementException("저장되지 않았습니다."));
        assertThat(savedOrderTable.getTableGroup()).isEqualTo(orderTable.getTableGroup());
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("empty 여부를 변경한다.")
    @Test
    void changeEmptyTest() {
        final boolean EXPECTED = false;

        // given
        OrderTable orderTable = new OrderTable(10, !EXPECTED);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        Order order = new Order(savedOrderTable, Collections.emptyList());
        order.updateOrderStatus(OrderStatus.COMPLETION.name());
        orderRepository.save(order);

        savedOrderTable.updateEmpty(EXPECTED);

        // when
        OrderTable result = tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable);

        // then
        assertThat(result.isEmpty()).isEqualTo(EXPECTED);
    }

    @DisplayName("numberOfGuests 변경한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 100})
    void changeNumberOfGuestsTest(final int EXPECTED) {
        // given
        OrderTable orderTable = new OrderTable(10, false);

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        savedOrderTable.updateNumberOfGuests(EXPECTED);

        // when
        OrderTable result = tableService.changeNumberOfGuests(savedOrderTable.getId(), EXPECTED);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(EXPECTED);
    }
}