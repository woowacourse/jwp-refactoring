package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static kitchenpos.fixture.OrderFixture.createOrderWithOrderStatusAndTableId;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithEmpty;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithNumberOfGuest;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithTableGroupId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql(value = "/truncate.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("Table 생성")
    @Test
    void create() {
        OrderTable orderTable = createOrderTableWithEmpty(false);

        OrderTable actual = tableService.create(orderTable);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getTableGroupId()).isNull();
        });
    }

    @DisplayName("Table 전체 조회")
    @Test
    void list() {
        orderTableDao.save(createOrderTableWithEmpty(false));

        List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSize(1);
    }

    @DisplayName("테이블이 비어있는지 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable orderTable = createOrderTableWithEmpty(false);
        OrderTable saved = orderTableDao.save(orderTable);

        OrderTable expect = createOrderTableWithEmpty(true);
        OrderTable actual = tableService.changeEmpty(saved.getId(), expect);

        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("테이블이 비어있는지 여부 변경시 table group Id가 null이 아닐 때 예외 테스트")
    @Test
    void changeEmptyNonNullTableGroupId() {
        TableGroup savedTableGroup = tableGroupDao.save(TableGroupFixture.createTableGroupWithOrderTables(null));
        OrderTable orderTable = createOrderTableWithEmpty(false);
        OrderTable saved = orderTableDao.save(createOrderTableWithTableGroupId(savedTableGroup.getId()));

        assertThatThrownBy(() -> tableService.changeEmpty(saved.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있는지 여부 변경시 주문 상태가 식사중이거나 조리중일 때 예외 테스트")
    @Test
    void changeEmptyWrongOrderStatus() {
        OrderTable saved = orderTableDao.save(createOrderTableWithEmpty(false));
        orderDao.save(createOrderWithOrderStatusAndTableId(OrderStatus.MEAL.name(), saved.getId()));

        assertThatThrownBy(() -> tableService.changeEmpty(saved.getId(), saved))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 바꾸기")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedOrderTable = orderTableDao.save(createOrderTableWithNumberOfGuest(7));
        OrderTable expect = createOrderTableWithNumberOfGuest(2);

        OrderTable changed = tableService.changeNumberOfGuests(savedOrderTable.getId(), expect);

        assertThat(changed.getNumberOfGuests()).isEqualTo(expect.getNumberOfGuests());
    }

    @DisplayName("테이블 바꿀 손님 수 0명 미만일 때 예외 발생")
    @Test
    void changeWrongNumberOfGuests() {
        OrderTable wrongNumberOfGuest = createOrderTableWithNumberOfGuest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, wrongNumberOfGuest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있을 때 손님 수 변경 예외 발생")
    @Test
    void changeEmptyTableNumberOfGuests() {
        OrderTable emptyTable = createOrderTableWithEmpty(true);
        OrderTable savedOrderTable = orderTableDao.save(emptyTable);
        OrderTable orderTable = createOrderTableWithNumberOfGuest(3);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}