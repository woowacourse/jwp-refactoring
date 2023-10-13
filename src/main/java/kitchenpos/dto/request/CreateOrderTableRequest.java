package kitchenpos.dto.request;

public class CreateOrderTableRequest {

    private Integer numberOfGuest;
    private Boolean empty;

    public CreateOrderTableRequest(Integer numberOfGuest, Boolean empty) {
        this.numberOfGuest = numberOfGuest;
        this.empty = empty;
    }

    public Integer getNumberOfGuest() {
        return numberOfGuest;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
