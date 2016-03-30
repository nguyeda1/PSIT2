package ch.avocado.share.common.form;

import ch.avocado.share.common.Encoder;
import ch.avocado.share.common.HttpMethod;
import ch.avocado.share.model.data.AccessControlObjectBase;
import ch.avocado.share.model.data.Module;
import ch.avocado.share.model.data.UserPassword;
import ch.avocado.share.model.exceptions.FormBuilderException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class FormBuilder {
    private Map<String, String> formErrors;
    private String path;
    private String encodingType = null;
    private AccessControlObjectBase object;
    private Class<? extends AccessControlObjectBase> objectClass;
    private String idPrefix = null;
    private Map<String, String> readableFieldNames;

    public FormBuilder(String path, Class<? extends AccessControlObjectBase> resourceClass, Map<String, String> formErrors) {
        this(path, null, resourceClass, formErrors);
    }


    public FormBuilder(String path, AccessControlObjectBase object, Class<? extends AccessControlObjectBase> resourceClass, Map<String, String> formErrors) {
        if (path == null) throw new IllegalArgumentException("path is null");
        if (resourceClass == null) throw new IllegalArgumentException("objectClass is null");
        if (formErrors == null) {
            formErrors = new HashMap<>();
        }
        this.path = path;
        this.object = object;
        this.objectClass = resourceClass;
        this.formErrors = formErrors;
        readableFieldNames = new HashMap<>();
    }

    public void setReadableFieldName(String fieldName, String readableName) {
        readableFieldNames.put(fieldName, readableName);
    }

    public String getFormErrors() {
        if (formErrors.isEmpty()) {
            return "";
        }
        String errors = "<ul class=\"form-errors\">\n";
        for (Map.Entry<String, String> entry : formErrors.entrySet()) {
            String name;
            if (readableFieldNames.containsKey(entry.getKey())) {
                name = readableFieldNames.get(entry.getKey());
            } else {
                name = entry.getKey();
            }
            errors += "\t<li>" + Encoder.forHtml(name) + ": " + Encoder.forHtml(entry.getValue()) + "</li>\n";
        }
        return errors + "</ul>";
    }

    public String getFormBegin(String method) throws FormBuilderException {
        String form = "<form ";
        String formContent = "";
        String formMethod;
        HttpMethod httpMethod = HttpMethod.fromString(method);
        if (httpMethod == null) {
            throw new FormBuilderException("Method not supported: " + method);
        }
        switch (httpMethod) {
            case POST:
            case GET:
                formMethod = httpMethod.name();
                break;
            case PUT:
            case PATCH:
            case DELETE:
            default:
                formMethod = HttpMethod.POST.name();
                formContent = getInputFor("method", "hidden", method);
        }

        if (object != null) {
            formContent += getInputFor("id", "hidden");
        }
        form += formatAttribute("action", path);
        form += formatAttribute("method", formMethod);
        if(getEncodingType() != null) {
            form += formatAttribute("enctype", getEncodingType());
        }
        form += ">\n" + formContent;
        return form;
    }

    public String getFormEnd() {
        return "</form>";
    }

    private InputType getTypeFromGetter(Method getter) throws FormBuilderException {
        if (getter == null) throw new IllegalArgumentException("getter is null");
        InputType type;
        Class fieldType = getter.getReturnType();
        if (fieldType == String.class) {
            type = InputType.TEXT;
        } else if (fieldType == UserPassword.class) {
            type = InputType.PASSWORD;
        } else if (fieldType == int.class || fieldType == Integer.class) {
            type = InputType.NUMBER;
        } else {
            throw new FormBuilderException("unknown type " + fieldType);
        }
        return type;
    }


    private Method getGetterMethod(String fieldName) throws FormBuilderException {
        String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            return objectClass.getMethod(getterMethodName);
        } catch (NoSuchMethodException e) {
            throw new FormBuilderException("Getter " + getterMethodName + " doesn't exist in " + objectClass);
        }
    }

    public String getInputFor(String fieldName) throws FormBuilderException {
        return getInputFor(fieldName, null, null);
    }

    public String getInputFor(String fieldName, String type) throws FormBuilderException {
        return getInputFor(fieldName, type, null);
    }

    private String getIdPrefix() {
        if (idPrefix == null) {
            Random random = new Random();
            idPrefix = "" + random.nextLong() + "-";
        }
        return idPrefix;
    }

    /**
     * Returns the identifier for a html field.
     *
     * @param fieldName
     * @return
     */
    private String getIdForFieldName(String fieldName) {
        return getIdPrefix() + fieldName;
    }

    public String getLabelFor(String fieldName, String htmlClass) {
        if (fieldName == null) throw new IllegalArgumentException("fieldName is null");
        String content;
        if (readableFieldNames.containsKey(fieldName)) {
            content = readableFieldNames.get(fieldName);
        } else {
            content = fieldName;
        }
        String label = "<label ";
        label += formatAttribute("id", getIdForFieldName(fieldName));
        if (htmlClass != null) {
            label += formatAttribute("class", htmlClass);
        }
        label += ">" + Encoder.forHtml(content) + "</label>";
        return label;
    }

    public String getLabelFor(String fieldName) {
        return getLabelFor(fieldName, null);
    }

    protected static String formatAttribute(String name, String value) {
        return name + "=\"" + Encoder.forHtmlAttribute(value) + "\" ";
    }


    public String getSelectForModules(String fieldName, Module[] modules) {
        if (fieldName == null) throw new IllegalArgumentException("fieldName is null");
        SelectField selectField = new SelectField(fieldName, getIdForFieldName(fieldName));
        for(Module module: modules) {
            selectField.addChoice(module.getId(), module.getName());
        }
        return selectField.toHtml();
    }

    /**
     * @param fieldName The name of the field
     * @param type      null or the type of the element.
     * @param value     null or the value of the element.
     * @return A html input or textarea element.
     * @throws FormBuilderException
     */
    public String getInputFor(String fieldName, String type, String value) throws FormBuilderException {
        if (fieldName == null) throw new IllegalArgumentException("field is null");
        if (type == null || value == null) {
            Method getter = getGetterMethod(fieldName);
            if (type == null) {
                type = getTypeFromGetter(getter).toString().toLowerCase();
            }
            if (value == null) {
                if (object != null && getter != null && !type.equals("password")) {
                    try {
                        value = getter.invoke(object).toString();
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new FormBuilderException(e.getMessage());
                    }
                }
            }
        }

        InputField inputField = new InputField(fieldName, getIdForFieldName(fieldName), type);
        if (value != null) {
            inputField.setValue(value);
        }
        if (formErrors.containsKey(fieldName)) {
            inputField.setError(formErrors.get(fieldName));
        }
        return inputField.toHtml();
    }

    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }
}