package kitchenpos.application;

import static kitchenpos.Fixtures.빈테이블_1;
import static kitchenpos.Fixtures.빈테이블_2;
import static kitchenpos.Fixtures.주문_테이블1;
import static kitchenpos.Fixtures.테이블_1;
import static kitchenpos.Fixtures.테이블그룹;
import static kitchenpos.Fixtures.테이블그룹2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.dao.OrderDao;
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

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderService orderService;

    @DisplayName("단체 지정을 추가하면 특정 테이블이 단체에 속한다.")
    @Test
    void create() {
        OrderTable 빈테이블_1 = orderTableDao.save(빈테이블_1());
        OrderTable 빈테이블_2 = orderTableDao.save(빈테이블_2());

        TableGroup 테이블그룹 = tableGroupService.create(테이블그룹(List.of(빈테이블_1, 빈테이블_2)));

        assertThat(orderTableDao.findAllByTableGroupId(테이블그룹.getId()))
                .hasSize(2);
    }

    @DisplayName("등록되는 테이블 수가 2 이상이어야 한다.")
    @Test
    void create_zeroTable() {
        TableGroup 테이블그룹 = 테이블그룹(new ArrayList<>());

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 테이블 수가 2 이상이어야 한다.");
    }

    @DisplayName("등록되는 모든 테이블들은 비어있어야 한다.")
    @Test
    void create_fillTable() {
        OrderTable 빈테이블_2 = orderTableDao.save(빈테이블_2());
        OrderTable 테이블_1 = orderTableDao.save(테이블_1());
        TableGroup 테이블그룹 = 테이블그룹(List.of(테이블_1, 빈테이블_2));

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 모든 테이블들은 비어있어야 한다.");
    }

    @DisplayName("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.")
    @Test
    void create_alreadyInGroup() {
        OrderTable 빈테이블_2 = orderTableDao.save(빈테이블_2());
        TableGroup 테이블그룹1 = tableGroupService.create(테이블그룹(List.of(빈테이블_1(), 빈테이블_2)));
        OrderTable 빈테이블_1 = 빈테이블_1();
        빈테이블_1.updateTableGroupId(테이블그룹1.getId());
        OrderTable 단체지정_빈테이블_1 = orderTableDao.save(빈테이블_1);

        TableGroup 테이블그룹2 = 테이블그룹2(List.of(단체지정_빈테이블_1, 빈테이블_2));
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.");
    }

    @DisplayName("단체지정을 삭제하면 소속된 테이블들이 단체에서 빠진다.")
    @Test
    void ungroup() {
        OrderTable 빈테이블_1 = orderTableDao.save(빈테이블_1());
        OrderTable 빈테이블_2 = orderTableDao.save(빈테이블_2());
        TableGroup 테이블그룹 = tableGroupService.create(테이블그룹(List.of(빈테이블_1, 빈테이블_2)));

        tableGroupService.ungroup(테이블그룹.getId());

        assertThat(orderTableDao.findAllByTableGroupId(테이블그룹.getId()))
                .hasSize(0);
    }

    @DisplayName("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.")
    @Test
    void ungroup_noCompleteOrder() {
        OrderTable 빈테이블_1 = orderTableDao.save(빈테이블_1());
        OrderTable 빈테이블_2 = orderTableDao.save(빈테이블_2());
        TableGroup 테이블그룹 = tableGroupService.create(테이블그룹(List.of(빈테이블_1, 빈테이블_2)));

        OrderTable 테이블_1 = 테이블_1();
        테이블_1.updateTableGroupId(테이블그룹.getId());
        orderTableDao.save(테이블_1);
        orderService.create(Fixtures.주문_테이블1());

        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.");
    }
}
