package kitchenpos.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

public class TableGroupCreateRequest {

    @UniqueElements(message = "중복되지 않는 테이블 Id들을 입력해주세요.")
    @Size(min = 2, message = "테이블의 수는 최소 2개 이상이어야 합니다.")
    @NotNull(message = "테이블 id를 입력해 주세요.")
    private List<Long> tableIds;

    protected TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
