package kitchenpos.application.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

//    @NotEmpty(message = "단체 지정할 테이블을 지정해 주세요.")
//    @Size(min = 2, message = "두 개 이상의 테이블부터 단체 지정할 수 있습니다.")
    private final List<TableIdRequest> tableIdRequests;

    public TableGroupRequest(final List<TableIdRequest> tableIdRequests) {
        this.tableIdRequests = tableIdRequests;
    }

    public List<Long> getOrderTableIds() {
        return tableIdRequests.stream()
                .map(TableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
