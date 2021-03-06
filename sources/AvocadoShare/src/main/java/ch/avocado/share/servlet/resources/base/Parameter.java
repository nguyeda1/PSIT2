package ch.avocado.share.servlet.resources.base;

import ch.avocado.share.common.constants.ErrorMessageConstants;
import ch.avocado.share.model.exceptions.HttpServletException;

import java.util.HashMap;

import static ch.avocado.share.common.HttpStatusCode.BAD_REQUEST;
import static ch.avocado.share.common.constants.ErrorMessageConstants.MISSING_PARAMETER;

/**
 * Created by coffeemakr on 04.05.16.
 */
public class Parameter extends HashMap<String, Object> {
    public String getRequiredParameter(String name) throws HttpServletException {
        if(!containsKey(name)) {
            throw new HttpServletException(BAD_REQUEST, MISSING_PARAMETER);
        }
        Object value = get(name);
        if(!(value instanceof String)) {
            throw new HttpServletException(BAD_REQUEST, ErrorMessageConstants.PARAMETER_STRING_EXPECTED);
        }
        return (String) value;
    }
}
