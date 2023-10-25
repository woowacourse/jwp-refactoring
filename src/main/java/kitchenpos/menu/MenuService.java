package kitchenpos.menu;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuRepository menuRepository
    ) {
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
