package info.andifalk.common;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, PARAMETER, CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = info.andifalk.common.StringEnumerationValidator.class)
public @interface StringEnumeration {

    String message() default "{info.andifalk.common.StringEnumeration.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    Class<? extends Enum<?>> enumClass();

    String enumValues() default "";

}
