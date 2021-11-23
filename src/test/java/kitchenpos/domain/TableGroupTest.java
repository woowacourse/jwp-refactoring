package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    @DisplayName("TableGroup 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        Long id = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();

        //when
        TableGroup tableGroup = TableGroup.create(id, localDateTime);

        //then
        assertEquals(tableGroup.getId(), id);
        assertEquals(tableGroup.getCreatedDate(), localDateTime);
    }
}
