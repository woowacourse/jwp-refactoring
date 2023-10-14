package kitchenpos.application;

import static kitchenpos.common.fixture.OrderFixture.주문;
import static kitchenpos.common.fixture.OrderTableFixture.빈_주문_테이블;
import static kitchenpos.common.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.common.fixture.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTable orderTable = 주문_테이블();

        // when
        OrderTable createdOrderTable = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(createdOrderTable.getId()).isNotNull();
            softly.assertThat(createdOrderTable).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(주문_테이블());
        });
    }

    @Test
    void 전체_주문을_조회한다() {
        // given
        Long orderTableId = orderTableDao.save(주문_테이블()).getId();

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).usingRecursiveComparison()
                .isEqualTo(List.of(주문_테이블(orderTableId, null)));
    }

    @Nested
    class 주문_테이블이_테이블이_비어있는지_여부를_변경할_때 {

        @Test
        void 빈_주문_테이블을_비어있지_않도록_변경한다() {
            // given
            OrderTable emptyOrderTable = tableService.create(빈_주문_테이블());

            // when
            OrderTable orderTable = tableService.changeEmpty(emptyOrderTable.getId(), 주문_테이블());

            // then
            assertThat(orderTable.isEmpty()).isFalse();
        }

        @Test
        void 채워진_주문_테이블을_비어있도록_변경한다() {
            // given
            OrderTable filledOrderTable = tableService.create(주문_테이블());

            // when
            OrderTable orderTable = tableService.changeEmpty(filledOrderTable.getId(), 빈_주문_테이블());

            // then
            assertThat(orderTable.isEmpty()).isTrue();
        }

        @Test
        void 존재하지_않는_주문_테이블을_변경하려고_하면_예외를_던진다() {
            // given
            Long invalidOrderTableId = Long.MIN_VALUE;

            // expect
            assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTableId, 주문_테이블()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_단체_지정되어_있다면_예외를_던진다() {
            // given
            Long tableGroupId = tableGroupDao.save(단체_지정()).getId();

            OrderTable groupedOrderTable = orderTableDao.save(빈_주문_테이블(tableGroupId));

            // expect
            assertThatThrownBy(() -> tableService.changeEmpty(groupedOrderTable.getId(), 주문_테이블()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void 주문_테이블에_조리_혹은_식사_중인_주문이_있다면_예외를_던진다(String orderStatus) {
            // given
            OrderTable orderTable = tableService.create(주문_테이블());
            orderDao.save(주문(orderTable.getId(), orderStatus));

            // expect
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), 빈_주문_테이블()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_테이블의_방문한_손님_수를_변경할_때 {

        @ParameterizedTest
        @CsvSource({"1, 0, 0", "0, 1, 1", "1, 2, 2"})
        void 정상적으로_변경한다(int original, int actual, int expected) {
            // given
            OrderTable zeroOrderTable = tableService.create(주문_테이블(original));

            // when
            OrderTable changedOrderTable = tableService.changeNumberOfGuests(zeroOrderTable.getId(), 주문_테이블(actual));

            // then
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -2})
        void 방문한_손님_수가_0미만이면_예외를_던진다(int numberOfGuests) {
            // given
            OrderTable orderTable = tableService.create(주문_테이블());

            // expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), 주문_테이블(numberOfGuests)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블이면_예외를_던진다() {
            // given
            Long invalidOrderTableId = Long.MIN_VALUE;

            // expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, 주문_테이블(1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있다면_예외를_던진다() {
            // given
            OrderTable emptyOrderTable = tableService.create(빈_주문_테이블());

            // expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyOrderTable.getId(), 주문_테이블(1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
