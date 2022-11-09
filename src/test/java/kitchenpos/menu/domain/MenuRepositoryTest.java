package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("메뉴를 저장한다")
    void save() {
        // given
        final MenuPrice menuPrice = new MenuPrice(new BigDecimal(3000));
        final Menu menu = new Menu(null, "치킨치킨", menuPrice, 1L);

        // when
        final Menu saved = menuRepository.save(menu);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo("치킨치킨"),
                () -> assertThat(saved.getPrice()).isEqualByComparingTo(new BigDecimal(3000)),
                () -> assertThat(saved.getMenuGroupId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("id로 메뉴를 조회한다")
    void findById() {
        // given
        final MenuPrice menuPrice = new MenuPrice(new BigDecimal(3000));
        final Menu menu = new Menu(null, "치킨치킨", menuPrice, 1L);
        final Menu saved = menuRepository.save(menu);

        // when
        final Menu foundMenu = menuRepository.findById(saved.getId())
                .get();

        // then
        assertThat(foundMenu).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("id로 메뉴를 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<Menu> menu = menuRepository.findById(-1L);

        // then
        assertThat(menu).isEmpty();
    }

    @Test
    @DisplayName("모든 메뉴를 조회한다")
    void findByAll() {
        // given
        final MenuPrice menuPrice = new MenuPrice(new BigDecimal(3000));
        final Menu menu = new Menu(null, "치킨치킨", menuPrice, 1L);
        final Menu saved = menuRepository.save(menu);

        // when
        final List<Menu> menus = menuRepository.findAll();

        // then
        assertAll(
                () -> assertThat(menus).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(menus).extracting("id")
                        .contains(saved.getId())
        );
    }

    @Test
    @DisplayName("id에 일치하는 메뉴들의 개수를 반환한다")
    void countByIdIn() {
        // given
        final MenuPrice menuPrice = new MenuPrice(new BigDecimal(3000));
        final Menu menu = new Menu(null, "치킨치킨", menuPrice, 1L);
        final Menu saved = menuRepository.save(menu);

        // when
        final long count = menuRepository.countByIdIn(Collections.singletonList(saved.getId()));

        // then
        assertThat(count).isEqualTo(1);
    }
}
