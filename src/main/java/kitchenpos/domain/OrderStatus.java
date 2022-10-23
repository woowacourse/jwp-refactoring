package kitchenpos.domain;

/**
 * Order 는 Cooking -> Meal -> completion 순서로 진행된다.
 */
public enum OrderStatus {

    COOKING, MEAL, COMPLETION
}
