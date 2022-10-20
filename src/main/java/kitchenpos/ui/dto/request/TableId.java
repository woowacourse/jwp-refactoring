package kitchenpos.ui.dto.request;

public class TableId {
    private Long id;

    public TableId() {
    }

    public TableId(final Long id) {
        this.id = id;
    }

    public static TableId from(final Long id) {
        return new TableId(id);
    }

    public Long getId() {
        return id;
    }
}
