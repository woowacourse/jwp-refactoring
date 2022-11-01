package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    private final List<OrderTable> orderTables = List.of(new OrderTable(0, true), new OrderTable(1, true));


    @Test
    void 테이블_집합_생성() {
        Assertions.assertDoesNotThrow(() -> new TableGroup(LocalDateTime.now(), orderTables));
    }
}
