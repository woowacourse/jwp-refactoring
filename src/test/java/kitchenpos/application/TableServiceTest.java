package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.빈_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.application.support.IntegrationTest;
import kitchenpos.dao.OrderDao;
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
    private OrderDao orderDao;

    @Autowired
    OrderTableDao orderTableDao;

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
        final OrderTable 주문_테이블_1번 = 빈_테이블_생성(5);
        orderTableDao.save(주문_테이블_1번);

        final OrderTable 주문_테이블_2번 = 주문_테이블_생성(5);
        orderTableDao.save(주문_테이블_2번);

        final OrderTable 주문_테이블_3번 = 주문_테이블_생성(6);
        orderTableDao.save(주문_테이블_3번);

        assertThat(sut.list())
                .hasSize(3)
                .extracting("numberOfGuests", "empty")
                .containsExactly(
                        tuple(주문_테이블_1번.getNumberOfGuests(), 주문_테이블_1번.isEmpty()),
                        tuple(주문_테이블_2번.getNumberOfGuests(), 주문_테이블_2번.isEmpty()),
                        tuple(주문_테이블_3번.getNumberOfGuests(), 주문_테이블_3번.isEmpty())
                );
    }

    @Nested
    @DisplayName("빈 테이블 변경")
    class EmptyTableChangeTest {

        @DisplayName("정상적인 경우 빈 테이블로 변경할 수 있다.")
        @Test
        void changeEmptyTable() {
            final OrderTable 주문_테이블 = orderTableDao.save(주문_테이블_생성(5));

            final OrderTable actual = sut.changeEmpty(주문_테이블.getId(), 빈_테이블_생성(주문_테이블.getNumberOfGuests()));

            assertThat(actual.isEmpty()).isTrue();
        }

        @DisplayName("주문 테이블이 존재하지 않으면 변경할 수 없다.")
        @Test
        void changeNotExistTableAsEmptyTable() {
            final Long 존재하지_않는_주문_테이블_ID = -1L;

            assertThatThrownBy(() -> sut.changeEmpty(존재하지_않는_주문_테이블_ID, 빈_테이블_생성(5)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("테이블 손님 수 변경")
    class changeNumberOfGuestsTest {

        @DisplayName("정상적인 경우 손님 수를 변경할 수 있다.")
        @Test
        void changeNumberOfGuests() {
            final OrderTable 주문_테이블 = orderTableDao.save(주문_테이블_생성(5));

            final OrderTable actual = sut.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_생성(10));

            assertThat(actual.getNumberOfGuests()).isEqualTo(10);
        }

        @DisplayName("손님 수를 0명 미만으로 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsLessThanZero() {
            final OrderTable 주문_테이블 = orderTableDao.save(주문_테이블_생성(5));

            assertThatThrownBy(() -> sut.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_생성(-1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 존재하지 않으면 손님 수를 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsWithNotExistTable() {
            final Long 존재하지_않는_테이블_ID = -1L;

            assertThatThrownBy(() -> sut.changeNumberOfGuests(존재하지_않는_테이블_ID, 주문_테이블_생성(10)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블이면 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsWithEmptyTable() {
            final OrderTable 빈_테이블 = orderTableDao.save(빈_테이블_생성(5));

            assertThatThrownBy(() -> sut.changeNumberOfGuests(빈_테이블.getId(), 주문_테이블_생성(10)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
