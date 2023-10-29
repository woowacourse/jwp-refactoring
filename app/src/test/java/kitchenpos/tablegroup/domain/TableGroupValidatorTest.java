package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
class TableGroupValidatorTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("테이블 그룹을 생성할 때 주문 테이블이 비어있지 않은 경우 예외가 발생한다.")
    void throwsExceptionWhenOrderTableIsNotEmpty() {
        // given
        final TableGroupValidator tableGroupValidator = new TableGroupValidator(orderTableRepository);
        final OrderTable orderTableA = new OrderTable(null, 2, false);
        final OrderTable orderTableB = new OrderTable(null, 3, false);
        orderTableRepository.save(orderTableA);
        orderTableRepository.save(orderTableB);

        final List<Long> orderTableIds = List.of(orderTableA.getId(), orderTableB.getId());

        // when, then
        assertThatThrownBy(() -> tableGroupValidator.validate(orderTableIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 비어있어야 합니다.");
    }
}
