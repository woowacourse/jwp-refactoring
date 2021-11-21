package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import kitchenpos.factory.TableGroupFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = TableGroupFactory.builder()
            .id(1L)
            .orderTables()
            .build();
    }

    @DisplayName("TableGroup 객체를 세팅한다")
    @Test
    void createWith() {
        // given
        OrderTables orderTables = new OrderTables();

        // when
        tableGroup.createWith(orderTables);

        // then
        assertThat(tableGroup.getCreatedDate()).isNotNull();
        assertThat(tableGroup.getOrderTables()).isEqualTo(orderTables);
    }
}
