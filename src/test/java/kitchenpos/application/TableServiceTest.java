package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixtures.Fixtures;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    Fixtures fixtures;

    @Autowired
    TableService tableService;

    @Test
    void 주문_테이블을_등록한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(0);

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void 모든_주문_테이블_목록을_불러온다() {
        // given
        fixtures.주문_테이블_저장();

        // when
        List<OrderTable> results = tableService.list();

        // then
        assertThat(results).hasSize(1);
    }


    @Nested
    class 빈_테이블_설정 {

        @Test
        void 주문_테이블을_빈_테이블로_설정한다() {
            // given
            OrderTable orderTable = fixtures.주문_테이블_저장();

            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setEmpty(true);

            // when
            OrderTable result = tableService.changeEmpty(orderTable.getId(), newOrderTable);

            // then
            assertThat(result.getId()).isEqualTo(orderTable.getId());
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        void 존재하지_않는_주문_테이블을_설정하는_경우_예외가_발생한다() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setId(-1L);

            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setEmpty(true);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_단체_지정이_되어있는_경우_예외가_발생한다() {
            // given
            TableGroup tableGroup = fixtures.단체_지정_저장();
            OrderTable orderTable = fixtures.주문_테이블_저장(tableGroup, false);

            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setEmpty(true);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COMPLETION"}, mode = Mode.EXCLUDE)
        void 주문_테이블의_상태가_계산완료_상태가_아닌_경우_예외가_발생한다(OrderStatus orderStatus) {
            // given
            OrderTable orderTable = fixtures.주문_테이블_저장();
            fixtures.주문_저장(orderTable, orderStatus);

            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setEmpty(true);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 방문한_손님_수_설정 {

        @Test
        void 방문한_손님_수를_설정한다() {
            // given
            OrderTable orderTable = fixtures.주문_테이블_저장();

            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setNumberOfGuests(2);

            // when
            OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable);

            // then
            assertThat(result.getId()).isEqualTo(orderTable.getId());
            assertThat(result.getNumberOfGuests()).isEqualTo(2);
        }

        @Test
        void 방문한_손님_수가_음수인_경우_예외가_발생한다() {
            // given
            OrderTable orderTable = fixtures.주문_테이블_저장();

            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setNumberOfGuests(-1);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블을_설정하는_경우_예외가_발생한다() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setId(-1L);

            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setNumberOfGuests(2);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블인_경우_예외가_발생한다() {
            // given
            OrderTable orderTable = fixtures.빈_테이블_저장();

            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setNumberOfGuests(2);

            // when, then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
