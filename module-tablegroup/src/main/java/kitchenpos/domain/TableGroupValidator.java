package kitchenpos.domain;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    public void validate(int requestSize, List<OrderTable> orderTables) {
        if (requestSize != orderTables.size()) {
            throw new IllegalArgumentException("묶으려는 테이블 중 저장되지 않은 테이블이 존재합니다.");
        }

        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("하나의 테이블 그룹엔 2개 이상의 테이블이 존재해야합니다.");
        }
    }
}
