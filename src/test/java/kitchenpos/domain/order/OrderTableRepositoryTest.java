package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
@DataJpaTest
public class OrderTableRepositoryTest {
    private static final Integer 테이블_사람_1명 = 1;
    private static final Integer 테이블_사람_2명 = 2;
    private static final Boolean 테이블_비어있음 = true;
    private static final Boolean 테이블_비어있지않음 = false;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("OrderTable을 DB에 저장할 경우, 올바르게 수행된다.")
    @Test
    void saveTest() {
        OrderTable orderTable = new OrderTable(테이블_사람_1명, 테이블_비어있음);

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        Long size = orderTableRepository.count();

        assertThat(size).isEqualTo(1L);
        assertThat(savedOrderTable.getId()).isEqualTo(1L);
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(테이블_사람_1명);
        assertThat(savedOrderTable.getEmpty()).isEqualTo(테이블_비어있음);
    }

    @DisplayName("OrderTable의 목록 조회를 요청할 경우, 올바르게 수행된다.")
    @Test
    void findAllTest() {
        OrderTable orderTable1 = new OrderTable(테이블_사람_1명, 테이블_비어있음);
        OrderTable orderTable2 = new OrderTable(테이블_사람_2명, 테이블_비어있지않음);
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        List<OrderTable> orderTables = orderTableRepository.findAll();

        assertThat(orderTables).hasSize(2);
        assertThat(orderTables.get(0).getId()).isEqualTo(1L);
        assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(테이블_사람_1명);
        assertThat(orderTables.get(0).getEmpty()).isEqualTo(테이블_비어있음);
        assertThat(orderTables.get(1).getId()).isEqualTo(2L);
        assertThat(orderTables.get(1).getNumberOfGuests()).isEqualTo(테이블_사람_2명);
        assertThat(orderTables.get(1).getEmpty()).isEqualTo(테이블_비어있지않음);
    }
}
