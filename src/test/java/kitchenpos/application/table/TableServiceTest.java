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
        standardTable.setId(1L);
        standardTable.setEmpty(true);
        standardTable.setNumberOfGuests(10);
        standardTable.setTableGroupId(1L);

        standardTables = new ArrayList<>();
        standardTables.add(standardTable);
    }

}
