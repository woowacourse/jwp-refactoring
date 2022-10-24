package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A"));

        //when
        Menu savedMenu = menuService.create(new Menu("신메뉴", BigDecimal.ZERO, menuGroup.getId(), List.of()));

        //then
        assertAll(
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getPrice().compareTo(BigDecimal.ZERO)).isZero(),
                () -> assertThat(savedMenu.getMenuProducts()).isEmpty()
        );
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        //given
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A"));
        menuService.create(new Menu("신메뉴", BigDecimal.ZERO, menuGroup.getId(), List.of()));

        //when
        //then
        assertThat(menuService.list()).hasSize(1);
    }
}
