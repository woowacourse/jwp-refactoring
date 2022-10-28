package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    OrderTable 빈테이블_1;
    OrderTable 빈테이블_2;


    void init() {
        빈테이블_1 = orderTableDao.save(빈테이블());
        빈테이블_2 = orderTableDao.save(빈테이블());
    }

    @DisplayName("단체 지정을 추가하면 특정 테이블이 단체에 속한다.")
    @Test
    void create() {
        init();
        Long 테이블그룹Id = tableGroupService.create(테이블그룹요청(List.of(
                주문요청변환(빈테이블_1),
                주문요청변환(빈테이블_2)
        ))).getId();

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
        OrderTable 테이블_1 = orderTableDao.save(테이블_1());
        TableGroupRequest 테이블그룹 = 테이블그룹요청(List.of(주문요청변환(테이블_1), 주문요청변환(빈테이블_2)));

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 모든 테이블들은 비어있어야 한다.");
    }

    @DisplayName("등록되는 모든 테이블들은 존재해야 한다.")
    @Test
    void create_noTable() {
        init();
        long 존재하지_않는_테이블_id = 100L;
        TableGroupRequest 테이블그룹 = 테이블그룹요청(List.of(주문요청변환(테이블_ofId(존재하지_않는_테이블_id)), 주문요청변환(빈테이블_2)));

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 모든 테이블들은 존재해야 한다.");
    }

    @DisplayName("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.")
    @Test
    void create_alreadyInGroup() {
        init();
        TableGroupResponse 테이블그룹1 = tableGroupService.create(테이블그룹요청(List.of(
                주문요청변환(빈테이블_1),
                주문요청변환(빈테이블_2)
        )));
        테이블_단체_지정(테이블그룹1.getId(), 빈테이블_1());

        TableGroupRequest 테이블그룹2 = 테이블그룹요청2(List.of(new OrderTableRequest(1L),
                new OrderTableRequest(2L)));
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.");
    }

    private OrderTable 테이블_단체_지정(Long id, OrderTable 테이블) {
        테이블.updateTableGroupId(id);
        return orderTableDao.save(테이블);
    }

    @DisplayName("단체지정을 삭제하면 소속된 테이블들이 단체에서 빠진다.")
    @Test
    void ungroup() {
        init();
        Long 테이블그룹Id = tableGroupService.create(테이블그룹요청(List.of(
                주문요청변환(빈테이블_1),
                주문요청변환(빈테이블_2)
        ))).getId();

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

        Long 테이블그룹Id = tableGroupService.create(테이블그룹요청(List.of(
                new OrderTableRequest(빈테이블_1.getId()),
                new OrderTableRequest(빈테이블_2.getId())
        ))).getId();

        테이블의_그룹변경(테이블그룹Id, 테이블_1());
        orderService.create(주문요청_변환(주문_테이블1()));

        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹Id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.");
    }

    private void 테이블의_그룹변경(Long id, OrderTable 테이블_1) {
        테이블_1.updateTableGroupId(id);
        orderTableDao.save(테이블_1);
    }
}
