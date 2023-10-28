package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.order.OrderTable;
import kitchenpos.order.OrderTableRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderCreateRequest.OrderLineRequest;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest.OrderTableRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문_테이블_리스트는_빈값일_수_없습니다() {
        TableGroupCreateRequest request = new TableGroupCreateRequest(null);
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어 있거나 2개 미만일 수 없습니다.");
    }

    @Test
    void 주문_테이블_리스트_개수는_1개_이하일_수_없습니다() {
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(new OrderTableRequest(1L)));
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어 있거나 2개 미만일 수 없습니다.");
    }

    @Test
    void 주문_테이블이_존재하지_않는_경우_예외를_반환한다() {
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                new OrderTableRequest(0L),
                new OrderTableRequest(1L)
        ));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    void 저장된_주문_테이블은_테이블_그룹이_존재할_수_없다() {
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                new OrderTableRequest(9L),
                new OrderTableRequest(10L)
        ));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹이 존재하는 경우 테이블 상태를 수정할 수 없습니다.");
    }

    @Test
    void 단체_테이블_생성할_수_있다() {
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                new OrderTableRequest(1L),
                new OrderTableRequest(2L)
        ));

        tableGroupService.create(request);

        OrderTable orderTable1 = orderTableRepository.findById(1L).orElseThrow();
        OrderTable orderTable2 = orderTableRepository.findById(2L).orElseThrow();

        assertSoftly(softly -> {
            softly.assertThat(orderTable1.isEmpty()).isFalse();
            softly.assertThat(orderTable1.getTableGroupId()).isNotNull();
            softly.assertThat(orderTable2.isEmpty()).isFalse();
            softly.assertThat(orderTable2.getTableGroupId()).isNotNull();
        });
    }

    @Test
    void COOKING_MEAL_상태일때는_삭제할_수_없다() {
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                new OrderTableRequest(1L),
                new OrderTableRequest(2L)
        ));

        TableGroupResponse tableGroup = tableGroupService.create(request);
        orderService.create(new OrderCreateRequest(
                tableGroup.getId(),
                List.of(new OrderLineRequest(1L, 1))
        ));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 완료 상태일때만 테이블 수정 가능합니다.");
    }

    @Test
    void 단체_테이블_삭제할_수_있다() {
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                new OrderTableRequest(1L),
                new OrderTableRequest(2L)
        ));

        TableGroupResponse tableGroup = tableGroupService.create(request);

        tableGroupService.ungroup(tableGroup.getId());

        OrderTable orderTable1 = orderTableRepository.findById(1L).orElseThrow();
        OrderTable orderTable2 = orderTableRepository.findById(2L).orElseThrow();

        assertSoftly(softly -> {
            softly.assertThat(orderTable1.isEmpty()).isFalse();
            softly.assertThat(orderTable1.getTableGroupId()).isNull();
            softly.assertThat(orderTable2.isEmpty()).isFalse();
            softly.assertThat(orderTable2.getTableGroupId()).isNull();
        });
    }
}
