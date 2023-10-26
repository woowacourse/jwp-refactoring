package kitchenpos.ordertable.controller.dto;

public class OrderTableChangeNumberOfGuests {
    
    private final int numberOfGuests;
    
    public OrderTableChangeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
