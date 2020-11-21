package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupTablesTest {
    @Mock
    private TableOrderEmptyValidator tableOrderEmptyValidator;

    @DisplayName("그룹을 지정할 테이블이 null이면 예외 처리한다.")
    @Test
    void createWithEmptyList() {
        assertThatThrownBy(() -> new GroupTables(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 지정할 테이블이 2개 미만이면 예외 처리한다.")
    @Test
    void createWithOneElement() {
        assertThatThrownBy(() -> new GroupTables(Collections.singletonList(new OrderTable())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주어진 수와 같은 크기인지 확인한다.")
    @Test
    void hasSize() {
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable());
        GroupTables groupTables = new GroupTables(orderTables);

        assertThat(groupTables.hasSize(orderTables.size())).isTrue();
    }

    @DisplayName("테이블 그룹을 지정한다.")
    @Test
    void designateGroup() {
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(1, true), new OrderTable(2, true));
        GroupTables groupTables = new GroupTables(orderTables);

        long newTableGroupId = 1L;
        groupTables.designateGroup(newTableGroupId);

        assertThat(groupTables).extracting(GroupTables::getOrderTables,
            InstanceOfAssertFactories.list(OrderTable.class)).extracting(OrderTable::getTableGroupId)
            .containsOnly(newTableGroupId);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(1L, 1L, 1, false),
            new OrderTable(2L, 1L, 2, false));
        GroupTables groupTables = new GroupTables(orderTables);
        doNothing().when(tableOrderEmptyValidator).validate(anyList());

        groupTables.ungroup(tableOrderEmptyValidator);

        assertAll(
            () -> assertThat(groupTables).extracting(GroupTables::getOrderTables,
                InstanceOfAssertFactories.list(OrderTable.class)).extracting(OrderTable::getTableGroupId)
                .containsOnlyNulls(),
            () -> verify(tableOrderEmptyValidator).validate(anyList())
        );
    }
}