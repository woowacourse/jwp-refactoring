package kitchenpos.tablegroup.request;

import javax.validation.constraints.NotNull;

public class TableGroupUnitDto {

    @NotNull
    private final Long id;

    public TableGroupUnitDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
