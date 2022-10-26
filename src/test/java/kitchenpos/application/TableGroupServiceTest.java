package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    OrderTable 빈테이블_1;
    OrderTable 빈테이블_2;

    @BeforeEach
    void init() {
        빈테이블_1 = orderTableDao.save(빈테이블_1());
        빈테이블_2 = orderTableDao.save(빈테이블_2());
    }

    @DisplayName("단체 지정을 추가하면 특정 테이블이 단체에 속한다.")
    @Test
    void create() {
        Long 테이블그룹Id = tableGroupService.create(테이블그룹(List.of(빈테이블_1, 빈테이블_2))).getId();

        List<OrderTable> 테이블_목록 = orderTableDao.findAllByTableGroupId(테이블그룹Id);
        boolean 손님_착석 = false;
        검증_필드비교_동일_목록(테이블_목록, List.of(
                new OrderTable(빈테이블_1.getId(), 테이블그룹Id, 0, 손님_착석),
                new OrderTable(빈테이블_2.getId(), 테이블그룹Id, 0, 손님_착석)
        ));
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
        OrderTable 테이블_1 = orderTableDao.save(테이블_1());
        TableGroup 테이블그룹 = 테이블그룹(List.of(테이블_1, 빈테이블_2));

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 모든 테이블들은 비어있어야 한다.");
    }

    @DisplayName("등록되는 모든 테이블들은 존재해야 한다.")
    @Test
    void create_noTable() {
        long 존재하지_않는_테이블_id = 100L;
        TableGroup 테이블그룹 = 테이블그룹(List.of(테이블_1_ofId(존재하지_않는_테이블_id), 빈테이블_2));

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 모든 테이블들은 존재해야 한다.");
    }

    @DisplayName("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.")
    @Test
    void create_alreadyInGroup() {
        TableGroup 테이블그룹1 = tableGroupService.create(테이블그룹(List.of(빈테이블_1, 빈테이블_2)));
        OrderTable 단체지정_빈테이블_1 = 테이블_단체_지정(테이블그룹1, 빈테이블_1());

        TableGroup 테이블그룹2 = 테이블그룹2(List.of(단체지정_빈테이블_1, 빈테이블_2));
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.");
    }

    private OrderTable 테이블_단체_지정(TableGroup 테이블그룹, OrderTable 테이블) {
        테이블.updateTableGroupId(테이블그룹.getId());
        return orderTableDao.save(테이블);
    }

    @DisplayName("단체지정을 삭제하면 소속된 테이블들이 단체에서 빠진다.")
    @Test
    void ungroup() {
        TableGroup 테이블그룹 = tableGroupService.create(테이블그룹(List.of(빈테이블_1, 빈테이블_2)));

        tableGroupService.ungroup(테이블그룹.getId());

        List<OrderTable> 테이블_목록 = orderTableDao.findAllByTableGroupId(테이블그룹.getId());
        assertThat(테이블_목록).hasSize(0);
    }

    @DisplayName("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.")
    @Test
    void ungroup_noCompleteOrder() {
        TableGroup 테이블그룹 = tableGroupService.create(테이블그룹(List.of(빈테이블_1, 빈테이블_2)));

        테이블의_그룹변경(테이블그룹, 테이블_1());
        orderService.create(주문_테이블1());

        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.");
    }

    private void 테이블의_그룹변경(TableGroup 테이블그룹, OrderTable 테이블_1) {
        테이블_1.updateTableGroupId(테이블그룹.getId());
        orderTableDao.save(테이블_1);
    }
}
