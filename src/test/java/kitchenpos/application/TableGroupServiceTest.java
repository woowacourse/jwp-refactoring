package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import kitchenpos.dto.request.tablegroup.CreateTableGroupRequest;
import kitchenpos.dto.request.tablegroup.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("테이블 그룹 테스트")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void create()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        final OrderTableRequest orderTableRequest1 = getRequest(OrderTableRequest.class, 1L, 1L);
        final OrderTableRequest orderTableRequest2 = getRequest(OrderTableRequest.class, 2L, 1L);
        final CreateTableGroupRequest request = getRequest(
                CreateTableGroupRequest.class, 1L,
                List.of(orderTableRequest1, orderTableRequest2)
        );

        // when
        final TableGroupResponse actual = tableGroupService.create(request);

        // then
        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @DisplayName("2개 미만의 테이블 그룹 생성에 실패한다")
    @Test
    void create_Fail()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        final OrderTableRequest orderTableRequest = getRequest(OrderTableRequest.class, 1L, 1L);
        final CreateTableGroupRequest tableGroupRequest = getRequest(CreateTableGroupRequest.class, 1L,
                List.of(orderTableRequest));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어 있지 않을 경우 그룹생성에 실패한다")
    @Test
    void create_FailEmptyTable()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //
        final OrderTableRequest orderTableRequest1 = getRequest(OrderTableRequest.class, 1L, 1L);
        final OrderTableRequest orderTableRequest2 = getRequest(OrderTableRequest.class, 5L, 1L);
        final CreateTableGroupRequest tableGroup = getRequest(
                CreateTableGroupRequest.class, 1L,
                List.of(orderTableRequest1, orderTableRequest2)
        );
        // when
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void ungroup()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        final OrderTableRequest orderTableRequest1 = getRequest(OrderTableRequest.class, 1L, 1L);
        final OrderTableRequest orderTableRequest2 = getRequest(OrderTableRequest.class, 2L, 1L);
        final CreateTableGroupRequest tableGroup = getRequest(
                CreateTableGroupRequest.class, 1L,
                List.of(orderTableRequest1, orderTableRequest2)
        );
        final Long tableGroupId = tableGroupService.create(tableGroup).getId();

        // when
        tableGroupService.ungroup(tableGroupId);
        final List<OrderTableResponse> actual = tableService.list();

        // then
        assertSoftly(
                softly -> {
                    actual.stream()
                            .filter(orderTable -> orderTable.getId().equals(1L) || orderTable.getId().equals(2L))
                            .forEach(orderTable -> softly.assertThat(orderTable.getTableGroupId()).isNull());
                }
        );
    }

    @DisplayName("조리 중이거나 먹는 중인 테이블이면 그룹 해제에 실패한다")
    @ParameterizedTest(name = "{0} 중인 테이블 상태 변경시 실패한다")
    @MethodSource("statusAndIdProvider")
    void ungroup_FailNonExistTable(final String name, final Long id, final Class exception)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        final OrderTableRequest orderTableRequest1 = getRequest(OrderTableRequest.class, 1L, 1L);
        final OrderTableRequest orderTableRequest2 = getRequest(OrderTableRequest.class, id, 1L);
        final CreateTableGroupRequest request = getRequest(
                CreateTableGroupRequest.class, 1L,
                List.of(orderTableRequest1, orderTableRequest2)
        );
        final TableGroupResponse response = tableGroupService.create(request);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(response.getId()))
                .isInstanceOf(exception);
    }
}
