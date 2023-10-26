package kitchenpos.table_group.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.infrastructure.persistence.OrderTableRepositoryImpl;
import kitchenpos.support.JdbcTestHelper;
import kitchenpos.table_group.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupRepositoryImplTest extends JdbcTestHelper {

  @Autowired
  private TableGroupRepositoryImpl tableGroupRepository;

  @Autowired
  private OrderTableRepositoryImpl orderTableRepository;

  private OrderTable orderTable1, orderTable, orderTable3;

  @BeforeEach
  void setUp() {
    orderTable1 = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );
    orderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );
    orderTable3 = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );
  }

  @Test
  @DisplayName("save() : 테이블 그룹을 저장할 수 있다.")
  void test_save() throws Exception {
    //given
    final TableGroup tableGroup = TableGroupFixture.createTableGroup(
        List.of(orderTable1, orderTable)
    );

    final List<Long> beforeSavedTableGroupId = tableGroup.getOrderTables()
        .stream()
        .map(OrderTable::getTableGroupId)
        .collect(Collectors.toList());

    assertThat(beforeSavedTableGroupId)
        .allMatch(Objects::isNull);

    //when
    final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

    //then
    final List<Long> afterSavedTableGroupId = savedTableGroup.getOrderTables()
        .stream()
        .map(OrderTable::getTableGroupId)
        .collect(Collectors.toList());

    assertAll(
        () -> assertNotNull(savedTableGroup.getId()),
        () -> Assertions.assertThat(afterSavedTableGroupId)
            .allMatch(it -> it.equals(savedTableGroup.getId()))
    );
  }

  @Test
  @DisplayName("findById() : id를 통해 테이블 그룹을 조회할 수 있다.")
  void test_findById() throws Exception {
    //given
    final TableGroup tableGroup = tableGroupRepository.save(
        TableGroupFixture.createTableGroup(
            List.of(orderTable1, orderTable)
        )
    );

    //when
    final Optional<TableGroup> savedTableGroup = tableGroupRepository.findById(tableGroup.getId());

    //then
    assertAll(
        () -> assertTrue(savedTableGroup.isPresent()),
        () -> assertThat(savedTableGroup.get())
            .usingRecursiveComparison()
            .isEqualTo(tableGroup)
    );
  }

  @Test
  @DisplayName("findAll() : 모든 테이블 그룹을 조회할 수 있다.")
  void test_findAll() throws Exception {
    //given
    final TableGroup tableGroup1 = tableGroupRepository.save(
        TableGroupFixture.createTableGroup(
            List.of(orderTable1, orderTable)
        )
    );
    final TableGroup tableGroup = tableGroupRepository.save(
        TableGroupFixture.createTableGroup(
            List.of(orderTable3)
        )
    );

    //when
    final List<TableGroup> tableGroups = tableGroupRepository.findAll();

    //then
    assertThat(tableGroups)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(List.of(tableGroup1, tableGroup));
  }
}
