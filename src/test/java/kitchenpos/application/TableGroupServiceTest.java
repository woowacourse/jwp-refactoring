package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Autowired
    private DataSource dataSource;

    private TableGroupService tableGroupService;

    @Mock
    private OrderDao mockedOrderDao;
    @Mock
    private OrderTableDao mockedOrderTableDao;
    @Mock
    private OrderTableDao mockedTableGroupDao;


    @BeforeEach
    void setUp() {
        var orderDao = new JdbcTemplateOrderDao(dataSource);
        var orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        var tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Test
    void 등록시_두_테이블_이상이어야한다() {
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록시_기존_테이블이어야한다() {
        var tableGroup = new TableGroup();
        var unsaved = 테이블9();
        tableGroup.setOrderTables(List.of(테이블1(), unsaved));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록시_테이블들은_비어있어야한다() {
        var orderDao = new JdbcTemplateOrderDao(dataSource);
        var tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        this.tableGroupService = new TableGroupService(orderDao, mockedOrderTableDao, tableGroupDao);

        var tableGroupWithNotEmpty = new TableGroup();
        var notEmptyTable = 테이블2();
        notEmptyTable.setEmpty(false);
        tableGroupWithNotEmpty.setOrderTables(List.of(테이블1(), notEmptyTable));
        when(mockedOrderTableDao.findAllByIdIn(any())).thenReturn(tableGroupWithNotEmpty.getOrderTables());

        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithNotEmpty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록시_생성시각을_기록한다() {
        LocalDateTime startedTime = LocalDateTime.now();
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1(), 테이블2()));

        assertThat(tableGroupService.create(tableGroup).getCreatedDate()).isBetween(startedTime, LocalDateTime.now());
    }

    @Test
    void 등록시_테이블들을_이용중으로_바꾼다() {
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1(), 테이블2()));

        assertThat(tableGroupService.create(tableGroup).getOrderTables()).allMatch(it -> !it.isEmpty());
    }

    @Test
    void 등록한_테이블그룹을_반환한다() {
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1(), 테이블2()));

        assertThat(tableGroupService.create(tableGroup).getId()).isNotNull();
    }

    @Test
    void 그룹해제시_조리중이거나_식사중이면_안된다() {
        var orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        var tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        this.tableGroupService = new TableGroupService(mockedOrderDao, orderTableDao, tableGroupDao);
        when(mockedOrderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1(), 테이블2()));
        var saved = tableGroupService.create(tableGroup);

        assertThatThrownBy(() -> tableGroupService.ungroup(saved.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹해제시_테이블그룹을_제거한다() {
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1(), 테이블2()));

        var groupped = tableGroupService.create(tableGroup);
        tableGroupService.ungroup(groupped.getId());

        List<OrderTable> orderTables = new JdbcTemplateOrderTableDao(dataSource).findAllByIdIn(List.of(테이블1().getId(), 테이블2().getId()));
        orderTables.forEach(it -> assertThat(it.getTableGroupId()).isNull());
    }
}
