package acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 인수테스트에서")
public class TableAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void getTables() {
        테이블을_생성한다(0, true);
        테이블을_생성한다(1, true);
        테이블을_생성한다(2, true);

        List<OrderTableResponse> tables = 테이블_목록을_조회한다();

        assertThat(tables).hasSize(3);
    }

    @Test
    @DisplayName("빈 테이블로 변경한다.")
    void changeEmpty() {
        long 테이블 = 테이블을_생성한다(0, false);

        OrderTableResponse response = 테이블_상태를_변경한다(테이블, true);

        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 방문자 수를 변경한다.")
    void changeNumberOfGuest() {
        long 테이블 = 테이블을_생성한다(0, false);

        OrderTableResponse response = 테이블_방문자_수를_변경한다(테이블, 2);

        assertThat(response.getNumberOfGuests()).isEqualTo(2);
    }
}
