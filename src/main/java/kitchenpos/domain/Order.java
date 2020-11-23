package kitchenpos.domain;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long orderTableId;
	@Enumerated(value = EnumType.STRING)
	private OrderStatus orderStatus;
	private LocalDateTime orderedTime;
	@Embedded
	private OrderLineItems orderLineItems;

	protected Order() {
	}

	public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
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

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
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
