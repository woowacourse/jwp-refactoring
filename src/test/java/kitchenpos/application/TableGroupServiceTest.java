package kitchenpos.application;

import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableId;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    private TableGroupCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = makeTableGroupCreate();
    }

    @Test
    void 테이블_그룹을_생성한다() {
        assertDoesNotThrow(
                () -> tableGroupService.create(createRequest)
        );
    }

    @Test
    void 주문테이블이_주문가능_상태이면_예외발생() {
        tableService.changeEmpty(1L, new OrderTableChangeEmptyRequest(false));
        assertThatThrownBy(
                () -> tableGroupService.create(createRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 통합_계산_해제() {
        TableGroupResponse response = tableGroupService.create(createRequest);
        tableGroupService.ungroup(response.getId());

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(List.of(1L, 2L));
        for (OrderTable orderTable : orderTables) {
            assertAll(
                    () -> assertThat(orderTable.getTableGroupId()).isNull(),
                    () -> assertThat(orderTable.isEmpty()).isFalse()
            );
        }

    }

    private TableGroupCreateRequest makeTableGroupCreate() {
        return new TableGroupCreateRequest(List.of(
                new OrderTableId(1L),
                new OrderTableId(2L)));
    }
}
