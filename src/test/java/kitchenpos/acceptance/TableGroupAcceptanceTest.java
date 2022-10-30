package kitchenpos.acceptance;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("둘 이상의 테이블을 그룹으로 묶을 수 있다")
    void createTableGroup() {
        final TableGroup requestBody = TableGroup.of(List.of(빈_테이블1, 빈_테이블2));

        final var response = 테이블_그룹_생성_요청(requestBody);
        final var responseBody = response.body().as(TableGroup.class);

        final List<OrderTable> orderTables = responseBody.getOrderTables();
        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.CREATED),
                () -> 리스트_데이터_검증(orderTables, "id", 빈_테이블1.getId(), 빈_테이블2.getId()),
                () -> 리스트_데이터_검증(orderTables, "tableGroupId", responseBody.getId(), responseBody.getId()),
                () -> 리스트_데이터_검증(orderTables, "numberOfGuests", 빈_테이블1.getNumberOfGuests(), 빈_테이블2.getNumberOfGuests()),
                () -> 리스트_데이터_검증(orderTables, "empty", false, false)
        );
    }

    @Test
    @DisplayName("그룹으로 묶인 테이블의 그룹을 해제할 수 있다.")
    void ungroup() {
        final var tableGroup = 테이블_그룹_생성(빈_테이블1, 빈_테이블2);

        final var response = 테이블_그룹_해제_요청(tableGroup);

        final OrderTable orderTable1 = orderTableRepository.findById(빈_테이블1.getId())
                .orElseThrow();
        final OrderTable orderTable2 = orderTableRepository.findById(빈_테이블1.getId())
                .orElseThrow();
        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.NO_CONTENT),
                () -> 단일_데이터_검증(orderTable1.getTableGroupId(), null),
                () -> 단일_데이터_검증(orderTable2.getTableGroupId(), null)
        );
    }
}
