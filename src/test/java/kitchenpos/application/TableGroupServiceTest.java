package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;

class TableGroupServiceTest extends ServiceTest{

    @Autowired
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = makeTableGroup();
    }

    @Test
    void 테이블_그룹을_생성한다() {
        Mockito.when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(List.of(makeEmptyOrderTableById(1L), makeEmptyOrderTableById(2L)));
        Mockito.when(tableGroupDao.save(any(TableGroup.class)))
                .thenReturn(tableGroup);
        Mockito.when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(makeOrderTableById(1L));

        TableGroup saved = tableGroupService.create(tableGroup);
        assertThat(saved.getId()).isEqualTo(tableGroup.getId());
    }

    @Test
    void ungroup() {
        Mockito.when(orderTableDao.findAllByTableGroupId(anyLong()))
                .thenReturn(List.of(makeOrderTableById(1L)));
        Mockito.when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .thenReturn(false);
        Mockito.when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(makeEmptyOrderTableById(1L));

        assertThatCode(
                () -> tableGroupService.ungroup(1L)
        ).doesNotThrowAnyException();
    }

    private TableGroup makeTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(List.of(makeOrderTableById(1L), makeOrderTableById(2L)));
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    private OrderTable makeOrderTableById(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }

    private OrderTable makeEmptyOrderTableById(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }
}
