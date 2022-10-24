package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @Test
    void 테이블_생성_메소드는_입력받은_테이블을_저장한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        List<OrderTable> tables = tableService.list();
        assertThat(tables).extracting(OrderTable::getId, OrderTable::getNumberOfGuests, OrderTable::isEmpty)
                .contains(Tuple.tuple(savedOrderTable.getId(), 4, false));
    }

    @Test
    void 테이블_목록_조회_메소드는_모든_테이블을_조회한다() {
        // given
        OrderTable orderTable1 = 테이블을_저장한다(4);
        OrderTable orderTable2 = 빈_테이블을_저장한다();

        // when
        List<OrderTable> tables = tableService.list();

        // then
        assertThat(tables).extracting(OrderTable::getId, OrderTable::getNumberOfGuests, OrderTable::isEmpty)
                .contains(Tuple.tuple(orderTable1.getId(), 4, false), tuple(orderTable2.getId(), 0, true));
    }

    @Nested
    class changeEmpty_메소드는 {

        @Test
        void 테이블_상태를_empty로_변경한다() {
            // given
            OrderTable savedOrderTable = 테이블을_저장한다(4);

            OrderTable emptyOrderTable = new OrderTable();
            emptyOrderTable.setEmpty(true);

            // when
            tableService.changeEmpty(savedOrderTable.getId(), emptyOrderTable);

            // then
            OrderTable findTable = tableService.list()
                    .stream()
                    .filter(table -> table.getId().equals(savedOrderTable.getId()))
                    .findFirst()
                    .orElseThrow();

            assertThat(findTable.isEmpty()).isTrue();
        }

        // TODO
        @Test
        @Disabled
        void 주문_상태가_계산_완료_상태가_아니라면_예외가_발생한다() {
            // given
            OrderTable savedOrderTable = 테이블을_저장한다(4);

            OrderTable emptyOrderTable = new OrderTable();
            emptyOrderTable.setEmpty(true);

            // when
            tableService.changeEmpty(savedOrderTable.getId(), emptyOrderTable);

            // then
            OrderTable findTable = tableService.list()
                    .stream()
                    .filter(table -> table.getId().equals(savedOrderTable.getId()))
                    .findFirst()
                    .orElseThrow();

            assertThat(findTable.isEmpty()).isTrue();
        }

        @Test
        @Disabled
        void 테이블_그룹_id를_가지고_있다면_예외가_발생한다() {
            // given
            OrderTable savedOrderTable = 테이블을_저장한다(4);
            // TODO 테이블 그룹화 코드 추가

            OrderTable emptyOrderTable = new OrderTable();
            emptyOrderTable.setEmpty(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), emptyOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이라면_예외가_발생한다() {
            // given
            OrderTable emptyOrderTable = new OrderTable();
            emptyOrderTable.setEmpty(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(20L, emptyOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class changeNumberOfGuests_메소드는 {

        @Test
        void 테이블_손님_수를_변경한다() {
            // given
            OrderTable savedOrderTable = 테이블을_저장한다(4);

            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(2);
            orderTable.setEmpty(false);

            // when
            tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable);

            // then
            OrderTable findTable = tableService.list()
                    .stream()
                    .filter(table -> table.getId().equals(savedOrderTable.getId()))
                    .findFirst()
                    .orElseThrow();

            assertThat(findTable.getNumberOfGuests()).isEqualTo(2);
        }

        @Test
        void 존재하지_않는_테이블이라면_예외가_발생한다() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(5);
            orderTable.setEmpty(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(20L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_테이블이라면_예외가_발생한다() {
            // given
            OrderTable savedOrderTable = 빈_테이블을_저장한다();

            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(5);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경하려는_손님_수가_음수라면_예외가_발생한다() {
            // given
            OrderTable savedOrderTable = 테이블을_저장한다(4);

            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(-1);
            orderTable.setEmpty(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}