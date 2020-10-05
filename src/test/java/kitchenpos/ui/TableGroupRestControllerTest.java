package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.TextFixture;

class TableGroupRestControllerTest extends ControllerTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("create: 단체 지정 등록 테스트")
    @Test
    void createTest() throws Exception {
        final OrderTable orderTable1 = orderTableDao.findById(1L).orElseThrow(IllegalArgumentException::new);
        final OrderTable orderTable2 = orderTableDao.findById(2L).orElseThrow(IllegalArgumentException::new);
        final TableGroup tableGroup = TextFixture.getTableGroup(orderTable1, orderTable2);

        create("/api/table-groups", tableGroup)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderTables[0].id").value(1));
    }

    @DisplayName("delete: 단체 지정 해제 테스트")
    @Test
    void delete() throws Exception {
        final OrderTable orderTable1 = orderTableDao.findById(1L).orElseThrow(IllegalArgumentException::new);
        final OrderTable orderTable2 = orderTableDao.findById(2L).orElseThrow(IllegalArgumentException::new);

        final TableGroup tableGroup = TextFixture.getTableGroup(orderTable1, orderTable2);

        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        deleteByPathVariable("/api/table-groups/" + savedTableGroup.getId());
    }
}
