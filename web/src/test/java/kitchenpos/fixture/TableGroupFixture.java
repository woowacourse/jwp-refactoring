package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.application.dto.TableGroupCreateDto;
import kitchenpos.tablegroup.application.dto.TableIdDto;

public class TableGroupFixture {

    public static TableGroupCreateDto 테이블그룹_생성_요청(List<Long> tableIds) {
        List<TableIdDto> tableIdDtos = tableIds.stream()
                .map(TableIdDto::new)
                .collect(Collectors.toList());
        return new TableGroupCreateDto(tableIdDtos);
    }

    public static TableGroup 테이블그룹() {
        return new TableGroup();
    }
}