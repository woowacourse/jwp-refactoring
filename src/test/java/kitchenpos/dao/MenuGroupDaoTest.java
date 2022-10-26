package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("MenuGroup 은 ")
@SpringBootTest
class MenuGroupDaoTest {

   @Autowired
   private MenuGroupDao menuGroupDao;

   @DisplayName("MenuGroup을 저장한다.")
   @Test
   void saveMenuGroup() {
      final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroupName"));

      assertAll(
              () -> assertThat(menuGroup.getId()).isGreaterThanOrEqualTo(1L),
              () -> assertThat(menuGroup.getName()).isEqualTo("menuGroupName")
      );
   }
}
