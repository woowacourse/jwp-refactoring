package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static java.util.Arrays.asList;
import static kitchenpos.fixture.FixtureFactory.createOrderTable;
import static kitchenpos.fixture.FixtureFactory.createTableGroup;
import static kitchenpos.ui.TableGroupRestController.TABLE_GROUP_API;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = TableGroupRestController.class)
class TableGroupRestControllerTest extends ControllerTest {
    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정 요청")
    @Test
    void create() throws Exception {
        OrderTable orderTable1 = createOrderTable(1L, null, 0, true);
        OrderTable orderTable2 = createOrderTable(2L, null, 0, true);
        TableGroup request = createTableGroup(null, asList(orderTable1, orderTable2), null);
        String body = objectMapper.writeValueAsString(request);

        when(tableGroupService.create(any())).thenReturn(new TableGroup());

        requestWithPost(TABLE_GROUP_API, body);
    }

    @DisplayName("단체 지정 해제 요청")
    @Test
    void ungroup() throws Exception{
        requestWithDelete(TABLE_GROUP_API + "/1");
    }
}