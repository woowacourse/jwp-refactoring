package kitchenpos.application.dto;

import javax.persistence.criteria.CriteriaBuilder.In;

public class OrderTableChangeNumberOfGuestsRequest {

    private Integer numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
