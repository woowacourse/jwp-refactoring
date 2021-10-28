package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("단체 지정 서비스 테스트")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;


    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        TableGroupResponse response = tableGroupService.create(request);

        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getOrderTables()).isNotEmpty()
        );
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        TableGroupResponse response = tableGroupService.create(request);

        tableGroupService.ungroup(response.getId());

        boolean actual = orderTableRepository.findAll().stream()
            .allMatch(table -> Objects.isNull(table.getTableGroup()));
        assertThat(actual).isTrue();
    }
}
