package kitchenpos.application.dto.tablegroup;

public class TableOfGroupDto {

    private Long id;

    private TableOfGroupDto() {}

    public TableOfGroupDto(final Long id) {
        this.id = id;
    }

    public static TableOfGroupDto from(final Long orderTableId) {
        return new TableOfGroupDto(orderTableId);
    }

    public Long getId() {
        return id;
    }
}
