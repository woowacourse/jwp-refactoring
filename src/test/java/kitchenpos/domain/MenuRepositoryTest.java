package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    MenuRepository sut;


    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Menu를 저장하면 MenuProduct도 함께 저장한다")
    void save() {
        // given
        MenuProduct menuProduct = new MenuProduct(1L, 1L);
        Menu menu = new Menu("치킨", BigDecimal.valueOf(1000L), 1L);

        // when
        Menu save = sut.save(menu);

        Long id = save.getId();
        var find = sut.findById(id).get();

        save.addMenuProducts(List.of(menuProduct));
        // then

        System.out.println(save);
        System.out.println(find);
    }
}
