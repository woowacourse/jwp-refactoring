package kitchenpos.acceptance;

import static kitchenpos.adapter.presentation.web.OrderTableRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.application.command.ChangeNumberOfOrderTableGuestsCommand;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.domain.model.ordertable.OrderTable;

public class TableAcceptanceTest extends AcceptanceTest {
    /**
     * 테이블을 관리한다.
     * <p>
     * When 테이블 생성 요청.
     * Then 테이블이 생성 된다.
     * <p>
     * Given 테이블이 생성 되어 있다.
     * When 테이블 전체 조회 요청.
     * Then 전체 테이블을 반환한다.
     * <p>
     * When 테이블 주문 여부 변경 요청.
     * Then 변경된 테이블 정보를 반환한다.
     * <p>
     * When 테이블 손님 수 변경 요청.
     * Then 변경된 테이블 정보를 반환한다.
     */
    @DisplayName("테이블 관리")
    @TestFactory
    Stream<DynamicTest> manageTable() throws Exception {
        // 테이블 생성
        Long orderTableId = createTable();
        assertThat(orderTableId).isNotNull();

        return Stream.of(
                dynamicTest("테이블 전체 조회", () -> {
                    List<OrderTableResponse> tables = getAll(OrderTableResponse.class, API_TABLES);
                    OrderTableResponse lastTable = getLastItem(tables);

                    assertThat(lastTable.getId()).isEqualTo(orderTableId);
                }),
                dynamicTest("테이블 주문 여부 변경", () -> {
                    boolean empty = false;
                    OrderTableResponse response = changeOrderTableEmpty(empty, orderTableId);

                    assertAll(
                            () -> assertThat(response.getId()).isEqualTo(orderTableId),
                            () -> assertThat(response.isEmpty()).isEqualTo(empty)
                    );
                }),
                dynamicTest("테이블 손님 수 변경", () -> {
                    int numberOfGuests = 3;
                    OrderTableResponse response = changeNumberOfGuests(numberOfGuests,
                            orderTableId);

                    assertAll(
                            () -> assertThat(response.getId()).isEqualTo(orderTableId),
                            () -> assertThat(response.getNumberOfGuests()).isEqualTo(
                                    numberOfGuests)
                    );
                })
        );
    }

    private Long createTable() throws Exception {
        OrderTable orderTable = new OrderTable(null, null, 0, true);

        String request = objectMapper.writeValueAsString(orderTable);
        return post(request, API_TABLES);
    }

    private OrderTableResponse changeNumberOfGuests(int numberOfGuests, Long orderTableId) throws Exception {
        ChangeNumberOfOrderTableGuestsCommand request = new ChangeNumberOfOrderTableGuestsCommand(
                numberOfGuests);

        return put(OrderTableResponse.class, objectMapper.writeValueAsString(request),
                API_TABLES + "/" + orderTableId + "/number-of-guests");
    }
}
