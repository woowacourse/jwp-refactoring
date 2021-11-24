package kitchenpos.fixture;

import java.util.List;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.testtool.RequestBuilder;
import kitchenpos.testtool.response.HttpResponse;
import kitchenpos.table.ui.dto.request.OrderTableRequest;
import kitchenpos.table.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.table.ui.dto.response.TableGroupResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TableGroupFixture extends DefaultFixture {

    private final TableGroupRepository tableGroupRepository;

    public TableGroupFixture(RequestBuilder requestBuilder,
            TableGroupRepository tableGroupRepository) {
        super(requestBuilder);
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupCreateRequest 테이블_그룹_생성_요청(List<OrderTableRequest> orderTableRequests) {
        return new TableGroupCreateRequest(orderTableRequests);
    }

    public TableGroup 테이블_그룹_조회(Long id) {
        return tableGroupRepository.getOne(id);
    }

    public TableGroupResponse 테이블_그룹_등록(TableGroupCreateRequest request) {
        return request()
                .post("/api/table-groups", request)
                .build()
                .convertBody(TableGroupResponse.class);
    }

    public HttpResponse 테이블_그룹_해제(Long tableGroupId) {
        return request()
                .delete("/api/table-groups/" + tableGroupId)
                .build();
    }
}
