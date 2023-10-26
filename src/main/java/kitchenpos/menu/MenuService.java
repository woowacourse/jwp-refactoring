package kitchenpos.menu;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuCreateValidator menuCreateValidator;
    private final MenuProductPriceMultiplier menuProductPriceMultiplier;

    public MenuService(
            final MenuRepository menuRepository,
            MenuCreateValidator menuCreateValidator,
            MenuProductPriceMultiplier menuProductPriceMultiplier
    ) {
        this.menuRepository = menuRepository;
        this.menuCreateValidator = menuCreateValidator;
        this.menuProductPriceMultiplier = menuProductPriceMultiplier;
    }

    @Transactional
    public Menu create(Menu menu) {
        menuCreateValidator.validate(menu);
        menu.validateMenuProductsPrice(menuProductPriceMultiplier);

        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
