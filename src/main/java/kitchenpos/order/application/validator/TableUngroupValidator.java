package kitchenpos.order.application.validator;

import kitchenpos.order.domain.TableGroup;

public interface TableUngroupValidator {

    void validate(final TableGroup tableGroup);
}
