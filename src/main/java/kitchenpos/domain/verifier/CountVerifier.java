package kitchenpos.domain.verifier;

import java.util.List;

public interface CountVerifier<T> {
    List<T> verify(List<Long> ids);
}
