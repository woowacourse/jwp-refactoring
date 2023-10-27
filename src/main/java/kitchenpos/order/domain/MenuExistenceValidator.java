package kitchenpos.order.domain;

import java.util.List;

public interface MenuExistenceValidator {

    void validate(final List<Long> menuIds);
}
