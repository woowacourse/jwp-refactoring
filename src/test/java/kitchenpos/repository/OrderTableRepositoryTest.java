package kitchenpos.repository;

import static kitchenpos.fixture.DomainFixture.createOrderTable;
import static kitchenpos.fixture.DomainFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        tableGroup = tableGroupRepository.save(createTableGroup());
        orderTable1 = orderTableRepository.save(createOrderTable(tableGroup, false));
        orderTable2 = orderTableRepository.save(createOrderTable(tableGroup, false));
    }

    @Test
    void 주문테이블_아이디에_해당하는_모든_요소를_찾는다() {
        assertThat(orderTableRepository.findAllByIdIn(
                List.of(orderTable1.getId(), orderTable2.getId())
        )).hasSize(2);
    }

    @Test
    void 테이블그룹_아이디에_해당하는_모든_요소를_찾는다() {
        assertThat(orderTableRepository.findAllByTableGroupId(tableGroup.getId())).hasSize(2);
    }
}
