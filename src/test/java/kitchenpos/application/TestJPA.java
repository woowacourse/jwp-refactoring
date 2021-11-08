package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@Disabled("테스트용")
@DataJpaTest
public class TestJPA {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuProductRepository menuProductRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void name() {
        System.out.println("============================");

        Menu menu = menuRepository.findById(1L).get();
        final List<MenuProduct> all = menuProductRepository.findAll();
        all.forEach(menuProduct -> menuProduct.updateMenu(menu));
        menu.updateMenuProducts(all);

        testEntityManager.flush();
        testEntityManager.clear();

        System.out.println("============================");

        Menu savedMenu = menuRepository.findById(1L).get();
        final List<Long> menuIds = savedMenu.getMenuProducts().stream()
                .map(products -> products.getId())
                .collect(Collectors.toList());

        assertThat(menuIds).hasSize(6);
    }
}
