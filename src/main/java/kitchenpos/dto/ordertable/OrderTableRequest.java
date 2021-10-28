package kitchenpos.dto.ordertable;

public class OrderTableRequest {

    private final Long id;
    private final Integer numberOfGuests;
    private final Boolean empty;

    public OrderTableRequest(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTableRequest(Long id) {
        this(id, null, null);
    }

    public OrderTableRequest(int numberOfQuests) {
        this(numberOfQuests, null);
    }

    public OrderTableRequest(Boolean empty) {
        this(null, null, empty);
    }

    public OrderTableRequest() {
        this(null, null, null);
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
