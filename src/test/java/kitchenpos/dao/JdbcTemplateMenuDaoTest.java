package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    @DisplayName("menu 하나를 조회한다.")
    @Test
    void findById() {
        // given
        final String name = "후라이드치킨";
        final int price = 16000;
        final Long menuGroupId = 2L;

        // when
        final Optional<Menu> menu = jdbcTemplateMenuDao.findById(1L);

        // then
        assertAll(
                () -> assertThat(menu.isPresent()).isTrue(),
                () -> assertThat(menu.get().getName()).isEqualTo(name),
                () -> assertThat(menu.get().getPrice().intValue()).isEqualTo(price),
                () -> assertThat(menu.get().getMenuGroupId()).isEqualTo(menuGroupId)
        );
    }

    @DisplayName("menu들을 전체 조회한다.")
    @Test
    void findAll() {
        // when
        final List<Menu> menus = jdbcTemplateMenuDao.findAll();
        final List<Long> ids = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        // then
        assertThat(ids).containsExactly(1L, 2L, 3L, 4L, 5L, 6L);
    }
}
