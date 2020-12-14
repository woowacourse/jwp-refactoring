package kitchenpos.tablegroup.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.ControllerTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;

class TableGroupRestControllerTest extends ControllerTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("create: 단체 지정 등록 테스트")
    @Test
    void createTest() throws Exception {
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));

        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));

        create("/api/table-groups", tableGroupRequest)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.orderTables[0].id").exists())
                .andExpect(jsonPath("$.orderTables[1].id").exists());
    }

    @DisplayName("delete: 단체 지정 해제 테스트")
    @Test
    void deleteTest() throws Exception {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable orderTable1 = orderTableRepository.save(orderTable);
        final OrderTable orderTable2 = orderTableRepository.save(orderTable);

        final TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));
        final TableGroup saved = tableGroupRepository.save(tableGroup);

        deleteByPathVariable("/api/table-groups/" + saved.getId());

    }
}
