package ch.avocado.share.common.constants;

public final class ErrorMessageConstants {
    public static final String ERROR_NO_EMAIL = "Kein Passwort eingegeben.";
    public static final String ERROR_NO_PASSWORD = "Kein Passwort eingegeben.";
    public static final String ERROR_WRONG_PASSWORD = "Passwort oder E-Mail-Adresse stimmt nicht.";
    public static final String ERROR_SECURITY_HANDLER = "Interner Fehler";
    public static final String ERROR_INDEX_FAILED = "Index konnte nicht geladen werden.";
    public static final String ERROR_CREATE_FAILED = "Es konnte kein Objekt erstellt werden.";
    public static final String ERROR_GET_FAILED = "Objekt konnte nicht gefunden werden.";
    public static final String INVALID_REQUEST = "Ungültige Anfrage";
    public static final String ACCESS_DENIED = "Sie verfügen über zu wenig Zugriffsrecht für diese Aktion.";
    public static final String NOT_LOGGED_IN = "Sie müssen angemeldet sein für diese Aktion.";
    public static final String ERROR_NO_TITLE = "Bitte einen Titel eingeben.";
    public static final String ERROR_NO_CATEGORY_NAME = "Bitte einen Kategorien Namen eingeben.";
    // public static final String ERROR_NO_AUTHOR = "Bitte einen Author eingeben.";
    public static final String ERROR_FILE_TITLE_ALREADY_EXISTS = "Ein File mit diesem Titel existiert bereits.";
    public static final String ERROR_CATEGORY_ALREADY_ADDED = "Diese Kategorie wurde schon hinzugefügt.";
    public static final String ERROR_NO_SUCH_FILE = "File existiert nicht.";
    public static final String DATAHANDLER_EXPCEPTION = "Ein Datenbank-Fehler ist aufgetreten";
    public static final String ERROR_CONTENT_TYPE_NOT_ALLOWED = "Ungültiger Content-Type";
    public static final String ERROR_NO_MODULE_ID = "Module nicht ausgewählt.";
    public static final String ERROR_STORAGE_HANDLER_NOT_FOUND = "IFileStorageHandler wurde nicht gefunden.";
    public static final String ERROR_NO_NAME = "Bitte einen Namen eingeben.";
    public static final String ERROR_NO_DESCRIPTION = "Bitte eine Beschreibung angeben.";
    public static final String ERROR_INTERNAL_SERVER = "Interner Server-Fehler.";
    public static final String ERROR_GROUP_NAME_ALREADY_EXISTS = "Eine Gruppe mit diesem Namen existiert bereits.";
    public static final String ERROR_NO_SUCH_GROUP = "Gruppe existiert nicht.";
    public static final String ERROR_GENERAL_FAILURE = "Password zurücksetzen schlug fehl.";
    public static final String ERROR_EMPTY_EMAIL = "Bitte geben Sie ihre E-Mail-Adresse an.";
    public static final String ERROR_EMPTY_PASSWORD = "Passwort darf nicht leer sein.";
    public static final String ERROR_EMPTY_PASSWORD_CONFIRMATION = "Passwort-Bestätigung darf nicht leer sein.";
    public static final String ERROR_INVALID_CODE_OR_EMAIL = "Bestätigungscode oder E-Mail-Adresse stimmen nicht.";
    public static final String ERROR_PASSWORDS_DO_NOT_MATCH = "Passwörter stimmen nicht überein.";
    public static final String ERROR_SEND_MAIL_FAILED = "Senden des E-Mail fehlgeschlagen.";
    public static final String ERROR_EMAIL_NOT_VERIFIED = "E-Mail-Adresse wurde noch nicht bestätigt.";
    public static final String ERROR_SEND_VERIFICATION_MAIL = "Senden des verifikations E-Mails fehlgeschlagen";
    public static final String ERROR_SEND_ACCESS_REQUEST_MAIL = "Senden des Zugriff beantragen E-Mails fehlgeschlagen";
    public static final String ERROR_SEND_PASSWORD_RESET_MAIL = "Senden des Passwort zurücksetzten E-Mails fehlgeschlagen";


