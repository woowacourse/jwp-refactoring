package kitchenpos.application;

import kitchenpos.exception.NonExistentException;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("TableGroup 서비스 테스트")
class TableGroupServiceTest extends ServiceTest {
    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderTableService orderTableService;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @DisplayName("테이블그룹을 해제한다. - 실패, TableGroup이 존재하지 않음.")
    @Test
    void ungroupFailedNotFound() {
        // given
        Long tableGroupId = -100L;
        given(tableGroupRepository.existsById(tableGroupId)).willReturn(false);

        // when -  then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(NonExistentException.class);
        then(tableGroupRepository).should(times(1))
                .existsById(tableGroupId);
        then(orderTableService).should(never())
                .ungroup(tableGroupId);
    }
}
