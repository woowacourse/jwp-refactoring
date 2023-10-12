package kitchenpos.application;

import com.sun.tools.javac.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql({"/h2-truncate.sql"})
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    private OrderTable savedOrderTableA;
    private OrderTable savedOrderTableB;

    @BeforeEach
    void setup() {
        OrderTable orderTableA = new OrderTable();
        orderTableA.setEmpty(true);
        savedOrderTableA = orderTableDao.save(orderTableA);

        OrderTable orderTableB = new OrderTable();
        orderTableB.setEmpty(true);
        savedOrderTableB = orderTableDao.save(orderTableB);
    }

    @Test
    @DisplayName("테이블 그룹 생성에 성공한다.")
    void succeedInRegisteringTableGroup() {
        // given & when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTableA, savedOrderTableB));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedTableGroup.getId()).isNotNull();
            softly.assertThat(savedTableGroup.getOrderTables()).hasSize(2);
        });
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 주문테이블이 2개 미만일 경우 예외가 발생한다.")
    void failToRegisterTableGroupWithWrongNumberOfOrderTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // when & then
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 주문 테이블이 등록되지 않았을 경우 예외가 발생한다.")
    void failToRegisterTableGroupWithNonExistOrderTable() {
        // given
        OrderTable orderTableA = new OrderTable();
        OrderTable orderTableB = new OrderTable();

        // when & then
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTableA, orderTableB));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 주문 테이블의 그룹이 지정되어 있으면 예외가 발생한다.")
    void failToRegisterTableGroupWithGroupedTable() {
        // given
        TableGroup wrongTableGroup = new TableGroup();
        wrongTableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup savedWrongTableGroup = tableGroupDao.save(wrongTableGroup);

        OrderTable orderTableA = new OrderTable();
        orderTableA.setEmpty(true);
        orderTableA.setTableGroupId(savedWrongTableGroup.getId());
        OrderTable savedOrderTableA = orderTableDao.save(orderTableA);

        OrderTable orderTableB = new OrderTable();
        orderTableB.setEmpty(true);
        orderTableB.setTableGroupId(savedWrongTableGroup.getId());
        OrderTable savedOrderTableB = orderTableDao.save(orderTableB);

        // when & then
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTableA, savedOrderTableB));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 주문 테이블이 비어있지 않으면 예외가 발생한다.")
    void failToRegisterTableGroupWithNonEmptyTable() {
        // given
        OrderTable orderTableA = new OrderTable();
        orderTableA.setEmpty(false);
        OrderTable savedOrderTableA = orderTableDao.save(orderTableA);

        OrderTable orderTableB = new OrderTable();
        orderTableB.setEmpty(false);
        OrderTable savedOrderTableB = orderTableDao.save(orderTableB);

        // when & then
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTableA, savedOrderTableB));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 해제에 성공한다.")
    void succeedInUngroupTable() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTableA, savedOrderTableB));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTableDao.findById(savedOrderTableA.getId()).get().getTableGroupId()).isNull();
            softly.assertThat(orderTableDao.findById(savedOrderTableB.getId()).get().getTableGroupId()).isNull();
        });
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.INCLUDE, names = {"COOKING", "MEAL"})
    @DisplayName("주문 상태가 조리 중이거나 식사 중인 테이블의 테이블 그룹을 해제할 경우 예외가 발생한다.")
    void failToUngroupTableWithCookingOrMealStatus(OrderStatus orderStatus) {
        // given
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(savedOrderTableA.getId());
        orderDao.save(order);

        // when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTableA, savedOrderTableB));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
