package kitchenpos.table;

import static kitchenpos.ui.TableRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;

public class TableAcceptanceTest extends AcceptanceTest {
    /**
     * 테이블을 관리한다.
     * <p>
     * When 테이블을 생성 요청.
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
                    List<OrderTable> tables = getAll(OrderTable.class, API_TABLES);
                    OrderTable lastTable = getLastItem(tables);

                    assertThat(lastTable.getId()).isEqualTo(orderTableId);
                }),
                dynamicTest("테이블 주문 여부 변경", () -> {
                    boolean empty = false;
                    OrderTable orderTable = changeEmpty(empty, orderTableId);

                    assertThat(orderTable.getId()).isEqualTo(orderTableId);
                    assertThat(orderTable.isEmpty()).isEqualTo(empty);
                }),
                dynamicTest("테이블 손님 수 변경", () -> {
                    int numberOfGuests = 3;
                    OrderTable orderTable = changNumberOfGuests(numberOfGuests, orderTableId);

                    assertThat(orderTable.getId()).isEqualTo(orderTableId);
                    assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
                })
        );
    }

    private Long createTable() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        String request = objectMapper.writeValueAsString(orderTable);
        return post(request, API_TABLES);
    }

    private OrderTable changeEmpty(boolean empty, Long orderTableId) throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);

        String request = objectMapper.writeValueAsString(orderTable);
        return put(OrderTable.class, request, API_TABLES + "/" + orderTableId + "/empty");
    }

    private OrderTable changNumberOfGuests(int numberOfGuests, Long orderTableId) throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);

        String request = objectMapper.writeValueAsString(orderTable);
        return put(OrderTable.class, request,
                API_TABLES + "/" + orderTableId + "/number-of-guests");
    }
}
