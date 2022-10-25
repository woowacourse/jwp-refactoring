package acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 인수테스트에서")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void getMenuGroups() {
        메뉴_그룹을_생성한다("호호 메뉴");
        메뉴_그룹을_생성한다("베루스 메뉴");
        메뉴_그룹을_생성한다("라라 메뉴");

        List<MenuGroup> menuGroups = 메뉴_그룹을_조회한다();

        assertThat(menuGroups).hasSize(3);
    }
}
