package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import kitchenpos.domain.Menu;

@JdbcTest
@Import(JdbcTemplateMenuDao.class)
class JdbcTemplateMenuDaoTest {
    @Autowired
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;

    @DisplayName("MenuDao save 테스트")
    @Test
    void save() {
        // Given
        final Menu menu = new Menu();
        menu.setName("파닭치킨");
        menu.setPrice(BigDecimal.valueOf(18000L));
        menu.setMenuGroupId(1L);

        // When
        final Menu savedMenu = jdbcTemplateMenuDao.save(menu);

        // Then
        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull()
                ,
                () -> assertThat(savedMenu)
                        .extracting(Menu::getName)
                        .isEqualTo(menu.getName())
                ,
                () -> assertThat(savedMenu)
                        .extracting(Menu::getPrice)
                        .extracting(BigDecimal::longValue)
                        .isEqualTo(menu.getPrice().longValue())
                ,
                () -> assertThat(savedMenu)
                        .extracting(Menu::getMenuGroupId)
                        .isEqualTo(menu.getMenuGroupId())
        );
    }

    @DisplayName("MenuDao findById 테스트")
    @Test
    void findById() {
        // When
        final Menu savedMenu = jdbcTemplateMenuDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        // Then
        assertAll(
                () -> assertThat(savedMenu)
                        .extracting(Menu::getName)
                        .isEqualTo("후라이드치킨")
                ,
                () -> assertThat(savedMenu)
                        .extracting(Menu::getPrice)
                        .extracting(BigDecimal::longValue)
                        .isEqualTo(16000L)
                ,
                () -> assertThat(savedMenu)
                        .extracting(Menu::getMenuGroupId)
                        .isEqualTo(2L)
        );
    }

    @DisplayName("MenuDao findAll 테스트")
    @Test
    void findAll() {
        // When
        final List<Menu> menus = jdbcTemplateMenuDao.findAll();

        // Then
        assertThat(menus).hasSize(6);
    }

    @DisplayName("MenuDao countByIdIn 테스트")
    @Test
    void countByIdIn() {
        // When
        final long count = jdbcTemplateMenuDao.countByIdIn(newArrayList(1L, 2L, 3L, 4L));

        // Then
        assertThat(count).isEqualTo(4L);
    }
}
