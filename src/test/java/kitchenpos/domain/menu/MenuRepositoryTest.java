package kitchenpos.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
@DataJpaTest
public class MenuRepositoryTest {
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final String 메뉴_이름_후라이드_치킨 = "후라이드 치킨";
    private static final String 메뉴_이름_양념_치킨 = "양념 치킨";
    private static final BigDecimal 메뉴_가격_16000원 = new BigDecimal("16000.0");

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("Menu를 DB에 저장할 경우, 올바르게 수행된다.")
    @Test
    void saveTest() {
        MenuGroup menuGroup = new MenuGroup(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
        Menu menu = new Menu(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup);

        menuGroupRepository.save(menuGroup);
        Menu savedMenu = menuRepository.save(menu);
        Long size = menuRepository.count();

        assertThat(size).isEqualTo(1L);
        assertThat(savedMenu.getId()).isEqualTo(1L);
        assertThat(savedMenu.getName()).isEqualTo(메뉴_이름_후라이드_치킨);
        assertThat(savedMenu.getPrice()).isEqualTo(메뉴_가격_16000원);
        assertThat(savedMenu.getMenuGroup().getId()).isEqualTo(메뉴_그룹_ID_1);
        assertThat(savedMenu.getMenuGroup().getName()).isEqualTo(메뉴_그룹_이름_후라이드_세트);
    }

    @DisplayName("Menu의 목록 조회를 요청할 경우, 올바르게 수행된다.")
    @Test
    void findAllTest() {
        MenuGroup menuGroup = new MenuGroup(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
        Menu menu1 = new Menu(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup);
        Menu menu2 = new Menu(메뉴_이름_양념_치킨, 메뉴_가격_16000원, menuGroup);
        menuGroupRepository.save(menuGroup);
        menuRepository.save(menu1);
        menuRepository.save(menu2);

        List<Menu> menus = menuRepository.findAll();

        assertThat(menus).hasSize(2);
        assertThat(menus.get(0).getId()).isEqualTo(1L);
        assertThat(menus.get(0).getName()).isEqualTo(메뉴_이름_후라이드_치킨);
        assertThat(menus.get(1).getId()).isEqualTo(2L);
        assertThat(menus.get(1).getName()).isEqualTo(메뉴_이름_양념_치킨);
    }
}
