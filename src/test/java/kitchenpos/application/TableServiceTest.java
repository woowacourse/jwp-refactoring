package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixtures.빈_테이블1;
import static kitchenpos.fixture.OrderTableFixtures.주문_테이블9;
import static kitchenpos.fixture.OrderTableFixtures.테이블_목록_조회;
import static kitchenpos.fixture.OrderTableFixtures.테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class TableServiceTest {

    @Autowired
    private TableService sut;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("테이블을 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void createTable(final Boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(5);
        orderTable.setEmpty(empty);

        final OrderTable actual = sut.create(orderTable);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTableGroupId()).isNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(5),
                () -> assertThat(actual.isEmpty()).isEqualTo(empty)
        );
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void getTables() {
        final List<OrderTable> orderTables = 테이블_목록_조회();

        assertThat(sut.list())
                .hasSize(8)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(orderTables);
    }

    @Nested
    @DisplayName("빈 테이블 변경")
    class EmptyTableChangeTest {

        @DisplayName("정상적인 경우 빈 테이블로 변경할 수 있다.")
        @Test
        void changeEmptyTable() {
            final OrderTable table = orderTableDao.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));

            final OrderTable actual =
                    sut.changeEmpty(table.getId(), 테이블_생성(table.getNumberOfGuests(), true));

            assertThat(actual.isEmpty()).isTrue();
        }

        @DisplayName("주문 테이블이 존재하지 않으면 변경할 수 없다.")
        @Test
        void changeNotExistTableAsEmptyTable() {
            final Long 존재하지_않는_테이블_ID = -1L;

            assertThatThrownBy(() -> sut.changeEmpty(존재하지_않는_테이블_ID, 테이블_생성(0, true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("테이블 손님 수 변경")
    class changeNumberOfGuestsTest {

        @DisplayName("정상적인 경우 손님 수를 변경할 수 있다.")
        @Test
        void changeNumberOfGuests() {
            final OrderTable table = orderTableDao.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));

            final OrderTable actual =
                    sut.changeNumberOfGuests(table.getId(), 테이블_생성(10, table.isEmpty()));

            assertThat(actual.getNumberOfGuests()).isEqualTo(10);
        }

        @DisplayName("손님 수를 0명 미만으로 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsLessThanZero() {
            final OrderTable table = orderTableDao.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));

            assertThatThrownBy(() -> sut.changeNumberOfGuests(table.getId(), 테이블_생성(-1, table.isEmpty())))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 존재하지 않으면 손님 수를 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsWithNotExistTable() {
            final Long 존재하지_않는_테이블_ID = -1L;

            assertThatThrownBy(() -> sut.changeNumberOfGuests(존재하지_않는_테이블_ID, 테이블_생성(-1, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블이면 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsWithEmptyTable() {
            final OrderTable table = orderTableDao.save(테이블_생성(빈_테이블1.getNumberOfGuests(), 빈_테이블1.isEmpty()));

            assertThatThrownBy(
                    () -> sut.changeNumberOfGuests(table.getId(), 테이블_생성(10, table.isEmpty()))
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
