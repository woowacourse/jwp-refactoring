package kitchenpos.domain.entity;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.core.AggregateReference;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.repository.OrderDao;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.service.TableGroupCreateService;
import kitchenpos.domain.service.TableGroupUngroupService;

@ExtendWith(MockitoExtension.class)
class TableGroupTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private TableGroupCreateService tableGroupCreateService;
    @InjectMocks
    private TableGroupUngroupService tableGroupUngroupService;

    @DisplayName("단체 지정 생성")
    @TestFactory
    Stream<DynamicTest> create() {
        return Stream.of(
                dynamicTest("단체 지정을 생성한다.", this::createSuccess),
                dynamicTest("요청한 테이블들이 하나씩 존재해야 한다.", this::orderTableMismatch),
                dynamicTest("테이블에 손님이 없어야 한다.", this::orderTableNotEmpty),
                dynamicTest("단체 지정이 되어있지 않아야 한다.", this::orderTableHasTableGroup)
        );
    }

    private void createSuccess() {
        TableGroup tableGroup = TABLE_GROUP_CREATE_REQUEST.toEntity();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(asList(new OrderTable(1L, null, 0, true),
                        new OrderTable(2L, null, 0, true)));

        TableGroup created = tableGroup.create(tableGroupCreateService);

        assertThat(created.getCreatedDate()).isNotNull();
    }

    private void orderTableMismatch() {
        TableGroup tableGroup = TABLE_GROUP_CREATE_REQUEST.toEntity();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(singletonList(new OrderTable(1L, null, 0, true)));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroup.create(tableGroupCreateService));
    }

    private void orderTableNotEmpty() {
        TableGroup tableGroup = TABLE_GROUP_CREATE_REQUEST.toEntity();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(asList(new OrderTable(1L, null, 0, false),
                        new OrderTable(2L, null, 0, true)));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroup.create(tableGroupCreateService));
    }

    private void orderTableHasTableGroup() {
        TableGroup tableGroup = TABLE_GROUP_CREATE_REQUEST.toEntity();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(asList(new OrderTable(1L, 1L, 0, true),
                        new OrderTable(2L, null, 0, true)));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroup.create(tableGroupCreateService));
    }

    @DisplayName("단체 지정 해제")
    @TestFactory
    Stream<DynamicTest> ungroup() {
        return Stream.of(
                dynamicTest("단체 지정을 해제한다.", this::ungroupSuccess),
                dynamicTest("테이블에 모든 주문은 완료 상태이어야 한다.", this::invalidOrderStatus)
        );
    }

    private void ungroupSuccess() {
        OrderTable orderTable1 = new OrderTable(1L, 1L, 0, false);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 0, false);
        TableGroup tableGroup = new TableGroup(1L,
                asList(new AggregateReference<>(orderTable1.getId()),
                        new AggregateReference<>(orderTable2.getId())), LocalDateTime.now());


        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(tableGroup.orderTableIds(),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
        given(orderTableRepository.save(orderTable1)).willReturn(orderTable1);
        given(orderTableRepository.save(orderTable2)).willReturn(orderTable2);

        tableGroup.ungroup(tableGroupUngroupService);

        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }

    private void invalidOrderStatus() {
        OrderTable orderTable1 = new OrderTable(1L, 1L, 0, false);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 0, false);
        TableGroup tableGroup = new TableGroup(1L,
                asList(new AggregateReference<>(orderTable1.getId()),
                        new AggregateReference<>(orderTable2.getId())), LocalDateTime.now());

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(tableGroup.orderTableIds(),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroup.ungroup(tableGroupUngroupService));
    }
}
