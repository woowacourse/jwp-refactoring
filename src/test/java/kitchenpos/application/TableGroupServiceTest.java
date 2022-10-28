package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.TableGroupCreationDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableIdDto;
import kitchenpos.ui.dto.request.TableGroupCreationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("TableGroupService 는 ")
@SpringTestWithData
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void createTableGroup() {
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(0, true));
        final TableGroupCreationRequest tableGroupCreationRequest = new TableGroupCreationRequest(
                List.of(new OrderTableIdDto(orderTable1.getId()), new OrderTableIdDto(orderTable2.getId())));
        final TableGroupCreationDto tableGroupCreationDto = TableGroupCreationDto.from(tableGroupCreationRequest);

        final TableGroupDto tableGroupDto = tableGroupService.create(tableGroupCreationDto);

        assertThat(tableGroupDto.getOrderTables().size()).isEqualTo(2);
    }
}
