package seedu.tutorPal.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.tutorPal.commons.exceptions.IllegalValueException;
import seedu.tutorPal.model.person.Class;

/**
 * Jackson-friendly version of {@link Class}.
 */
class JsonAdaptedClass {

    private final String className;

    /**
     * Constructs a {@code JsonAdaptedClass} with the given {@code className}.
     */
    @JsonCreator
    public JsonAdaptedClass(String className) {
        this.className = className;
    }

    /**
     * Converts a given {@code Class} into this class for Jackson use.
     */
    public JsonAdaptedClass(Class source) {
        className = source.value;
    }

    @JsonValue
    public String getClassName() {
        return className;
    }

    /**
     * Converts this Jackson-friendly adapted class object into the model's {@code Class} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted class.
     */
    public Class toModelType() throws IllegalValueException {
        if (!Class.isValidClass(className)) {
            throw new IllegalValueException(Class.MESSAGE_CONSTRAINTS);
        }
        return new Class(className);
    }

}
