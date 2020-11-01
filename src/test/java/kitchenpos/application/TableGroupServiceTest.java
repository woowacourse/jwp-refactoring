package kitchenpos.application;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private TableGroupService tableGroupService;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    private static Stream<Arguments> generateInvalidTable() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Collections.singletonList(new OrderTable(10, false)))
        );
    }

    @BeforeEach
    @Test
    void setUp() {
        this.tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
        this.orderTable1 = new OrderTable();
        orderTable1.updateEmpty(true);
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(10);

        this.orderTable2 = new OrderTable();
        orderTable2.updateEmpty(true);
        orderTable2.setId(2L);
        orderTable2.setNumberOfGuests(10);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);
    }

    @AfterEach
    void tearDown() {
        orderTableRepository.deleteAll();
    }

    @DisplayName("Table Group을 생성하고 DB에 저장한다.")
    @Test
    void createTest() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = new TableGroup();

        when(orderTableRepository.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())))
                .thenReturn(orderTables);
        when(tableGroupRepository.save(tableGroup)).thenReturn(tableGroup);

        TableGroup result = tableGroupService.create(tableGroup, orderTables);

        assertThat(orderTable1.getTableGroup()).isEqualTo(tableGroup);
        assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup);
    }

    @DisplayName("Table Group 생성 시, Table의 개수가 2개 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("generateInvalidTable")
    void invalidTableSizeExceptionTest(List orderTables) {
        TableGroup tableGroup = new TableGroup();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup, Collections.singletonList(orderTable1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Table Group 생성 시, Table은 저장된 상태여야 한다.")
    @Test
    void notSavedOrderTableExceptionTest() {
        orderTableRepository.deleteById(orderTable1.getId());

        when(orderTableRepository.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())))
                .thenReturn(Collections.singletonList(orderTable2));

        TableGroup tableGroup = new TableGroup();
        assertThatThrownBy(() -> tableGroupService.create(tableGroup, Arrays.asList(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Table Group 생성 시, Table이 empty가 아니라면 예외가 발생한다.")
    @Test
    void tableEmptyExceptionTest() {
        orderTable1.updateEmpty(false);

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(orderTableRepository.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())))
                .thenReturn(orderTables);

        TableGroup tableGroup = new TableGroup();
        assertThatThrownBy(() -> tableGroupService.create(tableGroup, Arrays.asList(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Table Group 생성 시, 이미 Table Group이 있는 Table이라면 예외가 발생한다.")
    @Test
    void tableAlreadyRegisteredExceptionTest() {
        orderTable1.updateTableGroup(new TableGroup());

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        when(orderTableRepository.findAllByIdIn(orderTableIds)).thenReturn(orderTables);

        TableGroup tableGroup = new TableGroup();
        assertThatThrownBy(() -> tableGroupService.create(tableGroup, Arrays.asList(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup을 분리한다.")
    @Test
    void ungroupTest() {
        final long TABLE_GROUP_ID = 1L;
        TableGroup tableGroup = new TableGroup();

        // given
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(orderTableRepository.findAllByTableGroup(tableGroup)).thenReturn(orderTables);
        when(tableGroupRepository.findById(TABLE_GROUP_ID)).thenReturn(Optional.of(tableGroup));

        // when
        tableGroupService.ungroup(TABLE_GROUP_ID);

        // then
        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable1.isEmpty()).isFalse();
        assertThat(orderTable2.getTableGroup()).isNull();
        assertThat(orderTable2.isEmpty()).isFalse();
    }

    @DisplayName("TableGroup 분리 시, 주문이 이미 요리/식사 중이면 예외가 발생한다.")
    @Test
    void invalidStateExceptionTest() {
        final long TABLE_GROUP_ID = 1L;
        TableGroup tableGroup = new TableGroup();

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(tableGroupRepository.findById(TABLE_GROUP_ID)).thenReturn(Optional.of(tableGroup));
        when(orderTableRepository.findAllByTableGroup(tableGroup)).thenReturn(orderTables);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(TABLE_GROUP_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
