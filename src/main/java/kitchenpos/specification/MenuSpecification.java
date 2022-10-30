package kitchenpos.specification;

import kitchenpos.dao.jpa.MenuGroupRepository;
import kitchenpos.dao.jpa.ProductRepository;
import kitchenpos.domain.Menu;
import org.springframework.stereotype.Component;

@Component
public class MenuSpecification {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuSpecification(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateCreate(Menu menu) {

        if (!menuGroupRepository.existsBy(menu.getMenuGroup())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹의 ID입니다.");
        }

        if (!productRepository.existsByIn(menu.products())) {
            throw new IllegalArgumentException("메뉴상품의 상품ID가 존재하지 않습니다.");
        }
    }
}
