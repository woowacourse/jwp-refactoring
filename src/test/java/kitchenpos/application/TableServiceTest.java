package kitchenpos.application;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.fixture.OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.테이블_그룹이_있는_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableService tableService;

    @Test
    void Order_Table_생성에_성공한다() {
        // given
        OrderTable orderTable = 테이블_그룹이_없는_주문_테이블_생성(1, false);

        // when
        Long orderTableId = tableService.create(orderTable)
                .getId();

        // then
        OrderTable savedOrderTableId = orderTableDao.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(savedOrderTableId).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTable);
    }

    @Test
    void Order_Table_목록을_반환한다() {
        // given
        List<OrderTable> orderTables = List.of(
                테이블_그룹이_없는_주문_테이블_생성(1, false),
                테이블_그룹이_없는_주문_테이블_생성(1, false),
                테이블_그룹이_없는_주문_테이블_생성(1, false)
        );
        List<OrderTable> expected = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            expected.add(tableService.create(orderTable));
        }

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void 저장되어_있지_않은_OrderTable의_empty를_변경하는_경우_실패한다() {
        // given
        OrderTable orderTable = 테이블_그룹이_없는_주문_테이블_생성(1, true);

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(MAX_VALUE, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable이_이미_TableGroup에_속해_있으면_empty_변경이_안된다() {
        // given
        TableGroup savedTableGroup = tableGroupDao.save(TableGroupFixture.빈_테이블_그룹_생성());
        OrderTable orderTable = 테이블_그룹이_있는_주문_테이블_생성(
                savedTableGroup,
                1,
                true
        );
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderTable emptyFalseOrderTable = 테이블_그룹이_있는_주문_테이블_생성(
                savedTableGroup,
                1,
                false
        );

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), emptyFalseOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable에_속해_있는_Order중_단_하나라도_요리중이면_empty_변경이_안된다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(테이블_그룹이_없는_주문_테이블_생성(1, false));
        Order order = 주문을_저장하고_반환받는다(savedOrderTable);
        주문의_상태를_변환한다(order, OrderStatus.COOKING);
        OrderTable emptyTrueOrderTable = 테이블_그룹이_없는_주문_테이블_생성(
                1,
                true
        );

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), emptyTrueOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable에_속해_있는_Order중_단_하나라도_식사중이면_empty_변경이_안된다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(테이블_그룹이_없는_주문_테이블_생성(1, false));
        Order order = 주문을_저장하고_반환받는다(savedOrderTable);
        주문의_상태를_변환한다(order, OrderStatus.MEAL);
        OrderTable emptyTrueOrderTable = 테이블_그룹이_없는_주문_테이블_생성(
                1,
                true
        );

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), emptyTrueOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable_empty를_성공적으로_변경한다() {
        // given
        OrderTable orderTable = 테이블_그룹이_없는_주문_테이블_생성(
                1,
                false
        );
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        Order order = 주문을_저장하고_반환받는다(savedOrderTable);
        주문의_상태를_변환한다(order, OrderStatus.COMPLETION);
        OrderTable emptyTrueOrderTable = 테이블_그룹이_없는_주문_테이블_생성(1, true);

        // when
        tableService.changeEmpty(savedOrderTable.getId(), emptyTrueOrderTable);

        // then
        OrderTable changedOrderTable = orderTableDao.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertAll(
                () -> assertThat(changedOrderTable).usingRecursiveComparison()
                        .ignoringFields("empty")
                        .isEqualTo(savedOrderTable),
                () -> assertThat(changedOrderTable.isEmpty()).isTrue()
        );
    }

    @Test
    void 존재하지_않는_OrderTable은_numberOfGuest를_변경할_수_없다() {
        // given
        OrderTable orderTable = 테이블_그룹이_없는_주문_테이블_생성(2, false);

        // expect
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(MAX_VALUE, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문이_불가능한_상태의_OrderTable은_numberOfGuest_를_변경할_수_없다() {
        // given
        OrderTable originalOrderTable = 테이블_그룹이_없는_주문_테이블_생성(
                1,
                true
        );
        OrderTable savedOrderTable = orderTableDao.save(originalOrderTable);
        OrderTable orderTable = 테이블_그룹이_없는_주문_테이블_생성(
                2,
                true
        );

        // expect
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable의_numberOfGuest를_0미만으로_바꿀_수_없다() {
        // given
        OrderTable originalOrderTable = 테이블_그룹이_없는_주문_테이블_생성(
                1,
                false
        );
        OrderTable savedOrderTable = orderTableDao.save(originalOrderTable);
        OrderTable orderTable = 테이블_그룹이_없는_주문_테이블_생성(
                -1,
                false
        );

        // expect
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable의_numberOfGuest를_성공적으로_변경한다() {
        // given
        OrderTable originalOrderTable = 테이블_그룹이_없는_주문_테이블_생성(
                1,
                false
        );
        OrderTable savedOrderTable = orderTableDao.save(originalOrderTable);
        OrderTable orderTable = 테이블_그룹이_없는_주문_테이블_생성(
                100,
                false
        );

        // when
        tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable);

        // then
        OrderTable changedOrderTable = orderTableDao.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertAll(
                () -> assertThat(changedOrderTable).usingRecursiveComparison()
                        .ignoringFields("numberOfGuests")
                        .isEqualTo(savedOrderTable),
                () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests())
        );
    }

}
