package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("OrderTable의 올바른 생성 예 : 손님이 없을 경우 empty는 true, 손님이 착석할 경우 empty는 false")
    @ParameterizedTest
    @CsvSource(value = {"0, true", "1, false"})
    void createOrderTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        assertThat(orderTable).isNotNull();
    }

    @DisplayName("1명 이상의 손님과 함께 빈 테이블로 등록할 수 없다.")
    @Test
    void createOrderTableException() {
        int numberOfGuests = 1;

        assertThatThrownBy(() -> new OrderTable(numberOfGuests, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(numberOfGuests + "명 : 1명 이상의 손님과 함께 빈 테이블로 등록할 수 없습니다.");
    }

    @DisplayName("주문 테이블의 empty 여부를 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {"true, false", "false, true"})
    void changeEmpty(boolean empty, boolean expect) {
        OrderTable orderTable = new OrderTable(0, empty);

        orderTable.changeEmpty(expect, false);

        assertThat(orderTable.isEmpty()).isEqualTo(expect);
    }

    @DisplayName("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmptyException1(boolean empty) {
        OrderTable orderTable = new OrderTable(1, false);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        orderTable.groupBy(tableGroup);

        assertThatThrownBy(() -> orderTable.changeEmpty(empty, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
    }

    @DisplayName("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmptyException2(boolean empty) {
        OrderTable orderTable = new OrderTable(1, false);

        assertThatThrownBy(() -> orderTable.changeEmpty(empty, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(1, false);

        orderTable.changeNumberOfGuests(10);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("방문한 손님 수가 0명 미만이면 입력할 수 없다.")
    @Test
    void changeNumberOfGuestsException1() {
        OrderTable orderTable = new OrderTable(1, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("방문한 손님 수가 0명 미만이면 입력할 수 없습니다.");
    }

    @DisplayName("빈 테이블은 방문한 손님 수를 입력할 수 없다.")
    @Test
    void changeNumberOfGuestsException2() {
        OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블은 방문한 손님 수를 입력할 수 없습니다.");
    }

    @DisplayName("OrderTable을 단체로 지정한다.")
    @Test
    void groupBy() {
        OrderTable orderTable = new OrderTable(1, false);
        TableGroup tableGroup = new TableGroup(1L);

        orderTable.groupBy(tableGroup);

        assertThat(orderTable.getIdOfTableGroup()).isEqualTo(tableGroup.getId());
    }

    @DisplayName("OrderTable을 단체로 지정한다.")
    @Test
    void groupByException1() {
        OrderTable orderTable = new OrderTable(1, false);

        assertThatThrownBy(() -> orderTable.groupBy(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 TableGroup입니다.");
    }

    @DisplayName("단체 지정은 중복될 수 없다.")
    @Test
    void groupByException2() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.groupBy(new TableGroup(1L));

        assertThatThrownBy(() -> orderTable.groupBy(new TableGroup(2L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 지정은 중복될 수 없습니다.");
    }

    @Test
    void ungroup() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.groupBy(new TableGroup(1L));

        orderTable.ungroup();

        assertThat(orderTable.isEmpty()).isFalse();
        assertThat(orderTable.getIdOfTableGroup()).isNull();
    }
}