package kitchenpos.application.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableCreationRequest;

public class OrderTableCreationDto {

    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableCreationDto(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableCreationDto from(final OrderTableCreationRequest orderTableCreationRequest) {
        return new OrderTableCreationDto(orderTableCreationRequest.getNumberOfGuests(), orderTableCreationRequest.isEmpty());
    }

    public static OrderTable toEntity(final OrderTableCreationDto orderTableCreationDto) {
        return new OrderTable(orderTableCreationDto.getNumberOfGuests(), orderTableCreationDto.isEmpty());
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public String toString() {
        return "OrderTableCreationDto{" +
                "numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
