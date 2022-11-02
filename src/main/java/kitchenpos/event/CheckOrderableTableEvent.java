package kitchenpos.event;

public class CheckOrderableTableEvent {

    private final Long id;

    public CheckOrderableTableEvent(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
