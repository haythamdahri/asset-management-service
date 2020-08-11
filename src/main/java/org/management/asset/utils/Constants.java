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

    public static final String PROCESS_NAME_ALREADY_TAKEN = "Nom du processus est déja utilisé pour l'organisme selectionné!";
    public static final String PARENT_PROCESS_IS_ALREADY_ASSIGNED_TO_SAME_PROCESS = "Le processus superieur à déja ce processus comme superieur!";

    public static final String NO_THREAT_FOUND = "Aucune menace n'a été trouvée!";
    public static final String NO_VULENRABILITY_FOUND = "Aucune vulnérabilité n'a été trouvée!";
    public static final String NO_RISK_SCENARIO_FOUND = "Aucune scénario de risque n'a été trouvé!";

    public static final String TYPOLOGY_NAME_ALREADY_TAKEN = "Nom de la typologie des actifs est déja utilisé!";
    public static final String THREAT_NAME_ALREADY_TAKEN = "Nom de la menace est déja utilisé!";
    public static final String VULNERABILITY_NAME_ALREADY_TAKEN = "Nom de la vulnérabilité est déja utilisé!";
    public static final String RISK_SCENARIO_NAME_ALREADY_TAKEN = "Nom du scénrio de risque est déja utilisé!";

    public static final String INVALID_ASSET_IMAGE = "Image d'actif non valide!";
    public static final String INVALID_LOCATION_IMAGE = "Image de la localisation est non valide!";

    public static final String CANNOT_UPDATE_RISK_ANALYSIS_ASSET = "Impossible d'affecter un scénario de risque pour un actif différent!";

    public static final String ENTITY_NAME_ALREADY_TAKEN = "Nom de l'entité' est déja utilisé pour l'organisme selectionné!";

    private Constants() {}

}
