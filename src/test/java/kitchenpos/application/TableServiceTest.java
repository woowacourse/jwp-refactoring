package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixtures.테이블_목록_조회;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class TableServiceTest {

    @Autowired
    private TableService sut;

    @DisplayName("테이블을 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void createTable(final Boolean empty) {
        final OrderTableRequest orderTable = new OrderTableRequest(5, empty);

        final OrderTable actual = sut.create(orderTable);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTableGroup()).isNull(),
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

    @DisplayName("주문 테이블이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeNotExistTableAsEmptyTable() {
        final Long 존재하지_않는_테이블_ID = -1L;

        assertThatThrownBy(() -> sut.changeEmpty(존재하지_않는_테이블_ID, new OrderTableRequest(0, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 존재하지 않으면 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsWithNotExistTable() {
        final Long 존재하지_않는_테이블_ID = -1L;

        assertThatThrownBy(() -> sut.changeNumberOfGuests(존재하지_않는_테이블_ID, new OrderTableRequest(-1, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
