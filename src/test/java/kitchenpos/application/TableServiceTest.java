package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @DisplayName("테이블을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable 테이블_1 = tableService.create(테이블_1());

        assertThat(tableService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(테이블_1);
    }

    @DisplayName("테이블의 빈 상태 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable 테이블_1 = tableService.create(테이블_1());

        테이블_1.updateEmpty(false);
        tableService.changeEmpty(테이블_1.getId(), 테이블_1);

        assertThat(orderTableDao.findById(테이블_1.getId()).orElseThrow().isEmpty())
                .isFalse();
    }

    @DisplayName("테이블은 존재해야 한다.")
    @Test
    void changeEmpty_noTable() {
        long 없는_테이블_ID = 100L;
        OrderTable 테이블_1 = new OrderTable(없는_테이블_ID, null, 0, true);

        테이블_1.updateEmpty(false);

        assertThatThrownBy(() -> tableService.changeEmpty(테이블_1.getId(), 테이블_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블은 차있어야 한다.");
    }

    @DisplayName("테이블은 단체지정이 없어야 한다.")
    @Test
    void changeEmpty_noTableGroup() {
        TableGroup 테이블그룹_1 = tableGroupDao.save(테이블그룹(List.of(빈테이블_1(), 빈테이블_2())));
        OrderTable 단체지정_테이블_1 = orderTableDao.save(테이블_1(테이블그룹_1.getId()));

        assertThatThrownBy(() -> tableService.changeEmpty(단체지정_테이블_1.getId(), 단체지정_테이블_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블은 단체지정이 없어야 한다.");
    }

    @DisplayName("테이블의 주문이 있다면 COMPLETION 상태여야 한다.")
    @Test
    void changeEmpty_noOrderComplete() {
        OrderTable 테이블_1 = orderTableDao.save(테이블_1());
        orderService.create(주문_테이블1());

        assertThatThrownBy(() -> tableService.changeEmpty(테이블_1.getId(), 테이블_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 주문이 있다면 COMPLETION 상태여야 한다.");
    }

    @DisplayName("특정 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        int 손님수 = 100;
        OrderTable 테이블_손님수_변경 = 테이블_손님수_변경(tableService.create(테이블_1()), 손님수);

        OrderTable 저장된_테이블_1 = orderTableDao.findById(테이블_손님수_변경.getId())
                .orElseThrow();

        assertThat(저장된_테이블_1.getNumberOfGuests())
                .isEqualTo(손님수);
    }

    @DisplayName("테이블은 차있어야 한다.")
    @Test
    void changeNumberOfGuests_noFillTable() {
        OrderTable 빈테이블_1 = tableService.create(빈테이블_1());

        assertThatThrownBy(() -> 테이블_손님수_변경(빈테이블_1, 100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블은 차있어야 한다.");
    }

    @DisplayName("테이블 고객 수는 0 이상이어야 한다.")
    @Test
    void changeNumberOfGuests_zeroCustomer() {
        OrderTable 테이블_1 = tableService.create(테이블_1());
        int 음수_손님수 = -1;

        assertThatThrownBy(() -> 테이블_손님수_변경(테이블_1, 음수_손님수))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 고객 수는 0 이상이어야 한다.");
    }

    private OrderTable 테이블_손님수_변경(OrderTable 테이블_1, int numberOfGuests) {
        테이블_1.updateNumberOfGuests(numberOfGuests);
        return tableService.changeNumberOfGuests(테이블_1.getId(), 테이블_1);
    }
}
