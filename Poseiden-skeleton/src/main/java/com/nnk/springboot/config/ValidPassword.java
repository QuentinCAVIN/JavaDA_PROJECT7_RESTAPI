package com.nnk.springboot.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation personnalisée pour valider les mots de passe.
 * Cette annotation peut être appliquée à des classes, des champs ou des méthodes pour spécifier
 * les critères de validation des mots de passe.
 */
@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    /**
     * Message par défaut en cas de non-validation.
     *
     * @return Le message par défaut.
     */
    String message() default "Invalid Password";

    /**
     * Groupes de validation auxquels cette contrainte appartient.
     * Ici, pas de groupe de validation spécifié par défaut pour cette contrainte = vide.
     *
     * @return Les groupes de validation.
     */
    Class<?>[] groups() default {};

    /**
     * Utilisé pour personnaliser plus en profondeur le système de validation.
     * Vide ici.
     *
     * @return Les objets Payload associé.
     */
    Class<? extends Payload>[] payload() default {};
}