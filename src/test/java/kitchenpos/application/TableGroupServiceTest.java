package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @MockBean
    private OrderTableDao orderTableDao;

    @MockBean
    private TableGroupDao tableGroupDao;

    @MockBean
    private OrderDao orderDao;

    @Test
    void 단체_지정을_생성한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        orderTable1.setId(1L);
        orderTable2.setId(2L);
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        given(orderTableDao.findAllByIdIn(orderTableIds))
                .willReturn(orderTables);
        given(tableGroupDao.save(tableGroup))
                .willReturn(tableGroup);

        // when
        TableGroup result = tableGroupService.create(tableGroup);

        // then
        assertThat(result.getOrderTables()).containsAll(orderTables);
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        orderTable1.setId(1L);
        orderTable2.setId(2L);
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTable1.getTableGroupId()).isNull();
            softly.assertThat(orderTable1.isEmpty()).isFalse();
            softly.assertThat(orderTable2.getTableGroupId()).isNull();
            softly.assertThat(orderTable2.isEmpty()).isFalse();
        });
    }
}
