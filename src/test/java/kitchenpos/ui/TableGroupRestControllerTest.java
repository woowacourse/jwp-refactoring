package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableRequest;

class TableGroupRestControllerTest extends ControllerTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("create: 단체 지정 등록 테스트")
    @Test
    void createTest() throws Exception {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable orderTable1 = orderTableDao.save(orderTable);
        final OrderTable orderTable2 = orderTableDao.save(orderTable);

        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));

        create("/api/table-groups", tableGroupRequest)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.orderTables[0].id").value(1))
                .andExpect(jsonPath("$.orderTables[1].id").value(2));
    }

    @DisplayName("delete: 단체 지정 해제 테스트")
    @Test
    void delete() throws Exception {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable orderTable1 = orderTableDao.save(orderTable);
        final OrderTable orderTable2 = orderTableDao.save(orderTable);

        final TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));
        final TableGroup saved = tableGroupDao.save(tableGroup);

        deleteByPathVariable("/api/table-groups/" + saved.getId());
    }
}
