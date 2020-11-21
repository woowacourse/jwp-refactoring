package kitchenpos.domain.verifier;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.Menu;
import kitchenpos.exception.EmptyMenuOrderException;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.repository.MenuRepository;

@Component
public class MenuCountVerifier implements CountVerifier<Menu> {
    private final MenuRepository menuRepository;

    public MenuCountVerifier(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> verify(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new EmptyMenuOrderException();
        }

        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new MenuNotFoundException();
        }
        return menus;
    }
}
