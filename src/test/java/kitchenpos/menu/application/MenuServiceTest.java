package kitchenpos.menu.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void Menu를_생성할_수_있다() {
        //given
        final Product product = productRepository.save(new Product("디노 탕후루", new BigDecimal(4000)));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("탕후루"));

        //when
        final Long menuId = menuService.create("디노 세트", new BigDecimal(8000), menuGroup.getId(),
                List.of(product.getId()), List.of(2));

        //then
        assertThat(menuId).isNotNull();
    }

    @Test
    void menuGroup이_존재하지_않으면_예외가_발생한다() {
        //when, then
        assertThatThrownBy(() -> menuService.create("디노 세트", new BigDecimal(20000), 987654321L, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @Test
    void 메뉴_가격이_메뉴_상품의_가격_합계를_초과하면_예외가_발생한다() {
        //given
        final Product product = productRepository.save(new Product("디노 탕후루", new BigDecimal(4000)));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("탕후루"));

        //when, then
        assertThatThrownBy(() -> menuService.create("디노 세트", new BigDecimal(9000), menuGroup.getId(),
                List.of(product.getId()), List.of(2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 금액이 총 상품 금액보다 클 수 없습니다.");
    }

    @Test
    void Menu를_조회할_수_있다() {
        //given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("탕후루"));
        final Menu menu = new Menu("디노 세트", new MenuPrice(new BigDecimal(8000)), menuGroup.getId());
        menuRepository.save(menu);

        //when
        final List<Menu> list = menuService.list();

        //then
        assertThat(list).hasSize(1);
    }
}
