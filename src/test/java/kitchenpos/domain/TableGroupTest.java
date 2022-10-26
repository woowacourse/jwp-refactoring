package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    @DisplayName("아이디를 설정한다")
    void setId() {
        // given
        TableGroup tableGroup = new TableGroup();
        Long id = 999L;

        // when
        tableGroup.setId(id);

        // then
        assertThat(tableGroup.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("생성 시간을 설정한다")
    void setCreatedDate() {
        // given
        TableGroup tableGroup = new TableGroup();
        LocalDateTime createdDate = LocalDateTime.now();

        // when
        tableGroup.setCreatedDate(createdDate);

        // then
        assertThat(tableGroup.getCreatedDate()).isEqualTo(createdDate);
    }

    @Test
    @DisplayName("주문 테이블 목록을 설정한다")
    void setOrderTables() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        // when
        tableGroup.setOrderTables(orderTables);

        // then
        assertAll(
            () -> assertThat(tableGroup.getOrderTables()).hasSize(2),
            () -> assertThat(tableGroup.getOrderTables()).containsExactly(orderTable1, orderTable2)
        );
    }
}
