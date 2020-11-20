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

@SuppressWarnings("NonAsciiCharacters")
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
        OrderTable 첫번째빈테이블 = orderTableDao.save(createTable(null, 0, true));
        OrderTable 두번째빈테이블 = orderTableDao.save(createTable(null, 0, true));
        TableGroup 지정하려는테이블그룹 = createTableGroup(null, Lists.list(첫번째빈테이블, 두번째빈테이블));

        String 테이블그룹추가_API_URL = "/api/table-groups";

        final ResultActions resultActions = create(테이블그룹추가_API_URL, 지정하려는테이블그룹);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue(Long.class)))
                .andExpect(jsonPath("$.createdDate", notNullValue(LocalDateTime.class)))
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
    }

    @DisplayName("ungroup: 테이블 그룹 대상에 포함되어있는 테이블 모두 주문 완료 상태인 경우, 해당 테이블들의 테이블 그룹 해지 후 204 코드를 반환한다.")
    @Test
    void ungroup() throws Exception {
        OrderTable 첫번째빈테이블 = orderTableDao.save(createTable(null, 0, true));
        OrderTable 두번째빈테이블 = orderTableDao.save(createTable(null, 0, true));

        orderDao.save(
                createOrder(첫번째빈테이블.getId(), LocalDateTime.of(2020, 10, 20, 20, 40), OrderStatus.COMPLETION,
                        null));
        orderDao.save(
                createOrder(두번째빈테이블.getId(), LocalDateTime.of(2020, 10, 20, 21, 44), OrderStatus.COMPLETION,
                        null));

        TableGroup 생성된테이블그룹 = tableGroupService.create(
                createTableGroup(null, Lists.list(첫번째빈테이블, 두번째빈테이블)));

        String 테이블그룹해제_API_URL = "/api/table-groups/{tableGroupId}";

        final ResultActions resultActions = deleteByPathId(테이블그룹해제_API_URL, 생성된테이블그룹.getId());

        resultActions
                .andExpect(status().isNoContent());
    }
}