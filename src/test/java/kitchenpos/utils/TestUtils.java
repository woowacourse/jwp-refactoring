package kitchenpos.utils;

import org.springframework.data.jpa.repository.JpaRepository;

public class TestUtils {

    public static <T> T findById(JpaRepository<T, Long> jpaRepository, Long id) {
        return jpaRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
