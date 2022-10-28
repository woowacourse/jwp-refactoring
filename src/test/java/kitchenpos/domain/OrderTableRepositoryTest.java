package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("id로 존재하는 주문 테이블을 모두 조회한다.")
    void findAllByIn() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(2, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, true));
        orderTableRepository.save(new OrderTable(2, true));

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(
                List.of(orderTable1.getId(), orderTable2.getId()));

        assertThat(orderTables).hasSize(2);
    }
}
