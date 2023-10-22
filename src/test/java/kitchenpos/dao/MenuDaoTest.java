package kitchenpos.dao;

import kitchenpos.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class MenuDaoTest {
    @Autowired
    private DataSource dataSource;
    private MenuDao menuDao;

    @BeforeEach
    void setUp() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
    }

    @Test
    @DisplayName("메뉴를 저장한다.")
    public void save() {
        //given
        kitchenpos.domain.Menu menu = new kitchenpos.domain.Menu();
        menu.setName("menu1");
        menu.setPrice(Money.valueOf(new BigDecimal("100.00")));
        menu.setMenuGroupId(1L);

        //when
        kitchenpos.domain.Menu returnedMenu = menuDao.save(menu);

        //then
        assertThat(returnedMenu.getId()).isNotNull();
        assertThat(menu.getName()).isEqualTo(returnedMenu.getName());
        assertThat(menu.getPrice()).isEqualTo(returnedMenu.getPrice());
        assertThat(menu.getMenuGroupId()).isEqualTo(returnedMenu.getMenuGroupId());
    }

    @Test
    @DisplayName("메뉴를 조회한다.")
    public void findById() {
        //given
        final long id = 1L;

        //when
        kitchenpos.domain.Menu result = menuDao.findById(id).orElse(null);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    public void findAll() {
        //given

        //when
        List<kitchenpos.domain.Menu> result = menuDao.findAll();

        //then
        assertThat(result).isNotNull().isNotEmpty();
    }
}
