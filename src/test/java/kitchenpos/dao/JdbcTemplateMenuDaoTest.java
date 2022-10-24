package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@Import(JdbcTemplateMenuDao.class)
@JdbcTest
public class JdbcTemplateMenuDaoTest {

    @Autowired
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;

    @DisplayName("menu를 저장한다.")
    @Test
    void save() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(1L, 3);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 3);
        final List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final Menu menu = new Menu("김밥", BigDecimal.valueOf(20000), 2L, menuProducts);

        // when
        final Menu savedMenu = jdbcTemplateMenuDao.save(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
    }
}
