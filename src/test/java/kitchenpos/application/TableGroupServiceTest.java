package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("테이블 그룹에 할당된 모든 주문 테이블을 해제한다.")
    void ungroupTest() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTableDao.findById(1L).get(), orderTableDao.findById(2L).get()));
        tableGroup.setCreatedDate(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        final List<OrderTable> actual = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());

        assertTrue(actual.isEmpty());
    }

    @Nested
    @DisplayName("테이블 그룹 생성 테스트")
    class CreateTableGroupTest {

        @Test
        @DisplayName("2개의 orderTables을 갖는 테이블 그룹을 생성한다.")
        void createTest() {
            // given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTableDao.findById(1L).get(), orderTableDao.findById(2L).get()));

            // when
            final TableGroup expect = tableGroupService.create(tableGroup);

            // then
            final TableGroup actual = tableGroupDao.findById(expect.getId()).get();

            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "orderTables")
                    .isEqualTo(expect);
        }

        @Test
        @DisplayName("빈 orderTables을 갖는 테이블 그룹을 생성하면 IllegalArgumentException이 발생한다.")
        void createWithEmptyOrderTablesTest() {
            // given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of());

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroup));
        }

        @Test
        @DisplayName("저장하려는 orderTables 수와 저장된 orderTables 수가 동일하지 않으면 IllegalArgumentException이 발생한다.")
        void createTableWithNotSavedOrderTable() {
            // given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(new OrderTable(), new OrderTable()));

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableGroupService.create(tableGroup));
        }
    }
}
