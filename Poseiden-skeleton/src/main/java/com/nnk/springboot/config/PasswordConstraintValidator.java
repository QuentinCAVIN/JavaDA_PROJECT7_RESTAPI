package com.nnk.springboot.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;
/**
 * Un validateur de contrainte personnalisé pour les mots de passe.
 * Cette classe implémente la logique de validation pour s'assurer que les mots de passe
 * respectent les critères spécifiés dans l'annotation {@code ValidPassword}.
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    /**
     * Initialise le validateur.
     * Cette méthode est appelée quand le programme rencontre l'annotation personnalisée.
     *
     * @param arg0 L'annotation {@code ValidPassword} utilisée sur le mot de passe.
     */
    @Override
    public void initialize(ValidPassword arg0) {
    }

    /**
     * Valide un mot de passe en fonction des critères définis:
     * Entre 8 et 30 caractères dont une majuscule, un chiffre et un caractère spécial
     *
     *
     * @param password Le mot de passe à valider.
     * @param context  l'objet ConstraintValidatorContext créé par Jakarta et utilisé
     *                pour construire un message d'erreur
     * @return {@code true} si le mot de passe est valide, sinon {@code false}.
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(

                new CharacterRule(EnglishCharacterData.UpperCase,1),
                new LengthRule(8,30),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1)));

        // Valide le mot de passe en utilisant le validateur
        final RuleResult result = validator.validate(new PasswordData(password));

        // Retourne vrai si le mot de passe est valide, sinon faux
        if (result.isValid()) {
            return true;
        }
        return false;
    }
}