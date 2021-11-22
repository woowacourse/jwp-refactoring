package kitchenpos.exception;

public class UnGroupNotCompletionException extends KitchenPosException {

    private static final String UN_GROUP_NOT_COMPLETION = "계산 완료 상태가 아닌 주문 테이블이 있을 경우 단체 지정을 해제할 수 없습니다.";

    public UnGroupNotCompletionException() {
        super(UN_GROUP_NOT_COMPLETION);
    }
}
