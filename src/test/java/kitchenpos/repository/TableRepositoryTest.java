package kitchenpos.repository;

import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("TableRepository 테스트")
class TableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = OrderTableFixture.nullTableGroup();
    }

    @DisplayName("테이블 추가")
    @Test
    void create() {
        OrderTable savedTable = orderTableRepository.save(orderTable);

        assertThat(savedTable.getId()).isNotNull();
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        OrderTable orderTable1 = new OrderTable(3, false);
        OrderTable orderTable2 = new OrderTable(3, false);
        OrderTable orderTable3 = new OrderTable(3, false);
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);
        orderTableRepository.save(orderTable3);

        List<OrderTable> tables = orderTableRepository.findAll();

        assertThat(tables).hasSize(3);
    }
}
