package kitchenpos.repository;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithTableGroup;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithoutId;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupWithoutId;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("findAllByIdIn 정상 작동")
    @Test
    void findAllByIdIn() {
        List<OrderTable> orderTables = Arrays.asList(
                orderTableRepository.save(createOrderTableWithoutId()),
                orderTableRepository.save(createOrderTableWithoutId()));
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        List<OrderTable> actual = orderTableRepository.findAllByIdIn(orderTableIds);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderTables);
    }

    @DisplayName("findAllByTableGroupId")
    @Test
    void findAllByTableGroupId() {
        TableGroup savedTableGroup = tableGroupRepository.save(createTableGroupWithoutId());
        List<OrderTable> orderTables = Arrays.asList(
                orderTableRepository.save(createOrderTableWithTableGroup(savedTableGroup)),
                orderTableRepository.save(createOrderTableWithTableGroup(savedTableGroup)));

        List<OrderTable> actual = orderTableRepository.findAllByTableGroupId(savedTableGroup.getId());

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderTables);
    }

    @AfterEach
    void tearDown() {
        tableGroupRepository.deleteAll();
        orderTableRepository.deleteAll();
    }
}