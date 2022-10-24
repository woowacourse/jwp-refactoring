package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTestBase {

    @Test
    void 주문_테이블_생성_성공() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(3);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        Optional<OrderTable> actual = orderTableDao.findById(savedOrderTable.getId());
        assertThat(actual).isNotEmpty();
    }

    @Test
    void 주문_테이블_목록_조회() {
        // given
        OrderTable orderTable = 주문_테이블_생성();
        List<OrderTable> expected = Collections.singletonList(orderTable);

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 주문_테이블_비어있는_상태_변경() {
        // given
        OrderTable orderTable = 주문_테이블_생성();
        boolean changedEmpty = !orderTable.isEmpty();
        orderTable.setEmpty(changedEmpty);

        Long orderTableId = orderTable.getId();

        // when
        tableService.changeEmpty(orderTableId, orderTable);

        // then
        Optional<OrderTable> actual = orderTableDao.findById(orderTableId);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().isEmpty()).isEqualTo(changedEmpty);
    }

    @ParameterizedTest(name = "주문 테이블의 현재 상태: {0}")
    @CsvSource(value = {"MEAL", "COOKING"})
    void 특정_상태의_주문_테이블_비어있는_상태_변경_시_실패(final OrderStatus orderStatus) {
        // given
        OrderTable orderTable = 주문_테이블_생성();
        주문_생성(분식_메뉴_생성(), orderTable, orderStatus);

        boolean changedEmpty = !orderTable.isEmpty();
        orderTable.setEmpty(changedEmpty);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정된_주문_테이블의_상태_변경_시_실패() {
        // given
        TableGroup tableGroup = 단체_지정_생성();
        OrderTable orderTable = 주문_테이블_생성(tableGroup.getId());
        boolean changedEmpty = !orderTable.isEmpty();
        orderTable.setEmpty(changedEmpty);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 방문한_손님_수_변경() {
        // given
        OrderTable orderTable = 주문_테이블_생성();
        int changedNumberOfGuests = orderTable.getNumberOfGuests() + 1;
        orderTable.setNumberOfGuests(changedNumberOfGuests);

        Long orderTableId = orderTable.getId();

        // when
        tableService.changeNumberOfGuests(orderTableId, orderTable);

        // then
        Optional<OrderTable> actual = orderTableDao.findById(orderTableId);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getNumberOfGuests()).isEqualTo(changedNumberOfGuests);
    }

    @Test
    void 방문한_손님_수_0_미만으로_변경_불가능() {
        // given
        OrderTable orderTable = 주문_테이블_생성();
        orderTable.setNumberOfGuests(-1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_주문_테이블의_손님_수는_변경_불가능() {
        // given
        OrderTable orderTable = 주문_테이블_생성();
        orderTable.setNumberOfGuests(5);
        Long invalidOrderTableId = orderTable.getId() + 1;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    
}
