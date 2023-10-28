package kitchenpos.application;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.application.dto.OrderTableId;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.ordertablegroup.application.OrderTableGroupService;
import kitchenpos.ordertablegroup.application.OrderTableGroupValidator;
import kitchenpos.ordertablegroup.application.dto.OrderTableGroupCreateRequest;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import kitchenpos.ordertablegroup.domain.repository.OrderTableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderOrderTableGroupServiceTest {
    @InjectMocks
    private OrderTableGroupService orderTableGroupService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderTableGroupRepository orderTableGroupRepository;
    @Mock
    private OrderTableGroupValidator orderTableGroupValidator;

    @Test
    @DisplayName("테이블 그룹 생성")
    void createTableGroup_ValidRequest_ShouldCreateTableGroup() {
        //given
        OrderTableGroupCreateRequest request = new OrderTableGroupCreateRequest(List.of(new OrderTableId(1L), new OrderTableId(2L)));

        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(orderTable1, orderTable2));

        when(orderTableGroupRepository.save(any(OrderTableGroup.class)))
                .thenReturn(new OrderTableGroup(1L, LocalDateTime.now(), null));
        doNothing().when(orderTableGroupValidator).validate(anyList(), anyList());

        //when
        Long createdTableGroupId = orderTableGroupService.create(request);

        //then
        assertNotNull(createdTableGroupId);
        verify(orderTableGroupRepository, times(1)).save(any(OrderTableGroup.class));
    }
}
