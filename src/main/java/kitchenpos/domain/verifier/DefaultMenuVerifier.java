package kitchenpos.domain.verifier;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.Menu;
import kitchenpos.exception.EmptyMenuOrderException;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.repository.MenuRepository;

@Component
public class DefaultMenuVerifier implements MenuVerifier {
    private final MenuRepository menuRepository;

    public DefaultMenuVerifier(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void verifyMenuCount(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new EmptyMenuOrderException();
        }

        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new MenuNotFoundException();
        }

    }
}
