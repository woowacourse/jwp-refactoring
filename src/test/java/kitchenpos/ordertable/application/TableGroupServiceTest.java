package kitchenpos.ordertable.application;

import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.request.TableGroupCreateRequest;
import kitchenpos.order.dto.response.TableGroupResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.repository.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("2 개 이상의 빈 테이블을 단체로 지정할 수 있다.")
    @Test
    void create() {
        //given
        OrderTable table1 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable table2 = orderTableRepository.save(new OrderTable(0, true));

        //when
        TableGroupResponse response = tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(table1.getId(), table2.getId())));

        //then
        List<OrderTable> findOrderTables = orderTableRepository.findAllByTableGroupId(response.getId());
        assertThat(findOrderTables).hasSize(2);
    }

    @DisplayName("테이블이 2개 미만이면 단체로 지정할 수 없다.")
    @Test
    void createException1() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(Collections.singletonList(orderTable.getId()))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블을 2개 이상 입력해주세요.");
    }

    @DisplayName("존재하지 않는 테이블을 단체로 지정할 수 없다.")
    @Test
    void createException2() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(-1L, orderTable1.getId(), orderTable2.getId()))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블을 단체로 지정할 수 없습니다.");
    }

    @DisplayName("단체 지정은 중복될 수 없다.")
    @Test
    void createException4() {
        //given
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        OrderTable table1 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable table2 = orderTableRepository.save(new OrderTable(0, true));
        table1.groupBy(tableGroup);

        orderTableRepository.save(table1);

        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(table1.getId(), table2.getId()))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%d번 테이블 : 단체 지정은 중복될 수 없습니다.", table1.getId());
    }

    @DisplayName("단체 지정을 해지할 수 있다.")
    @Test
    void ungroup() {
        //given
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);
        orderTable1.groupBy(tableGroup);
        orderTable2.groupBy(tableGroup);
        orderTableRepository.saveAll(Arrays.asList(orderTable1, orderTable2));

        //when
        tableGroupService.ungroup(tableGroup.getId());

        //then
        List<OrderTable> findOrderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        assertThat(findOrderTables).isEmpty();
    }

    @DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없다.")
    @ParameterizedTest()
    @CsvSource(value = {"COOKING", "MEAL"})
    void ungroupException1(OrderStatus orderStatus) {
        //given
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);
        orderTable1.groupBy(tableGroup);
        orderTable2.groupBy(tableGroup);
        orderTableRepository.saveAll(Arrays.asList(orderTable1, orderTable2));

        orderRepository.save(new Order(orderTable1, orderStatus));

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없습니다.");

    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        tableGroupRepository.deleteAll();
    }
}