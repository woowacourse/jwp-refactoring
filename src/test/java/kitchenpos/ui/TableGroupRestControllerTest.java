package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.TestFixture;

class TableGroupRestControllerTest extends ControllerTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        final OrderTable orderTable1 = orderTableDao.save(TestFixture.getOrderTableWithEmpty());
        final OrderTable orderTable2 = orderTableDao.save(TestFixture.getOrderTableWithEmpty());
        tableGroup = TestFixture.getTableGroup(orderTable1, orderTable2);
    }

    @DisplayName("create: 단체 지정 등록 테스트")
    @Test
    void createTest() throws Exception {
        create("/api/table-groups", tableGroup)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.orderTables[0].id").value(11));
    }

    @DisplayName("delete: 단체 지정 해제 테스트")
    @Test
    void delete() throws Exception {
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        deleteByPathVariable("/api/table-groups/" + savedTableGroup.getId());
    }
}
