package kitchenpos.application;

import kitchenpos.TestObjectFactory;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/delete_all.sql")
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 생성 메서드 테스트")
    @Test
    void create() {
        OrderTable orderTable = TestObjectFactory.creatOrderTable();

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("테이블 목록 조회 기능 테스트")
    @Test
    void list() {
        tableService.create(TestObjectFactory.creatOrderTable());
        tableService.create(TestObjectFactory.creatOrderTable());

        List<OrderTable> tables = tableService.list();

        assertThat(tables).hasSize(2);
    }

    @DisplayName("테이블의 empty 상태를 변경하는 기능 테스트")
    @Test
    void changeEmpty() {
        OrderTable savedOrderTable = tableService.create(TestObjectFactory.creatOrderTable());
        OrderTable changeEmptyOrderTable = TestObjectFactory.createChangeEmptyOrderTableDto(false);

        OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), changeEmptyOrderTable);

        assertAll(
                () -> assertThat(changedOrderTable.getId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(changedOrderTable.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("테이블의 empty 상태 변경 - 존재하지 않는 아이디를 입력받은 경우")
    @Test
    void changeEmptyWithNotFoundOrderTable() {
        OrderTable changeEmptyOrderTable = TestObjectFactory.createChangeEmptyOrderTableDto(false);

        assertThatThrownBy(() -> tableService.changeEmpty(100L, changeEmptyOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 empty 상태 변경 - 단체 테이블에 등록되어 있는 경우")
    @Test
    void changeEmptyWithRegisteredGroupTable() {
        OrderTable savedOrderTable1 = tableService.create(TestObjectFactory.creatOrderTable());
        OrderTable savedOrderTable2 = tableService.create(TestObjectFactory.creatOrderTable());
        OrderTable changeEmptyOrderTableDto = TestObjectFactory.createChangeEmptyOrderTableDto(false);
        List<OrderTable> orderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        tableGroupService.create(TestObjectFactory.createTableGroup(orderTables));

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), changeEmptyOrderTableDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 empty 상태 변경 - 주문 상태가 COOKING 혹은 MEAL 인경우 ")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL"})
    void changeEmptyWhenCooking(String orderStatus) {
        OrderTable savedOrderTable = tableService.create(TestObjectFactory.creatOrderTable());

        Order order = TestObjectFactory.createOrder(savedOrderTable.getId(), orderStatus, new ArrayList<>());
        orderDao.save(order);

        OrderTable changeEmptyOrderTable = TestObjectFactory.createChangeEmptyOrderTableDto(false);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), changeEmptyOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
