package kitchenpos.menugroup.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupTest {

    @Test
    @DisplayName("MenuGroup 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        Long id = 1L;
        String name = "치킨세트";

        //when
        MenuGroup actual = MenuGroup.create(id, name);

        //then
        assertEquals(actual.getId(), id);
        assertEquals(actual.getName(), name);
    }
}
