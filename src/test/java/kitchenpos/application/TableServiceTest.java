package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.order.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void create() {
        final OrderTable savedOrderTable1 = 테이블_등록();
        final OrderTable savedOrderTable2 = 테이블_등록();
        final OrderTable savedOrderTable3 = 테이블_등록();

        final List<OrderTable> orderTables = 테이블_전체_조회();

        assertAll(
                () -> assertThat(orderTables).usingElementComparatorIgnoringFields()
                        .contains(savedOrderTable1, savedOrderTable2, savedOrderTable3),
                () -> assertThat(savedOrderTable1.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("테이블 그룹에 속한 테이블은 비우거나 채울 수 없다.")
    void changeEmptyWithTableGroup() {
        테이블을_그룹으로_묶는다(빈_테이블1, 빈_테이블2);

        assertAll(
                () -> assertThatThrownBy(() -> 테이블_채움(빈_테이블1.getId()))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> 테이블_비움(빈_테이블1.getId()))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("조리 상태인 테이블은 빈 테이블이 될 수 없다.")
    void changeEmptyWithCookingStatus() {
        주문_요청한다(손님있는_테이블, 파스타한상);

        assertThatThrownBy(() -> tableService.changeEmpty(손님있는_테이블.getId(), 손님있는_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("식사 상태인 테이블은 빈 테이블이 될 수 없다.")
    void changeEmptyWithMealStatus() {
        assertThatThrownBy(() -> tableService.changeEmpty(손님있는_식사중_테이블.getId(), 손님있는_식사중_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 손님의 수는 0보다 작을 수 없다.")
    void changeNumberOfGuestsUnder0() {
        assertThatThrownBy(() -> 테이블_손님_수_변경(손님있는_테이블.getId(), -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블의 손님 수를 변경할 수 없다.")
    void changeNumberOfGuestsEmptyTable() {
        assertThatThrownBy(() -> 테이블_손님_수_변경(빈_테이블1.getId(), 20))
                .isInstanceOf(IllegalArgumentException.class);
    }
}