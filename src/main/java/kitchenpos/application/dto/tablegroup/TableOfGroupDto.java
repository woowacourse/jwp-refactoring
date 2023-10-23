package kitchenpos.application.dto.tablegroup;

import kitchenpos.domain.OrderTable;

public class TableOfGroupDto {

    private Long id;

    private TableOfGroupDto() {}

    public TableOfGroupDto(final Long id) {
        this.id = id;
    }

    public static TableOfGroupDto from(final OrderTable table) {
        return new TableOfGroupDto(table.getId());
    }

    public Long getId() {
        return id;
    }
}
