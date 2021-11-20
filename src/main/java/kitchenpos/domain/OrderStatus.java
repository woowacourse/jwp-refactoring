package kitchenpos.domain;

import kitchenpos.exception.InvalidOrderStatusException;

public enum OrderStatus {
    COOKING,
    MEAL,
    COMPLETION;

    public static OrderStatus findByName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException(String.format("%s 이름의 주문 상태는 존재하지 않습니다.", name));
        }
    }
}
