package info.andifalk.common;

import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

import java.util.UUID;

/**
 * Implementation of {@link IdGenerator}.
 */
@Component
public class RandomUuidGenerator implements IdGenerator {

    @Override
    public UUID generateId() {
        return UUID.randomUUID();
    }
}
