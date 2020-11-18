package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@Sql("/truncate.sql")
@DataJpaTest
public class TableGroupRepositoryTest {
    private static final Long 테이블_ID_1 = 1L;
    private static final Long 테이블_ID_2 = 2L;
    private static final Integer 테이블_사람_1명 = 1;
    private static final Integer 테이블_사람_2명 = 2;
    private static final Boolean 테이블_비어있음 = true;
    private static final Boolean 테이블_비어있지않음 = false;
    private static final Long 테이블_그룹_ID_1 = 1L;
    private static final LocalDateTime 테이블_그룹_생성시간 = LocalDateTime.now();

    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("TableGroup을 DB에 저장할 경우, 올바르게 수행된다.")
    @Test
    void saveTest() {
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(테이블_사람_1명, 테이블_비어있음),
                new OrderTable(테이블_사람_1명, 테이블_비어있음)
        );
        List<OrderTable> savedOrderTables = orderTableRepository.saveAll(orderTables);
        TableGroup tableGroup = new TableGroup(savedOrderTables);

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        Long size = tableGroupRepository.count();

        assertThat(size).isEqualTo(1L);
        assertThat(savedTableGroup.getId()).isEqualTo(1L);
        assertThat(savedTableGroup.getOrderTables())
                .hasSize(2)
                .extracting("id")
                .containsOnly(테이블_ID_1, 테이블_ID_2);
    }

    @DisplayName("TableGroup을 DB에서 조회할 경우, 올바르게 수행된다.")
    @Test
    void findByIdTest() {
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(테이블_사람_1명, 테이블_비어있음),
                new OrderTable(테이블_사람_1명, 테이블_비어있음)
        );
        List<OrderTable> savedOrderTables = orderTableRepository.saveAll(orderTables);
        TableGroup tableGroup = new TableGroup(savedOrderTables);

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        TableGroup foundTableGroup = tableGroupRepository.findById(savedTableGroup.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(foundTableGroup.getId()).isEqualTo(savedTableGroup.getId());
        assertThat(foundTableGroup.getOrderTables())
                .hasSize(2)
                .extracting("id")
                .containsOnly(테이블_ID_1, 테이블_ID_2);
    }

    @DisplayName("TableGroup을 DB에서 삭제할 경우, 올바르게 수행된다.")
    @Test
    void deleteTest() {
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(테이블_사람_1명, 테이블_비어있음),
                new OrderTable(테이블_사람_1명, 테이블_비어있음)
        );
        List<OrderTable> savedOrderTables = orderTableRepository.saveAll(orderTables);
        TableGroup tableGroup = new TableGroup(savedOrderTables);

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        assertThat(tableGroupRepository.count()).isEqualTo(1L);

        tableGroupRepository.delete(tableGroup);
        assertThat(tableGroupRepository.count()).isEqualTo(0L);
    }
}
