package kitchenpos.dto.table;

public class OrderTableUpdateNumberOfGuestsRequest {
	private int numberOfGuests;

	protected OrderTableUpdateNumberOfGuestsRequest() {
	}

	public OrderTableUpdateNumberOfGuestsRequest(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}
}
