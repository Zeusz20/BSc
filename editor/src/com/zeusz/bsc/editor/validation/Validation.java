package com.zeusz.bsc.editor.validation;

import com.zeusz.bsc.core.*;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.editor.Editor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;


public class Validation {

    /* Validations */
    public static Validation validate(GWObject object) {
        Validation result = new Validation();

        if(object instanceof Project)
            return validateProject((Project) object);

        if(object instanceof Object)
            return validateObject((Object) object);

        if(object instanceof Attribute)
            return validateAttribute((Attribute) object);

        if(object instanceof Question)
            return validateQuestion((Question) object);

        return result;
    }

    private static Validation validateProject(Project project) {
        List<Object> objects = project.getItemList(Object.class);
        List<Attribute> attributes = project.getItemList(Attribute.class);
        List<Question> questions = project.getItemList(Question.class);

        Predicate<List<? extends Item>> areItemsValid = items -> {
            // map validation to items, then AND them all together
            return items.stream().map(Validation::validate).map(Validation::isValid).reduce(true, (acc, it) -> acc & it);
        };

        /* BEGIN validation */
        Validation validation = new Validation();

        // project has at least 2 objects
        if(objects.size() < 2)
            validation.getErrors().add(Localization.localize("error.few_objects"));

        // project has at least 1 attribute
        if(attributes.size() < 1)
            validation.getErrors().add(Localization.localize("error.few_attributes"));

        // each attribute is assigned to a question (1 to n)
        for(Attribute attribute: attributes) {
            int i;
            for(i = 0; i < questions.size(); i++) {
                if(attribute.equals(questions.get(i).getAttribute()))
                    break;
            }

            if(i == questions.size()) {
                validation.getErrors().add(Localization.localize("error.unused_attributes"));
                break;
            }
        }

        // validate items
        if(!(areItemsValid.test(objects) && areItemsValid.test(attributes) && areItemsValid.test(questions)))
            validation.getErrors().add(Localization.localize("error.invalid_items"));

        project.valid = validation.isValid();
        return validation;
    }

    private static Validation validateItem(Item item) {
        Validation validation = new Validation();

        // name cannot be empty
        if(item.getName().matches("\\s*"))
            validation.getErrors().add(Localization.localize("error.invalid_name"));

        // name must be unique
        Optional<String> name = Editor.getInstance().getProject().getItemList(item.getClass()).stream()
                .filter(it -> !item.equals(it))
                .map(Item::getName)
                .filter(it -> item.getName().equals(it))
                .findAny();

        if(name.isPresent())
            validation.getErrors().add(Localization.localize("error.not_unique_name"));

        return validation;
    }

    private static Validation validateObject(Object object) {
        Validation validation = validateItem(object);

        // object has to have an image
        if(object.getImage() == null)
            validation.getErrors().add(Localization.localize("error.missing_image"));

        return validation;
    }

    private static Validation validateAttribute(Attribute attribute) {
        Validation validation = validateItem(attribute);

        // attribute has to have at least 1 value
        if(attribute.getValues().isEmpty())
            validation.getErrors().add(Localization.localize("error.no_values"));

        // values cannot be empty
        if(attribute.getValues().stream().anyMatch(it -> it.matches("\\s*")))
            validation.getErrors().add(Localization.localize("error.missing_value"));

        return validation;
    }

    private static Validation validateQuestion(Question question) {
        Validation validation = validateItem(question);

        if(!question.getText().contains("{$attr}"))
            validation.getErrors().add(Localization.localize("error.missing_attr_reference"));

        if(question.getAttribute() == null)
            validation.getErrors().add(Localization.localize("error.missing_attribute"));

        return validation;
    }

    /* Class methods and fields */
    private Set<String> errors;

    public Validation() { errors = new TreeSet<>(); }

    public boolean isValid() { return errors.isEmpty(); }

    public Set<String> getErrors() { return errors; }

}
