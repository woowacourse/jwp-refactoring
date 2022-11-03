package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    Long 빈테이블_1_id;
    Long 빈테이블_2_id;


    void init() {
        빈테이블_1_id = tableService.create(빈테이블생성요청()).getId();
        빈테이블_2_id = tableService.create(빈테이블생성요청()).getId();
    }

    @DisplayName("단체 지정을 추가하면 특정 테이블이 단체에 속한다.")
    @Test
    void create() {
        init();
        Long 테이블그룹Id = tableGroupService.create(테이블그룹요청_id(빈테이블_1_id, 빈테이블_2_id)).getId();

        List<OrderTable> 테이블_목록 = orderTableDao.findAllByTableGroupId(테이블그룹Id);
        boolean empty = false;
        검증_필드비교_동일_목록(테이블_목록, List.of(
                new OrderTable(빈테이블_1_id, 테이블그룹Id, 0, empty),
                new OrderTable(빈테이블_2_id, 테이블그룹Id, 0, empty)
        ));
    }

    @DisplayName("등록되는 테이블 수가 2 이상이어야 한다.")
    @Test
    void create_zeroTable() {
        init();
        TableGroupRequest 테이블그룹 = 테이블그룹요청(new ArrayList<>());

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 테이블 수가 2 이상이어야 한다.");
    }

    @DisplayName("등록되는 모든 테이블들은 비어있어야 한다.")
    @Test
    void create_fillTable() {
        init();
        Long id = tableService.changeEmpty(1L, new OrderTableChangeEmptyRequest(false)).getId();
        TableGroupRequest 테이블그룹 = 테이블그룹요청_id(id, 빈테이블_2_id);

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 모든 테이블들은 비어있어야 한다.");
    }

    @DisplayName("등록되는 모든 테이블들은 존재해야 한다.")
    @Test
    void create_noTable() {
        init();
        long 존재하지_않는_테이블_id = 100L;
        TableGroupRequest 테이블그룹 = 테이블그룹요청_id(존재하지_않는_테이블_id, 빈테이블_2_id);

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("테이블은 존재해야 한다.");
    }

    @DisplayName("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.")
    @Test
    void create_alreadyInGroup() {
        init();
        TableGroupRequest tableGroupRequest = 테이블그룹요청_id(빈테이블_1_id, 빈테이블_2_id);

        tableGroupService.create(tableGroupRequest);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.");
    }

    @DisplayName("단체지정을 삭제하면 소속된 테이블들이 단체에서 빠진다.")
    @Test
    void ungroup() {
        init();
        Long 테이블그룹Id = tableGroupService.create(테이블그룹요청_id(빈테이블_1_id, 빈테이블_2_id))
                .getId();

        tableGroupService.ungroup(테이블그룹Id);

        List<OrderTable> 테이블_목록 = orderTableDao.findAllByTableGroupId(테이블그룹Id);
        assertThat(테이블_목록).hasSize(0);
    }

    @DisplayName("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.")
    @Test
    void ungroup_noCompleteOrder() {
        menuGroupDao.save(메뉴그룹_한마리메뉴());
        productDao.save(상품_후라이드());
        menuDao.save(메뉴_후라이드치킨());
        init();

        Long 테이블그룹Id = tableGroupService.create(테이블그룹요청_id(빈테이블_1_id, 빈테이블_2_id)).getId();
        orderService.create(주문요청_테이블1());

        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹Id))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessageContaining("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.");
    }
}
