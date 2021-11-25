package kitchenpos.ordertable.domain.dto.response;

import kitchenpos.ordertable.domain.TableGroup;

public class TableGroupResponse {

    private Long id;

    private TableGroupResponse(Long id) {
        this.id = id;
    }

    protected TableGroupResponse() {
    }

    public Long getId() {
        return id;
    }

    public static TableGroupResponse toDTO(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId());
    }
}
