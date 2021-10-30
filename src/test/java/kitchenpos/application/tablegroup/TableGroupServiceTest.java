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
        firstOrderTable.setId(1L);
        firstOrderTable.setEmpty(true);
        firstOrderTable.setNumberOfGuests(5);

        OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setId(2L);
        secondOrderTable.setEmpty(true);
        secondOrderTable.setNumberOfGuests(5);

        standardOrderTables = new LinkedList<>();
        standardOrderTables.add(firstOrderTable);
        standardOrderTables.add(secondOrderTable);

        standardTableGroup = new TableGroup();
        standardTableGroup.setId(1L);
        standardTableGroup.setCreatedDate(LocalDateTime.MAX);
        standardTableGroup.setOrderTables(standardOrderTables);
    }

}
