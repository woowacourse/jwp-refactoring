package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.name.Name;
import kitchenpos.order.domain.Order;
import kitchenpos.table.application.TableEmptyChangeService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.price.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupRequest.OrderTableOfGroupRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.application.TableGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TableGroupServiceTest extends ServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableEmptyChangeService tableEmptyChangeService;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private Menu menu;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, null, 4, true);
        orderTable2 = new OrderTable(2L, null, 4, true);
        menu = new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
            new MenuGroup(1L, "치킨"),
            new MenuProducts(Collections.singletonList(
                new MenuProduct(new Product("후라이드치킨", BigDecimal.valueOf(16000)), 2L)
            ))
        );
    }

    @DisplayName("단체 지정 저장")
    @Test
    void create() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(orderTableRepository.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(
            orderTables
        );
        when(tableGroupRepository.save(any(TableGroup.class))).thenAnswer(
            invocation -> invocation.getArgument(0)
        );

        TableGroupResponse actual = tableGroupService.create(
            new TableGroupRequest(orderTableRequestFromEntity(orderTables))
        );
        List<OrderTableResponse> expectedOrderTablesResponse = OrderTableResponse.listFrom(
            orderTables
        );
        TableGroupResponse expected = new TableGroupResponse(1L, expectedOrderTablesResponse);

        verify(tableGroupRepository, times(1)).save(any(TableGroup.class));
        assertThat(actual).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expected);
    }

    private List<OrderTableOfGroupRequest> orderTableRequestFromEntity(
        final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable -> new OrderTableOfGroupRequest(orderTable.getId()))
            .collect(Collectors.toList());
    }

    @DisplayName("1개 이하의 주문 테이블로 단체 지정을 등록할 경우 예외 처리")
    @Test
    void createWhenOrderTableSizeIsLessThan2() {
        List<OrderTable> orderTables = Collections.singletonList(
            new OrderTable(1L, null, 0, true)
        );

        assertThatThrownBy(() -> tableGroupService.create(
            new TableGroupRequest(orderTableRequestFromEntity(orderTables))
        )).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 테이블로 단체 지정을 등록할 경우 예외 처리")
    @Test
    void createWhenSomeOrderTablesCannotBeFound() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 0, true),
            new OrderTable(2L, null, 0, true)
        );
        when(orderTableRepository.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(
            Collections.singletonList(orderTables.get(0))
        );

        assertThatThrownBy(() -> tableGroupService.create(
            new TableGroupRequest(orderTableRequestFromEntity(orderTables))
        )).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 테이블로 단체 지정을 등록할 경우 예외 처리")
    @Test
    void createWhenSomeOrderTablesAreNotEmpty() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 0, true),
            new OrderTable(2L, null, 0, false)
        );
        when(orderTableRepository.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(
            orderTables
        );

        assertThatThrownBy(() -> tableGroupService.create(
            new TableGroupRequest(orderTableRequestFromEntity(orderTables))
        )).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체 지정이 된 테이블을 단체 지정으로 등록할 경우 예외 처리")
    @Test
    void createWhenSomeOrderTablesAreAlreadyDesignatedAsGroup() {
        new TableGroup(1L, Arrays.asList(orderTable1, orderTable2));
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(orderTableRepository.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(
            orderTables
        );

        assertThatThrownBy(() -> tableGroupService.create(
            new TableGroupRequest(orderTableRequestFromEntity(orderTables))
        )).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 제거")
    @Test
    void ungroup() {
        long tableGroupId = 1L;
        TableGroup tableGroup = new TableGroup(
            tableGroupId,
            Arrays.asList(orderTable1, orderTable2)
        );
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(tableGroupRepository.findById(tableGroupId)).thenReturn(Optional.of(tableGroup));
        when(tableEmptyChangeService.canChangeEmpty(any())).thenReturn(true);

        tableGroupService.ungroup(tableGroupId);

        for (OrderTable orderTable : orderTables) {
            assertThat(orderTable.getTableGroup()).isNull();
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @DisplayName("조리나 식사 상태인 테이블이 있는 단체 지정을 제거할 경우 예외 처리")
    @Test
    void deleteWithNotFoundTableGroup() {
        long tableGroupId = 1L;
        TableGroup tableGroup = new TableGroup(
            tableGroupId,
            Arrays.asList(orderTable1, orderTable2)
        );
        new Order(orderTable1);
        when(tableGroupRepository.findById(tableGroupId)).thenReturn(Optional.of(tableGroup));
        when(tableEmptyChangeService.canChangeEmpty(any())).thenReturn(false);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private List<Long> convertIdsFromOrderTables(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}
