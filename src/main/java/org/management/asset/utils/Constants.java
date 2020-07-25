package org.management.asset.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
public class Constants {

    public static final String STRING_SEPARATOR = ";";
    public static final List<String> IMAGE_CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/gif");
    public static final String ERROR = "Une erreur est survenue, veuillez ressayer!";
    public static final String INVALID_USER_IMAGE = "Image utilisateur non valide!";
    public static final String INVALID_TOKEN = "Token n'appartient à aucun utilisateur!";
    public static final String EXPIRED_TOKEN = "Token est expiré!";
    public static final String EMAIL_NOT_FOUND = "Email n'appartient à aucun utilisateur ou adresse email n'est pas encore activée!";
    public static final String EMAIL_UPDATED = "Email a été mis à jour avec succès, veuillez confirmer votre nouvel e-mail via le courrier reçu";
    public static final String EMAIL_ALREADY_USED = "L'adresse Email est déja utilisée!";
    public static final String EMPLOYEE_NUMBER_ALREADY_USED = "Numéro d'employée est déja utilisé!";
    public static final String PASSWORD_RESET_EMAIL_SENT = "Email de réinitialisation du mot de passe est envoyé vers votre courrier avec succés";
    public static final String ACCOUNT_NOT_ACTIVE = "Compte n'est pas encore activé!";
    public static final String ACCOUNT_ALREADY_ACTIVE = "Compté est déja activé";
    public static final String ACCOUNT_ACTIVATED_SUCCESSFULLY = "Compte est activé avec succés";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Mot de passe est mis à jour avec succés";
    public static final String VALID_TOKEN = "Token est invalid!";

    public static final String ORGANIZATION_NAME_ALREADY_USER = "Le nom d'organisme est déja utilisé!";
    public static final String INVALID_ORGANIZATION_IMAGE = "Image d'organisme non valide!";

    public static final String GROUP_NAME_ALREADY_TAKEN = "Nom du groupe est déja utilisé!";

    public static final String PROCESS_NAME_ALREADY_TAKEN = "Nom du processus est déja utilisé pour cette le même organisme!";

    private Constants() {}

}
