package info.andifalk.common;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class StringEnumerationValidator implements ConstraintValidator<StringEnumeration, Enum<?>> {

    private Set<String> AVAILABLE_ENUM_NAMES;

    private Set<String> getNamesSet(Class<? extends Enum<?>> e) {
        Enum<?>[] enums = e.getEnumConstants();
        String[] names = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            names[i] = enums[i].name();
        }
        return new HashSet<>(Arrays.asList(names));
    }

    @Override
    public void initialize(StringEnumeration stringEnumeration) {
        Class<? extends Enum<?>> enumSelected = stringEnumeration.enumClass();
        AVAILABLE_ENUM_NAMES = getNamesSet(enumSelected);
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        return value == null || AVAILABLE_ENUM_NAMES.contains(value.name());
    }
}
