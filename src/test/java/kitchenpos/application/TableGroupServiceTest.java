package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.exception.AlreadyInTableGroupException;
import kitchenpos.exception.OrderNotCompleteException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.TableGroupSizeException;
import kitchenpos.exception.TableGroupWithNotEmptyTableException;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@SpringBootTest
@Sql("classpath:truncate.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    private List<OrderTable> tables;

    @BeforeEach
    void setUp() {
        tables = Lists.newArrayList(OrderTableFixture.createEmptyWithId(OrderTableFixture.ID1),
            OrderTableFixture.createEmptyWithId(OrderTableFixture.ID2));
    }

    @DisplayName("정상적으로 테이블을 그룹화 한다.")
    @Test
    void create() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        TableGroup tableWithId = TableGroupFixture.createWithId(1L);

        orderTableRepository.saveAll(tables);

        TableGroup savedTableGroup = tableGroupService.create(request);

        assertThat(savedTableGroup).isEqualToIgnoringGivenFields(tableWithId, "createdDate");
    }

    @DisplayName("그룹 요청 테이블의 개수가 0개인 경우 예외를 반환한다.")
    @Test
    void createWithEmptyTable() {
        TableGroupCreateRequest request = TableGroupFixture.createEmptyRequest();

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(TableGroupSizeException.class);
    }

    @DisplayName("그룹 요청 테이블의 개수가 1개인 경우 예외를 반환한다.")
    @Test
    void createWithOneTable() {
        TableGroupCreateRequest request = TableGroupFixture.createOneTableRequest();

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(TableGroupSizeException.class);
    }

    @DisplayName("실제 존재하지 않는 테이블을 그룹화하는 경우 예외를 반환한다.")
    @Test
    void createWithNotExistTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        tables.remove(0);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(OrderTableNotFoundException.class);
    }

    @DisplayName("Empty가 아닌 상태의 테이블을 그룹화 하는 경우 예외를 반환한다.")
    @Test
    void createWithNotEmptyTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        OrderTable notEmptyTable = OrderTableFixture.createNotEmptyWithId(null);
        tables.remove(1);
        tables.add(notEmptyTable);

        orderTableRepository.saveAll(tables);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(TableGroupWithNotEmptyTableException.class)
            .hasMessage(
                String.format("%d table is not empty Table. For group tables, first change empty",
                    notEmptyTable.getId()));
    }

    @DisplayName("이미 다른 그룹이 있는 테이블을 그룹화 하는 경우 예외를 반환한다.")
    @Test
    void createWithNotNullGroupTable() {
        TableGroupCreateRequest request = TableGroupFixture.createRequest();
        OrderTable tableInGroup = OrderTableFixture.createGroupTableWithId(null, 1L);
        tables.remove(1);
        tables.add(tableInGroup);

        orderTableRepository.saveAll(tables);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(AlreadyInTableGroupException.class)
            .hasMessage(String.format("%d table is already in table group %d", tableInGroup.getId(),
                tableInGroup.getTableGroupId()));
    }

    @DisplayName("정상적으로 테이블을 언그룹화 한다.")
    @Test
    void ungroup() {
        TableGroup withId = TableGroupFixture.createWithId(1L);
        orderTableRepository.saveAll(tables);
        tableGroupService.ungroup(withId.getId());

        assertThat(tables)
            .extracting(OrderTable::getTableGroupId)
            .allMatch(Objects::isNull);
    }

    @DisplayName("식사를 마치치 않은 상태(Meal, Cooking)인 테이블을 Group 해제할 때 예외를 반환한다.")
    @Test
    void ungroupWithNotCompleteTable() {
        TableGroup withNotCompleteTable = TableGroupFixture.createWithId(1L);
        Order order = OrderFixture.createWithoutId(OrderFixture.MEAL_STATUS,
            tables.get(0).getId());
        OrderTable orderTable1 = OrderTableFixture.createGroupTableWithId(1L, 1L);
        OrderTable orderTable2 = OrderTableFixture.createGroupTableWithId(2L, 1L);

        tableGroupRepository.save(withNotCompleteTable);
        orderTableRepository.saveAll(Arrays.asList(orderTable1, orderTable2));
        orderRepository.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(withNotCompleteTable.getId()))
            .isInstanceOf(OrderNotCompleteException.class);
    }
}
