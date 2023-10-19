package kitchenpos.fixture;

import java.time.LocalDateTime;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 테이블_그룹1() {
        return new TableGroup(1L, LocalDateTime.now());
    }
}
