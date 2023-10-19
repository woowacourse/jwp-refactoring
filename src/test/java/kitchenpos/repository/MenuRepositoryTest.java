package kitchenpos.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("메뉴를 저장하면 매핑된 객체들도 저장된다.")
    void save() {
        Menu menu = em.find(Menu.class, 1L);
        MenuProduct menuProduct = em.find(MenuProduct.class, 3L);
        em.clear(); // 1차 캐시 삭제

        Long lastMenuProductMenuId = menuProduct.getMenu().getId();
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        System.out.println("=---------------------------");
        menu.setMenuProducts(menuProducts);
        Menu savedMenu = menuRepository.save(menu);
        em.flush(); // save 데이터 베이스 반영
        System.out.println("=---------------------------");

        Long nowMenuProductMenuId = savedMenu.getMenuProducts().get(0).getMenu().getId();

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(lastMenuProductMenuId).isNotEqualTo(menu.getId());
            softAssertions.assertThat(nowMenuProductMenuId).isEqualTo(menu.getId());
        });
    }
}
