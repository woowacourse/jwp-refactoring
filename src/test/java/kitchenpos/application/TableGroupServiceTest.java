package kitchenpos.application;

import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableId;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("테이블 그룹 생성")
    void createTableGroup_ValidRequest_ShouldCreateTableGroup() {
        //given
        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(new OrderTableId(1L), new OrderTableId(2L)));

        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(orderTable1, orderTable2));

        when(tableGroupRepository.save(any(TableGroup.class)))
                .thenReturn(new TableGroup(1L, LocalDateTime.now(), null));

        //when
        Long createdTableGroupId = tableGroupService.create(request);

        //then
        assertNotNull(createdTableGroupId);
        verify(tableGroupRepository, times(1)).save(any(TableGroup.class));
    }
}
