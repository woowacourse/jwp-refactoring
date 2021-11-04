package kitchenpos.application.tablegroup;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    protected static final Boolean BASIC_EMPTY_STATE = true;
    protected static final Integer BASIC_GUEST_NUMBER = 5;
    protected static final Integer BASIC_TABLE_NUMBER = 2;
    protected static final Long BASIC_TABLE_GROUP_ID = 1L;
    protected static final Long FIRST_TABLE_ID = 1L;
    protected static final Long SECOND_TABLE_ID = 2L;
    protected static final LocalDateTime BASIC_CREATED_TIME = LocalDateTime.MAX;

    protected List<OrderTable> standardOrderTables;
    protected TableGroup standardTableGroup;

    @Mock
    protected OrderDao orderDao;

    @Mock
    protected OrderTableDao orderTableDao;

    @Mock
    protected TableGroupDao tableGroupDao;

    @InjectMocks
    protected TableGroupService tableGroupService;

    @BeforeEach
    protected void setUp() {
        OrderTable firstOrderTable = new OrderTable();
        firstOrderTable.setId(FIRST_TABLE_ID);
        firstOrderTable.setEmpty(BASIC_EMPTY_STATE);
        firstOrderTable.setNumberOfGuests(BASIC_GUEST_NUMBER);

        OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setId(SECOND_TABLE_ID);
        secondOrderTable.setEmpty(BASIC_EMPTY_STATE);
        secondOrderTable.setNumberOfGuests(BASIC_GUEST_NUMBER);

        standardOrderTables = new LinkedList<>();
        standardOrderTables.add(firstOrderTable);
        standardOrderTables.add(secondOrderTable);

        standardTableGroup = new TableGroup();
        standardTableGroup.setId(BASIC_TABLE_GROUP_ID);
        standardTableGroup.setCreatedDate(BASIC_CREATED_TIME);
        standardTableGroup.setOrderTables(standardOrderTables);
    }

}
