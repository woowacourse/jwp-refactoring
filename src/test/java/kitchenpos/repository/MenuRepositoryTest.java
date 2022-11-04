package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.domain.collection.MenuProducts;
import kitchenpos.domain.entity.Menu;
import kitchenpos.domain.entity.MenuGroup;
import kitchenpos.domain.entity.MenuProduct;
import kitchenpos.domain.entity.Price;
import kitchenpos.domain.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    private Menu menu1;
    private Menu menu2;

    @BeforeEach
    void setUp() {
        Product product = new Product("name", 1000L);

        MenuGroup menuGroup = new MenuGroup(1L, "name");
        menu1 = new Menu("name", menuGroup, new Price(1000L), new Price(product.getPrice()));
        menuRepository.save(menu1);

        menu2 = new Menu("name", menuGroup, new Price(1000L), new Price(product.getPrice()));
        menuRepository.save(menu2);
    }

    @DisplayName("주어지는 메뉴 아이디 중 db에 저장된 개수를 반환한다.")
    @Test
    void countExistMenu() {
        int numberOfExistMenu = menuRepository.countExistMenu(List.of(menu1.getId(), 9999L, menu2.getId()));

        assertThat(numberOfExistMenu).isEqualTo(2);
    }
}
