package info.andifalk.author.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Author entity.
 */
@Entity
public class Author extends AbstractPersistable<Long> {

    public static final int MAX_AUTHOR_IDENTIFIER_LENGTH = 50;
    public static final int MAX_AUTHOR_GENDER_LENGTH = 30;
    public static final int MAX_AUTHOR_FIRSTNAME_LENGTH = 50;
    public static final int MAX_AUTHOR_LASTNAME_LENGTH = 50;

    @NotNull
    @Column(nullable = false, length = MAX_AUTHOR_IDENTIFIER_LENGTH)
    private UUID identifier;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = MAX_AUTHOR_GENDER_LENGTH)
    private Gender gender;

    @NotNull
    @Size(min = 1, max = MAX_AUTHOR_FIRSTNAME_LENGTH)
    @Column(nullable = false, length = MAX_AUTHOR_FIRSTNAME_LENGTH)
    private String firstname;

    @NotNull
    @Size(min = 1, max = MAX_AUTHOR_LASTNAME_LENGTH)
    @Column(nullable = false, length = MAX_AUTHOR_LASTNAME_LENGTH)
    private String lastname;



}
