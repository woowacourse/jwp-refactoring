package kitchenpos.application;

import static kitchenpos.common.OrderTableFixtures.ORDER_TABLE1_REQUEST;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블 생성 시 ID와 테이블 그룹 ID를 null로 설정하여 저장한다.")
    void create() {
        // given
        final OrderTable orderTable = ORDER_TABLE1_REQUEST();

        // when
        final OrderTable createdOrderTable = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(createdOrderTable.getId()).isNotNull();
            softly.assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            softly.assertThat(createdOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
            softly.assertThat(createdOrderTable.getTableGroupId()).isNull();
        });
    }
}
