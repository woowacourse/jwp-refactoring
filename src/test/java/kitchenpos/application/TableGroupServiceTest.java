package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.ServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

class TableGroupServiceTest extends ServiceIntegrationTest {

  @Autowired
  private TableGroupService tableGroupService;

  @Autowired
  private OrderTableDao orderTableDao;

  @Test
  @DisplayName("create() : 테이블 그룹을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(List.of(336L, 337L));
    final TableGroup tableGroup = new TableGroup();
    tableGroup.setOrderTables(orderTables);

    //when
    final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

    //then
    assertAll(
        () -> assertNotNull(savedTableGroup.getId()),
        () -> assertNotNull(savedTableGroup.getCreatedDate())
    );
  }

  @Test
  @DisplayName("create() : 주문 테이블이 2개 미만일 때 테이블 그룹을 생성할 시 IllegalArgumentException가 발생할 수 있다.")
  void test_create_IllegalArgumentException() throws Exception {
    //given
    final OrderTable orderTable = orderTableDao.findById(335L).get();
    final TableGroup tableGroup = new TableGroup();
    tableGroup.setOrderTables(List.of(orderTable));

    //when & then
    assertThatThrownBy(() -> tableGroupService.create(tableGroup))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("create() : 해당 주문 테이블이 비어있지 않거나 이미 다른 그룹에 속해있다면 IllegalArgumentException가 발생할 수 있다.")
  void test_create_IllegalArgumentException2() throws Exception {
    //given
    //given
    final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(List.of(335L, 336L, 337L));
    final TableGroup tableGroup = new TableGroup();
    tableGroup.setOrderTables(orderTables);

    //when & then
    assertThatThrownBy(() -> tableGroupService.create(tableGroup))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("ungroup() : 주문 테이블을 주문 그룹으로 분리할 수 있다.")
  void test_ungroup() throws Exception {
    //given
    final long tableGroupId = 333L;

    assertThat(orderTableDao.findAllByTableGroupId(tableGroupId)).isNotEmpty();

    //when
    tableGroupService.ungroup(tableGroupId);

    //then
    assertThat(orderTableDao.findAllByTableGroupId(tableGroupId)).isEmpty();
  }
}
