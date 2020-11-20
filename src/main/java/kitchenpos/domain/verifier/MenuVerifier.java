package kitchenpos.domain.verifier;

import java.util.List;

public interface MenuVerifier {
    void verifyMenuCount(List<Long> menuIds);
}
