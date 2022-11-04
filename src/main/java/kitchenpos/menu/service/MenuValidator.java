package kitchenpos.menu.service;

import kitchenpos.menu.domain.Menu;

@FunctionalInterface
public interface MenuValidator {

    void validateInMenuGroup(final Menu menu);
}
