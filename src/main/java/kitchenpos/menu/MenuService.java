package kitchenpos.menu;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuCreateValidator menuCreateValidator;

    public MenuService(
            final MenuRepository menuRepository,
            MenuCreateValidator menuCreateValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuCreateValidator = menuCreateValidator;
    }

    @Transactional
    public Menu create(Menu menu) {
        return menuRepository.save(Menu.createWithValidation(menu, menuCreateValidator));
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
