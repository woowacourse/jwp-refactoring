package kitchenpos.domain;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long orderTableId;
	private String orderStatus;
	private LocalDateTime orderedTime;
	@Embedded
	private OrderLineItems orderLineItems;

	protected Order() {
	}

	public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
		OrderLineItems orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public Long getId() {
		return id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public OrderLineItems getOrderLineItems() {
		return orderLineItems;
	}

	public void setOrderLineItems(OrderLineItems orderLineItems) {
		this.orderLineItems = orderLineItems;
	}
}
