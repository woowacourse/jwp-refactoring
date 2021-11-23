package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.request.OrderTableRequest;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.ui.dto.response.TableGroupResponse;
import org.springframework.stereotype.Component;

@Component
public class TableGroupFixture {

    private final TableGroupRepository tableGroupRepository;

    public TableGroupFixture(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupCreateRequest 테이블_그룹_생성_요청(List<OrderTableRequest> orderTableRequests) {
        return new TableGroupCreateRequest(orderTableRequests);
    }

    public TableGroup 테이블_그룹_조회(Long id) {
        return tableGroupRepository.getOne(id);
    }

    public List<TableGroupResponse> 테이블_그룹_응답_리스트_생성(TableGroupResponse... tableGroupResponses) {
        return Arrays.asList(tableGroupResponses);
    }
}
