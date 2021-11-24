package kitchenpos.application.dto.request;

public class TableRequestDto {

    private Long numberOfGuests;
    private Boolean empty;

    private TableRequestDto() {
    }

    public TableRequestDto(Long numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
