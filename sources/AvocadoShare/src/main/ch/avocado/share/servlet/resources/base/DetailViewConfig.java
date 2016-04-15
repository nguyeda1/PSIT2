package ch.avocado.share.servlet.resources.base;

import ch.avocado.share.model.data.Members;
import ch.avocado.share.model.data.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;

/**
 * Configuration to render a single resource.
 */
public class DetailViewConfig extends ViewConfig {

    private Model object;
    private FormError formErrors;
    private Members members;

    public DetailViewConfig(View view, HttpServletRequest request, HttpServletResponse response, Model object,
                            Members members) {
        super(view, request, response);
        this.object = object;

        if(object != null) {
            this.formErrors = new FormError(object.getFieldErrors());
        } else {
            this.formErrors = new FormError(new HashMap<String, String>());
        }
        this.members = members;
    }

    public Model getObject() {
        return object;
    }

    public <E> E getObject(Class<E> modelClass) {
        if(modelClass == null) throw new IllegalArgumentException("modelClass is null");
        return modelClass.cast(object);
    }

    public FormError getFormErrors() {
        return formErrors;
    }

    public Members getMembers() {
        return members;
    }
}
