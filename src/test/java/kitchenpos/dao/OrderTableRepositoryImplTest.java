package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.support.JdbcTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderTableRepositoryImplTest extends JdbcTestHelper {

  @Autowired
  private OrderTableRepositoryImpl orderTableRepository;

  @Autowired
  private TableGroupRepositoryImpl tableGroupRepository;

  @Test
  @DisplayName("save() : 주문 테이블을 저장할 수 있다.")
  void test_save() throws Exception {
    //given
    final OrderTable orderTable = OrderTableFixture.createEmptySingleOrderTable();

    //when
    final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

    //then
    assertAll(
        () -> assertNotNull(savedOrderTable.getId()),
        () -> assertThat(savedOrderTable)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(orderTable)
    );
  }

  @Test
  @DisplayName("findById() : id를 통해 주문 테이블을 조회할 수 있다.")
  void test_findById() throws Exception {
    //given
    final OrderTable orderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );

    //when
    final Optional<OrderTable> savedOrderTable = orderTableRepository.findById(orderTable.getId());

    //then
    assertAll(
        () -> assertTrue(savedOrderTable.isPresent()),
        () -> assertThat(savedOrderTable.get())
            .usingRecursiveComparison()
            .isEqualTo(orderTable)
    );
  }

  @Test
  @DisplayName("findAll() : 모든 주문 테이블을 조회할 수 있다.")
  void test_findAll() throws Exception {
    //given
    final OrderTable orderTable1 = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );
    final OrderTable orderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );

    //when
    final List<OrderTable> orderTables = orderTableRepository.findAll();

    //then
    assertThat(orderTables)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(List.of(orderTable1, orderTable));
  }

  @Test
  @DisplayName("findAllByIdIn() : ids를 통해 주문 테이블들을 조회할 수 있다.")
  void test_findAllByIdIn() throws Exception {
    final OrderTable orderTable1 = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );
    final OrderTable orderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );
    final long notExistedId = 3333L;

    //when
    final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(
        List.of(orderTable1.getId(), orderTable.getId(), notExistedId)
    );

    //then
    assertThat(orderTables)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(List.of(orderTable1, orderTable));
  }

  @Test
  @DisplayName("findAllByTableGroupId() : 같은 테이블 그룹에 속한 주문 테이블을 조회할 수 있다.")
  void test_findAllByTableGroupId() throws Exception {
    final OrderTable orderTable1 = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );
    final OrderTable orderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );
    final OrderTable orderTable3 = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );
    final TableGroup tableGroup1 = tableGroupRepository.save(TableGroupFixture.createTableGroup(List.of(orderTable1,
        orderTable)));
    final TableGroup tableGroup = tableGroupRepository.save(TableGroupFixture.createTableGroup(List.of(orderTable3)));

    //when
    final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

    //then
    assertThat(orderTables)
        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("tableGroupId")
        .containsExactlyInAnyOrderElementsOf(List.of(orderTable3));
  }
}
