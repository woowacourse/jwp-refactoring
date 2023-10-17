package kitchenpos.ui.dto.table;

public class UpdateTableGuestRequest {

    private final Integer guests;

    public UpdateTableGuestRequest(final Integer guests) {
        this.guests = guests;
    }

    public Integer getGuests() {
        return guests;
    }
}
