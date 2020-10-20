package kitchenpos.ui;

import static kitchenpos.utils.TestObjects.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.application.OrderService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupRestControllerTest extends ControllerTest {
    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    OrderDao orderDao;

    @DisplayName("create: 2개 이상의 중복되지 않고 비어있지 않는 테이블목록에 대해 테이블 그룹 지정 요청시, 201 반환과 함께 그룹 지정된 테이블 그룹을 반환한다.")
    @Test
    void create() throws Exception {
        OrderTable firstSavedTable = orderTableDao.save(createTable(null, 0, true));
        OrderTable secondSavedTable = orderTableDao.save(createTable(null, 0, true));
        TableGroup tableGroupWithMultipleTable = createTableGroup(null, Lists.list(firstSavedTable, secondSavedTable));

        String groupApiUrl = "/api/table-groups";

        final ResultActions resultActions = create(groupApiUrl, tableGroupWithMultipleTable);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue(Long.class)))
                .andExpect(jsonPath("$.createdDate", notNullValue(LocalDateTime.class)))
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
    }

    @DisplayName("ungroup: 테이블 그룹 대상에 포함되어있는 테이블 모두 주문 완료 상태인 경우, 해당 테이블들의 테이블 그룹 해지 후 204 코드를 반환한다.")
    @Test
    void ungroup() throws Exception {
        OrderTable firstEmptyTable = orderTableDao.save(createTable(null, 0, true));
        OrderTable secondEmptyTable = orderTableDao.save(createTable(null, 0, true));

        orderDao.save(
                createOrder(firstEmptyTable.getId(), LocalDateTime.of(2020, 10, 20, 20, 40), OrderStatus.COMPLETION,
                        null));
        orderDao.save(
                createOrder(secondEmptyTable.getId(), LocalDateTime.of(2020, 10, 20, 21, 44), OrderStatus.COMPLETION,
                        null));

        TableGroup createdTableGroup = tableGroupService.create(
                createTableGroup(null, Lists.list(firstEmptyTable, secondEmptyTable)));

        String unGroupApiUrl = "/api/table-groups/{tableGroupId}";

        final ResultActions resultActions = deleteByPathId(unGroupApiUrl, createdTableGroup.getId());

        resultActions
                .andExpect(status().isNoContent());
    }
}