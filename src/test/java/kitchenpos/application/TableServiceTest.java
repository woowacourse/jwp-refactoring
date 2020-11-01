package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.Orderz;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        OrderTable orderTable = new OrderTable();
        orderTable.updateEmpty(false);
        orderTable.setNumberOfGuests(10);

        OrderTable result = tableService.create(orderTable);
        OrderTable savedOrderTable = orderTableRepository.findById(result.getId())
                .orElseThrow(() -> new NoSuchElementException("저장되지 않았습니다."));
        assertThat(savedOrderTable.getTableGroup()).isEqualTo(orderTable.getTableGroup());
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("empty 여부를 변경한다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmptyTest(final boolean EXPECTED) {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.updateEmpty(!EXPECTED);
        orderTable.setNumberOfGuests(10);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        Orderz order = new Orderz();
        order.setOrderTableId(savedOrderTable.getId());
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
        OrderTable orderTable = new OrderTable();
        orderTable.updateEmpty(false);
        orderTable.setNumberOfGuests(10);

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        savedOrderTable.setNumberOfGuests(EXPECTED);

        // when
        OrderTable result = tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(EXPECTED);
    }

    @DisplayName("0명 미만의 손님은 불가능하다.")
    @Test
    void invalidNumberOfGuestsTest() {
        final int INVALID_NUMBER_OF_GUESTS = -1;

        // given
        OrderTable orderTable = new OrderTable();
        orderTable.updateEmpty(false);
        orderTable.setNumberOfGuests(10);

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        savedOrderTable.setNumberOfGuests(INVALID_NUMBER_OF_GUESTS);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이면 손님의 인원 수를 수정할 수 없다.")
    @Test
    void changeNumberOfGuestsExceptionTest() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.updateEmpty(true);
        orderTable.setNumberOfGuests(10);

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}