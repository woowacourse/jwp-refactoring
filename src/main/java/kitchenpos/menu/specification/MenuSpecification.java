package kitchenpos.menu.specification;

import kitchenpos.menu.presentation.dto.request.MenuRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.menu.domain.Menu;
import org.springframework.stereotype.Component;

@Component
public class MenuSpecification {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuSpecification(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateCreate(Menu menu, MenuRequest request) {

        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹의 ID입니다.");
        }

        if (!productRepository.existsByIdIn(request.productIds())) {
            throw new IllegalArgumentException("메뉴상품의 상품ID가 존재하지 않습니다.");
        }

        menu.validate();
    }
}
