package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateDto;
import kitchenpos.dto.TableIdDto;

public class TableGroupFixture {

    public static TableGroupCreateDto 테이블그룹_생성_요청(List<Long> tableIds) {
        List<TableIdDto> tableIdDtos = tableIds.stream()
                .map(TableIdDto::new)
                .collect(Collectors.toList());
        return new TableGroupCreateDto(tableIdDtos);
    }

    public static TableGroup 테이블그룹(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}
