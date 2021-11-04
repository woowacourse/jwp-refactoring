package kitchenpos.application.table;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    protected static final Boolean BASIC_EMPTY_STATE = true;
    protected static final Integer BASIC_GUEST_NUMBER = 10;
    protected static final Integer BASIC_TABLE_NUMBER = 1;
    protected static final Long BASIC_ORDER_TABLE_ID = 1L;
    protected static final Long BASIC_TABLE_GROUP_ID = 1L;

    protected List<OrderTable> standardTables;
    protected OrderTable standardTable;

    @Mock
    protected OrderDao orderDao;

    @Mock
    protected OrderTableDao orderTableDao;

    @InjectMocks
    protected TableService tableService;

    @BeforeEach
    protected void setUp() {
        standardTable = new OrderTable();
        standardTable.setId(BASIC_ORDER_TABLE_ID);
        standardTable.setEmpty(BASIC_EMPTY_STATE);
        standardTable.setNumberOfGuests(BASIC_GUEST_NUMBER);
        standardTable.setTableGroupId(BASIC_TABLE_GROUP_ID);

        standardTables = new ArrayList<>();
        standardTables.add(standardTable);
    }

}
