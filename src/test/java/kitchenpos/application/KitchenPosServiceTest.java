package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_FALSE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_TRUE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY;
import static kitchenpos.constants.Constants.TEST_TABLE_GROUP_CREATED_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class KitchenPosServiceTest {

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    protected void setCreatedTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(TEST_TABLE_GROUP_CREATED_DATE);
        tableGroup.setOrderTables(orderTables);

        tableGroupService.create(tableGroup);
    }

    protected long getCreatedEmptyOrderTableId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY);
        OrderTable createdOrderTable = tableService.create(orderTable);

        Long createdOrderTableId = createdOrderTable.getId();
        assertThat(createdOrderTable).isNotNull();
        return createdOrderTableId;
    }

    protected long getCreatedNotEmptyOrderTableId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        OrderTable createdOrderTable = tableService.create(orderTable);

        Long createdOrderTableId = createdOrderTable.getId();
        assertThat(createdOrderTable).isNotNull();
        return createdOrderTableId;
    }
}
