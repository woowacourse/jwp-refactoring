package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @MockBean
    private OrderDao orderDao;

    @DisplayName("테이블 그룸화를 진행한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(0, true));
        final TableGroup tableGroup = new TableGroup(
                List.of(new OrderTable(orderTable1.getId()), new OrderTable(orderTable2.getId())));

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup).usingRecursiveComparison()
                .ignoringFields("id", "orderTables")
                .isEqualTo(tableGroup);
    }

    @DisplayName("2개 미만의 테이블은 그룹화할 수 없다.")
    @Test
    void create_throwException_ifOrderTableSizeUnderTwo() {
        // given
        final OrderTable orderTable = orderTableDao.save(new OrderTable(0, true));
        final TableGroup tableGroup = new TableGroup(List.of(new OrderTable(orderTable.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class).
                hasMessage("2개 이상의 테이블만 그룹화가 가능합니다.");
    }

    @DisplayName("존재하지 않는 테이블은 그룹화할 수 없다.")
    @Test
    void create_throwException_ifTableNotExist() {
        // given
        final Long invalidTableId = 999L;
        final OrderTable orderTable = orderTableDao.save(new OrderTable(0, true));
        final TableGroup tableGroup = new TableGroup(
                List.of(new OrderTable(orderTable.getId()), new OrderTable(invalidTableId)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class).
                hasMessage("존재하지 않는 테이블은 그룹화할 수 없습니다.");
    }

    @DisplayName("이미 그룹화된 테이블은 그룹화할 수 없다.")
    @Test
    void create_throwException_ifTableAlreadyGroup() {
        // given
        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(savedTableGroup.getId(), 0, true));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(0, true));
        final TableGroup tableGroup = new TableGroup(
                List.of(new OrderTable(orderTable.getId()), new OrderTable(orderTable2.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class).
                hasMessage("이미 사용 중이거나 그룹화된 테이블은 사용할 수 없습니다.");
    }

    @DisplayName("이미 사용 중인 테이블은 그룹화할 수 없다.")
    @Test
    void create_throwException_ifTableNotEmpty() {
        // given
        final OrderTable orderTable = orderTableDao.save(new OrderTable(4, false));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(0, true));
        final TableGroup tableGroup = new TableGroup(
                List.of(new OrderTable(orderTable.getId()), new OrderTable(orderTable2.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class).
                hasMessage("이미 사용 중이거나 그룹화된 테이블은 사용할 수 없습니다.");
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        // given
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(0, true));
        final TableGroup tableGroup = new TableGroup(
                List.of(new OrderTable(orderTable1.getId()), new OrderTable(orderTable2.getId())));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when, then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("이미 주문이 진행 중인 상태에서 그룹화를 제거할 수 없다.")
    @Test
    void ungroup_throwException_ifOrderAlreadyOngoing() {
        // given
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(true);
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(0, true));
        final TableGroup tableGroup = new TableGroup(
                List.of(new OrderTable(orderTable1.getId()), new OrderTable(orderTable2.getId())));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 주문이 진행 중입니다.");
    }
}
