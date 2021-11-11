package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TableGroupTest {

    @DisplayName("객체 생성: 객체가 정상적으로 생성된다.")
    @Test
    void create() {
        // given
        Long id = 1L;
        LocalDateTime createdDate = LocalDateTime.now();
        List<OrderTable> orderTables = new ArrayList<>();

        // when
        TableGroup tableGroup = new TableGroup(id, createdDate, orderTables);

        // then
        assertThat(tableGroup.getId()).isEqualTo(id);
        assertThat(tableGroup.getCreatedDate()).isEqualTo(createdDate);
        assertThat(tableGroup.getOrderTables()).isEqualTo(orderTables);
    }

    @DisplayName("테이블 그룹화 : 테이블을 그룹화 시킵니다. ")
    @Test
    void grouping() {
        // given
        Long id = 1L;
        LocalDateTime createdDate = LocalDateTime.now();
        TableGroup tableGroup = new TableGroup(id, createdDate, null);

        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1L, tableGroup, 4, false),
                new OrderTable(2L, tableGroup, 4, false)
        );

        // when
        tableGroup.grouping(orderTables);
        int expectedSize = 2;

        // then
        assertThat(tableGroup.getOrderTables()).hasSize(expectedSize);
    }
}