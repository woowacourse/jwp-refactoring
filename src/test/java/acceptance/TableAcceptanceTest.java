package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void getTables() {
        // given
        long 테이블1 = 테이블_생성(0, true);
        long 테이블2 = 테이블_생성(1, true);
        long 테이블3 = 테이블_생성(2, true);

        // when
        List<OrderTable> tables = 테이블_목록_조회();

        // then
        assertThat(tables)
                .extracting(OrderTable::getId, OrderTable::getNumberOfGuests, OrderTable::isEmpty)
                .containsExactlyInAnyOrder(
                        tuple(테이블1, 0, true),
                        tuple(테이블2, 1, true),
                        tuple(테이블3, 2, true)
                );
    }
}
