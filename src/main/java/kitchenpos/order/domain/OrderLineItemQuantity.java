package kitchenpos.order.domain;

import javax.persistence.Embeddable;

import kitchenpos.common.CreateFailException;

@Embeddable
public class OrderLineItemQuantity {

    private Long quantity;

    public OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(Long quantity) {
        if (quantity < 1) {
            throw new CreateFailException("잘못된 수량을 입력하셨습니다. quantity = " + quantity);
        }
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }
}
