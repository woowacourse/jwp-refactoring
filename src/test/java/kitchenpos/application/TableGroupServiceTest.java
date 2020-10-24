package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql("/truncate.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private static Stream<List<OrderTable>> wrongSizeOrderTableProvider() {
        OrderTable table1 = new OrderTable(0, true);

        return Stream.of(null, new ArrayList<>(), Collections.singletonList(table1));
    }

    @DisplayName("2 개 이상의 빈 테이블을 단체로 지정할 수 있다.")
    @Test
    void create() {
        //given
        OrderTable table1 = orderTableDao.save(new OrderTable(0, true));
        OrderTable table2 = orderTableDao.save(new OrderTable(0, true));

        //when
        TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), Arrays.asList(table1, table2)));

        //then
        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }

    @DisplayName("테이블이 2개 미만이면 단체로 지정할 수 없다.")
    @ParameterizedTest
    @MethodSource("wrongSizeOrderTableProvider")
    void createException1(List<OrderTable> orderTables) {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블을 2개 이상 입력해주세요.");
    }

    @DisplayName("존재하지 않는 테이블을 단체로 지정할 수 없다.")
    @Test
    void createException2() {
        OrderTable table1 = new OrderTable(0, true);
        OrderTable table2 = new OrderTable(0, true);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now(), Arrays.asList(table1, table2))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블을 단체로 지정할 수 없습니다.");
    }

    @DisplayName("단체 지정은 중복될 수 없다.")
    @Test
    void createException4() {
        //given
        OrderTable table1 = orderTableDao.save(new OrderTable(0, true));
        OrderTable table2 = orderTableDao.save(new OrderTable(0, true));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(table1));
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        table1.setTableGroupId(savedTableGroup.getId());
        orderTableDao.save(table1);

        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now(), Arrays.asList(table1, table2))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%d번 테이블 : 단체 지정은 중복될 수 없습니다.", table1.getId());
    }

    @DisplayName("단체 지정을 해지할 수 있다.")
    @Test
    void ungroup() {
        //given
        OrderTable table1 = orderTableDao.save(new OrderTable(0, true));
        OrderTable table2 = orderTableDao.save(new OrderTable(0, true));
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), Arrays.asList(table1, table2)));

        table1.setTableGroupId(tableGroup.getId());
        table2.setTableGroupId(tableGroup.getId());
        orderTableDao.save(table1);
        orderTableDao.save(table2);

        //when
        tableGroupService.ungroup(tableGroup.getId());

        //then
        OrderTable savedTable1 = orderTableDao.findById(table1.getId())
                .orElseThrow(RuntimeException::new);
        OrderTable savedTable2 = orderTableDao.findById(table2.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(savedTable1.getTableGroupId()).isNull();
        assertThat(savedTable2.getTableGroupId()).isNull();
    }

    @DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없다.")
    @Test
    void ungroupException1() {
        //given
        OrderTable table1 = orderTableDao.save(new OrderTable(0, true));
        OrderTable table2 = orderTableDao.save(new OrderTable(0, true));
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), Arrays.asList(table1, table2)));

        table1.setTableGroupId(tableGroup.getId());
        table2.setTableGroupId(tableGroup.getId());
        orderTableDao.save(table1);
        orderTableDao.save(table2);

        orderDao.save(new Order(table1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>()));

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없습니다.");

    }

    @AfterEach
    void tearDown() {
        orderDao.deleteAll();
        orderTableDao.deleteAll();
        tableGroupDao.deleteAll();
    }
}