    // Preview's
    public static final String ERROR_VIDEO_TYPE_NOT_SUPPORTED_IN_BROWSER = "Ihr Browser scheint das Video-Format leider nicth zu unterstützen.";
    public static final String ERROR_READ_FILE_FAILED = "Die Datei konnte nicht gelesen werden.";
    public static final String ERROR_FILE_TO_BIG_FOR_PREVIEW = "Die Datei ist zu gross, um eine Vorschau zu erzeugen.";
    public static final String ERROR_URLENCODE_FAILED = "Die URL konnte nicht kodiert werden.";

    public static final String SERVICE_NOT_FOUND = "Der Service konnte nicht gefunden werden: ";
    public static final String ERROR_NO_PREVIEW_FACTORY_FOR_TYPE = "Mime-Type wird nicht unterstützt.";

    // Member Control
    public static final String ERROR_GROUP_OR_USER_NOT_FOUND = "Gruppe oder Benutzer konnte nichte gefunden werden.";
    public static final String ERROR_ACCESS_ALREADY_EXISTS = "Es existiert bereits ein Zugriffsrecht.";
    public static final String ERROR_TARGET_NOT_FOUND = "Access target not found.";
    public static final String ERROR_LEVEL_MISSING = "Parameter 'level' missing.";
    public static final String ERROR_MISSING_GROUP_OR_USER_ID = "Parameter 'groupId' oder 'userId' fehlen.";
    public static final String ERROR_BOTH_USER_AND_GROUP_ID_SET = "Es darf nur einer der Parameter 'groupId' oder 'userId' gesetzt sein.";
    public static final String ERROR_UNABLE_TO_SET_RIGHTS = "Zugriffsrechte konnten nicht gesetzt werden.";


    public static final String MISSING_PARAMETER = "Es fehlen nötige Parameter. Bitte versichern Sie sich, dass sie die URL korrekt eingegeben haben oder versuchen Sie es später noch einmal.";
    public static final String OBJECT_NOT_FOUND = "Objekt konnte nicht gefunden werden";
    public static final String METHOD_NOT_ALLOWED = "Methode nicht erlaubt: ";
    public static final String ERROR_CODE_EXPIRED = "Der Code ist leider abgelaufen. Bitte beantragen Sie einen neuen.";
    public static final String ERROR_NAME_TOO_LONG = "Der angegebene Name ist zu lang.";
    public static final String DESCRIPTION_TOO_LONG = "Die Beschreibung ist zu lang.";
    public static final String UPDATE_USER_FAILED = "Der Benutzer konnte nicht aktualisiert werden.";
    public static final String CAPTCHA_INCORRECT = "Das CAPTCHA konnte nicht verfiziert werden.";
    public static final String NO_RIGHTS_TO_ADD_CATEGORY = "Sie müssen Schreibrecht besitzen, um Kategorien hinzuzufügen";
    public static final String NOT_RENDERABLE = "Anzeigen der Antwort fehlgeschlagen.";
    public static final String ACTION_NOT_IMPLEMENTED = "Diese Aktion is nicht implementiert.";
    public static final String PARAMETER_STRING_EXPECTED = "Ein Parameter ist nicht ein Formularfeld sondern eine Datei.";
    public static final String RESPONSE_NOT_WRITEABLE = "Antwort konnte nicht geschrieben werden.";
    public static final String FILE_STORAGE_EXCEPTION = "Es trat ein Fehler beim schreiben oder lesen der Datei auf.";
    public static final java.lang.String UNSUPPORTED_ENCODING = "Ein erforderliches Encoding wird nicht unterstützt.";
    public static final String PATH_NOT_FOUND = "Der Pfad konnte nicht gefunden werden: ";
    public static final String UNKNOWN_ERROR = "Unbekannter Fehler.";
    public static final java.lang.String INVALID_ID = "Ungültiger Identifier.";
    public static String UNKNOWN_SERVICE_EXCEPTION = "Unbekannter Fehler";
}
