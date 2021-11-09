package kitchenpos.application;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;

import static org.assertj.core.api.Assertions.assertThat;

class TableGroupServiceTest extends ServiceTest {

    private final TableGroupRequest tableGroup;

    public TableGroupServiceTest() {
        this.tableGroup = Fixtures.makeTableGroup();
    }

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("테이블 그룹 생성")
    void createTest() {

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(tableGroupDao.findAll()).contains(savedTableGroup);
    }

    @Test
    @DisplayName("테이블 등록 해제")
    void ungroupTest() {

        // given
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).isEmpty();
    }
}
