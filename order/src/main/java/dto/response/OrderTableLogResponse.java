package dto.response;

import domain.OrderTableLog;

public class OrderTableLogResponse {

    private Long id;
    private Long orderTableId;
    private int numberOfGuests;

    public OrderTableLogResponse() {
    }

    private OrderTableLogResponse(Long id, Long orderTableId, int numberOfGuests) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.numberOfGuests = numberOfGuests;
    }

    public static OrderTableLogResponse from(OrderTableLog orderTableLog) {
        Long id = orderTableLog.getId();
        Long orderTableId = orderTableLog.getOrderTableId();
        int numberOfGuests = orderTableLog.getNumberOfGuests();

        return new OrderTableLogResponse(id, orderTableId, numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
