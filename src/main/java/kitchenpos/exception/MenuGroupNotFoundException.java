package kitchenpos.exception;

public class MenuGroupNotFoundException extends BusinessException {
    public MenuGroupNotFoundException(Long menuGroupId) {
        super(String.format("%d menu group is not exits.", menuGroupId));
    }
}
