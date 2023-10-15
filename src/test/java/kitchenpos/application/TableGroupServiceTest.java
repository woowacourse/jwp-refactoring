package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup();
        orderTable = new OrderTable(1L, null, 4, true);
        tableGroup.setOrderTables(List.of(orderTable));
    }

    @Test
    @DisplayName("테이블 그룹을 생성하는 테스트")
    void create() {
        // Given
        given(orderTableDao.findAllByIdIn(Collections.singletonList(orderTable.getId())))
                .willReturn(Collections.singletonList(orderTable));
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

        // When
        TableGroup result = tableGroupService.create(tableGroup);

        // Then
        then(orderTableDao).should().findAllByIdIn(Collections.singletonList(orderTable.getId()));
        then(tableGroupDao).should().save(tableGroup);
        assertThat(result.getOrderTables()).contains(orderTable);
    }

    @Test
    @DisplayName("테이블 그룹을 제거하는 테스트")
    void ungroup() {
        // Given
        tableGroup.setId(1L);
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(Collections.singletonList(orderTable));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Collections.singletonList(orderTable.getId()),
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // When
        tableGroupService.ungroup(tableGroup.getId());

        // Then
        then(orderTableDao).should().findAllByTableGroupId(tableGroup.getId());
        then(orderDao).should().existsByOrderTableIdInAndOrderStatusIn(Collections.singletonList(orderTable.getId()),
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
        assertThat(orderTable.getTableGroupId()).isNull();
    }
}