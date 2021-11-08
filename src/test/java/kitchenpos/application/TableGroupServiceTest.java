package kitchenpos.application;

import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.OrdersRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.service.ServiceTest;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import kitchenpos.ui.dto.TableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("테이블 그룹 서비스 테스트")
class TableGroupServiceTest extends ServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrdersRepository ordersRepository;

    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        TableRequest tableRequest1 = new TableRequest(1L, 3, false);
        TableRequest tableRequest2 = new TableRequest(2L, 3, false);

        tableGroupRequest = new TableGroupRequest(Arrays.asList(tableRequest1, tableRequest2));
    }

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() {
        //given
        when(tableGroupRepository.save(any())).thenReturn(TableGroupFixture.create());
        //when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
        //then
        assertThat(tableGroupResponse.getId()).isNotNull();
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        //given
        when(orderTableRepository.findAllByTableGroupId(anyLong())).thenReturn(Collections.singletonList(OrderTableFixture.create()));
        //when
        tableGroupService.ungroup(anyLong());
        //then
        verify(ordersRepository, times(1)).findAllByOrderTableId(anyLong());
    }
}