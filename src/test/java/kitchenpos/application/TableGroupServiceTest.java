package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderTableDao orderTableDao;

    @DisplayName("단체 지정을 추가하면 특정 테이블이 단체에 속한다.")
    @Test
    void create() {
        OrderTable 테이블_참_1 = orderTableDao.save(Fixtures.테이블_참_1());
        OrderTable 테이블_참_2 = orderTableDao.save(Fixtures.테이블_참_2());
        TableGroup 테이블그룹 = Fixtures.테이블그룹(List.of(테이블_참_1, 테이블_참_2));

        TableGroup saved = tableGroupService.create(테이블그룹);

        assertThat(orderTableDao.findAllByTableGroupId(saved.getId()))
                .hasSize(2);
    }

    @DisplayName("단체지정을 삭제하면 소속된 테이블들이 단체에서 빠진다.")
    @Test
    void ungroup() {
        OrderTable 테이블_참_1 = orderTableDao.save(Fixtures.테이블_참_1());
        OrderTable 테이블_참_2 = orderTableDao.save(Fixtures.테이블_참_2());
        TableGroup 테이블그룹 = Fixtures.테이블그룹(List.of(테이블_참_1, 테이블_참_2));
        TableGroup saved = tableGroupService.create(테이블그룹);

        tableGroupService.ungroup(saved.getId());

        assertThat(orderTableDao.findAllByTableGroupId(saved.getId()))
                .hasSize(0);
    }
}
