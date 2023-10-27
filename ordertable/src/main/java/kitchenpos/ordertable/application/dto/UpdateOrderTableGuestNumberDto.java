package kitchenpos.ordertable.application.dto;

public class UpdateOrderTableGuestNumberDto {

    private Long id;
    private Integer numberOfGuests;

    public UpdateOrderTableGuestNumberDto() {
    }

    public UpdateOrderTableGuestNumberDto(Long id, Integer numberOfGuests) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
