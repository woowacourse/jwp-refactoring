package kitchenpos.dto.order;

public class OrderUpdateStatusRequest {
	private String orderStatus;

	protected OrderUpdateStatusRequest() {
	}

	public OrderUpdateStatusRequest(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatus() {
		return orderStatus;
	}
}
