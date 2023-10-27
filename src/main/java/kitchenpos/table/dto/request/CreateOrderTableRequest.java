package kitchenpos.table.dto.request;

import com.sun.istack.NotNull;

public class CreateOrderTableRequest {

    @NotNull
    private Integer numberOfGuest;
    @NotNull
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
