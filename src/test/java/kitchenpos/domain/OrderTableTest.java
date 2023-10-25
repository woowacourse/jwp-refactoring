package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.order.domain.exception.OrderTableException.EmptyTableException;
import kitchenpos.order.domain.exception.OrderTableException.ExistsTableGroupException;
import kitchenpos.order.domain.exception.OrderTableException.InvalidNumberOfGuestsException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블을 생성할 수 있다.")
    void init() {
        OrderTable orderTable = new OrderTable(10);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(orderTable.getTableGroup()).isNull();
            softAssertions.assertThat(orderTable.isEmpty()).isTrue();
        });
    }

    @Test
    @DisplayName("주문 테이블의 상태를 변경할 수 있다.")
    void changeEmpty_success() {
        OrderTable orderTable = new OrderTable(10);

        orderTable.changeEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 그룹이 있으면 주문 테이블의 상태를 변경할 수 없다.")
    void changeEmpty_fail() {
        OrderTable orderTable = new OrderTable(10);
        orderTable.setTableGroup(TableGroup.from(List.of(new OrderTable(10), new OrderTable(10))));

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(ExistsTableGroupException.class);
    }

    @Test
    @DisplayName("주문 테이블의 인원 수를 변경할 수 있다.")
    void changeNumberOfGuest_success() {
        OrderTable orderTable = new OrderTable(10);
        orderTable.changeEmpty(false);

        orderTable.changeNumberOfGuest(100);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(100);
    }

    @Test
    @DisplayName("주문 테이블의 인원 수가 0명 미만인 경우 주문 테이블의 상태를 변경할 수 없다.")
    void changeNumberOfGuest_fail1() {
        OrderTable orderTable = new OrderTable(10);
        orderTable.changeEmpty(false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuest(-1))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있는 경우 주문 테이블의 상태를 변경할 수 없다.")
    void changeNumberOfGuest_fail2() {
        OrderTable orderTable = new OrderTable(10);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuest(10))
                .isInstanceOf(EmptyTableException.class);
    }
}
