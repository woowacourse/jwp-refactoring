package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.Fixture.Fixture.orderFixture;
import static kitchenpos.Fixture.Fixture.orderTableFixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class TableServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableService tableService;

    @Test
    void 테이블을_등록할_수_있다() {
        OrderTable orderTable = orderTableFixture(null, 0, false);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertSoftly(softly -> {
            softly.assertThat(savedOrderTable.getId()).isNotNull();
            softly.assertThat(savedOrderTable).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(orderTable);
        });
    }

    @Test
    void 테이블_목록을_조회한다() {
        OrderTable orderTable1 = tableService.create(orderTableFixture(null, 1, false));
        OrderTable orderTable2 = tableService.create(orderTableFixture(null, 0, true));

        List<OrderTable> tableList = tableService.list();

        assertThat(tableList).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(orderTable1, orderTable2);
    }

    @DirtiesContext
    @Nested
    class 테이블_상태_변경 {
        @Test
        void 테이블을_빈_테이블로_변경한다() {
            OrderTable orderTable = tableService.create(orderTableFixture(null, 0, false));
            orderTable.setEmpty(true);
            OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

            assertThat(changedOrderTable.isEmpty()).isTrue();
        }

        @Test
        void 테이블이_존재하지_읺으면_상태를_변경할_수_없다() {
            OrderTable notExistTable = orderTableFixture(null, 10, false);
            long notExistTableId = Long.MIN_VALUE;

            assertThatThrownBy(() -> tableService.changeEmpty(notExistTableId, notExistTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블이 존재하지 않습니다. 빈 테이블로 변경할 수 없습니다.");
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 테이블의_주문상태가_조리_또는_식사인_주문_테이블은_빈_테이블로_변경할_수_없다(OrderStatus orderStatus) {
            OrderTable orderTable = tableService.create(orderTableFixture(null, 1, false));
            Order order = orderFixture(orderTable.getId(), orderStatus.name(), LocalDateTime.now(), null);
            orderDao.save(order);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블의 주문이 조리, 혹은 식사 상태이면 빈 테이블로 변경할 수 없습니다.");

        }
    }

    @DirtiesContext
    @Nested
    class 방문한_손님_등록 {
        @Test
        void 테이블에_방문한_손님_수를_등록한다() {
            OrderTable orderTable = tableService.create(orderTableFixture(null, 0, false));
            orderTable.setNumberOfGuests(1);

            tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

            assertThat(orderTable.getNumberOfGuests()).isOne();
        }

        @Test
        void 방문한_손님_수가_0명_미만이면_등록할_수_없다() {
            int lowe0NumberOfGuest = -1;
            OrderTable orderTable = tableService.create(orderTableFixture(null, lowe0NumberOfGuest, false));

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("방문한 손님 수는 0명 이상이어야 합니다.");
        }

        @Test
        void 테이블이_존재하지_않으면_방문한_손님_수를_등록할_수_없다() {
            OrderTable notExistTable = orderTableFixture(null, 1, false);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistTable.getId(), notExistTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블이 존재하지 않습니다. 방문한 손님 수를 등록할 수 없습니다.");
        }
    }
}
