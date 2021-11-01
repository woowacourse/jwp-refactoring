package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.fixtures.TableFixtures;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    void 단체_지정을_생성한다() {
        List<OrderTable> orderTables = TableFixtures.createOrderTables(true);

        assertDoesNotThrow(() -> new TableGroup(orderTables));
    }

    @Test
    void 생성_시_지정_할_주문_테이블들이_2개_미만_이면_예외를_반환한다() {
        List<OrderTable> notEnoughTables = Collections.singletonList(TableFixtures.createOrderTable(true));

        Exception exception = assertThrows(IllegalStateException.class, () -> new TableGroup(notEnoughTables));
        assertThat(exception.getMessage()).isEqualTo("단체 지정할 테이블들이 부족합니다.");
    }

    @Test
    void 생성_시_주문_테이블들이_비어있으면_예외를_반환한다() {
        List<OrderTable> emptyTables = new ArrayList<>();
        emptyTables.add(TableFixtures.createOrderTable(false));
        emptyTables.add(TableFixtures.createOrderTable(false));

        Exception exception = assertThrows(IllegalStateException.class, () -> new TableGroup(emptyTables));
        assertThat(exception.getMessage()).isEqualTo("빈 테이블이 아닙니다.");
    }

    @Test
    void 생성_시_주문_테이블들이_단체_지정_되어있으면_예외를_반환한다() {
        List<OrderTable> groupedTables = new ArrayList<>();
        groupedTables.add(TableFixtures.createGroupedOrderTable(true));
        groupedTables.add(TableFixtures.createGroupedOrderTable(true));

        Exception exception = assertThrows(IllegalStateException.class, () -> new TableGroup(groupedTables));
        assertThat(exception.getMessage()).isEqualTo("이미 단체 지정된 테이블입니다.");
    }
}
