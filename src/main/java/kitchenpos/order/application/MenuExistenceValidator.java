package kitchenpos.order.application;

import java.util.List;

public interface MenuExistenceValidator {

    void validate(final List<Long> menuIds);
}
