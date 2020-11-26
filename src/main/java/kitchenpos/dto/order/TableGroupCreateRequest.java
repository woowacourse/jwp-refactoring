package kitchenpos.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TableGroupCreateRequest {
    @Size(min = 2, message = "테이블의 개수는 2개 이상이어야 합니다.")
    @Valid
    private List<OrderTableRequest> orderTables;
}
