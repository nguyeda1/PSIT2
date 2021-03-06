package ch.avocado.share.servlet.resources.base;

import ch.avocado.share.common.HttpMethod;
import ch.avocado.share.common.HttpStatusCode;
import ch.avocado.share.common.ResponseHelper;
import ch.avocado.share.common.constants.ErrorMessageConstants;
import ch.avocado.share.controller.UserSession;
import ch.avocado.share.model.exceptions.HttpServletException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static ch.avocado.share.common.HttpStatusCode.*;
import static ch.avocado.share.common.constants.ErrorMessageConstants.*;
import static ch.avocado.share.common.constants.ErrorMessageConstants.UNKNOWN_ERROR;

/**
 * The ExtendedHttpServlet has similar functionality as {@link javax.servlet.http.HttpServlet HttpServlet}
 * but supports the following features:
 * <p>
 * Multiplart Form data
 * <p>
 * Simulated methods
 */
public abstract class ExtendedHttpServlet extends GenericServlet {
    /**
     * Simulated request parameter.
     */
    public static final String PARAMETER_METHOD = "method";

    private void throwMethodNotAllowed(String method) throws HttpServletException {
        if (method == null) throw new NullPointerException("method is null");
        throw new HttpServletException(HttpStatusCode.METHOD_NOT_ALLOWED, ErrorMessageConstants.METHOD_NOT_ALLOWED + method);
    }

    /**
     * We need this method because HTML forms can only send POST and GET requests and we want to be able to "simulate"
     * another method. This can be done by setting the parameter {@value PARAMETER_METHOD} to the required method.
     *
     * @param request   The http request (not null)
     * @param parameter The parsed parameters (not null)
     * @return The http method to be executed.
     * @throws HttpServletException
     */
    private HttpMethod getMethodFromRequest(HttpServletRequest request, Map<String, Object> parameter) throws HttpServletException {
        if (request == null) throw new NullPointerException("request is null");
        if (parameter == null) throw new NullPointerException("parameter is null");
        HttpMethod method = HttpMethod.fromString(request.getMethod());
        if (method == HttpMethod.POST) {
            if (parameter.containsKey(PARAMETER_METHOD)) {
                HttpMethod simulatedMethod = HttpMethod.fromString((String) parameter.get(PARAMETER_METHOD));
                if (simulatedMethod != null) {
                    method = simulatedMethod;
                }
            }
        }
        if (method == null) {
            throwMethodNotAllowed(request.getMethod());
        }
        return method;
    }

    /**
     * Returns the action associated with the http method
     *
     * @param method The method
     * @return The action
     */
    private Action getActionFromMethod(HttpMethod method) {
        if (method == null) throw new NullPointerException("method is null");
        switch (method) {
            case POST:
                return Action.CREATE;
            case GET:
                return Action.VIEW;
            case PUT:
                return Action.REPLACE;
            case PATCH:
                return Action.UPDATE;
            case DELETE:
                return Action.DELETE;
        }
        throw new RuntimeException("Method not checked: " + method);
    }

    private void parseRequestAndCallAction(HttpServletRequest request, HttpServletResponse response) throws HttpServletException, IOException, ServletException {
        if (request == null) throw new NullPointerException("request is null");
        if (response == null) throw new NullPointerException("response is null");
        Parameter parameter = getParameter(request);
        HttpMethod method = getMethodFromRequest(request, parameter);
        UserSession session = new UserSession(request);
        Action action = getActionFromMethod(method);

        executeAction(request, response, session, parameter, action);
    }

    protected void executeAction(HttpServletRequest request, HttpServletResponse response, UserSession session, Parameter parameter, Action action) throws HttpServletException, IOException {
        switch (action) {
            case VIEW:
                doView(request, response, session, parameter);
                break;
            case REPLACE:
                doReplace(request, response, session, parameter);
                break;
            case UPDATE:
                doUpdate(request, response, session, parameter);
                break;
            case DELETE:
                doDelete(request, response, session, parameter);
                break;
            case CREATE:
                doCreate(request, response, session, parameter);
                break;
            default:
                throw new HttpServletException(NOT_IMPLEMENTED, ACTION_NOT_IMPLEMENTED);
        }
    }

    /**
     * Wrapper method for {@link #service(HttpServletRequest, HttpServletResponse)} which casts the request and response
     * into HTTP request and HTTP responses. An ServletException is thrown if they are not of this type.
     *
     * @param req the request
     * @param res the response
     * @throws ServletException
     * @throws IOException
     * @see GenericServlet#service(ServletRequest, ServletResponse)
     */
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        if (!(req instanceof HttpServletRequest) || !(res instanceof HttpServletResponse)) {
            throw new ServletException("Not a HTTP request or response");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;
        service(httpRequest, httpResponse);
    }

    /**
     * Handle the request and response. In the most cases if an error occurs there will be no ServletException thrown.
     * Instead this method sets call {@link HttpServletResponse#sendError(int, String)} with an appropriate mesasge and
     * status code.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException
     * @throws ServletException
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request == null) throw new NullPointerException("request is null");
        if (response == null) throw new NullPointerException("response is null");
        HttpServletException exception = null;
        try {
            parseRequestAndCallAction(request, response);
        } catch (HttpServletException e) {
            e.printStackTrace();
            exception = e;
        } catch (RuntimeException e) {
            e.printStackTrace();
            exception = new HttpServletException(INTERNAL_SERVER_ERROR, UNKNOWN_ERROR, e);
        }
        if(exception != null) {
            exception.printStackTrace();
            if (!response.isCommitted()) {
                System.out.println("Sending HttpBeanException: " + exception.getMessage() + " - " + exception.getStatusCode());
                ResponseHelper.sendErrorFromHttpBeanException(exception, request, response);
            } else {
                // We can't send an error message so we throw an exception
                throw new ServletException(exception.getMessage());
            }
        }
    }

    /**
     * Parses the parameter of the request.
     *
     * @param request The request.
     * @return A map of parameter - values.
     * @throws HttpServletException
     */
    private Parameter getParameter(HttpServletRequest request) throws HttpServletException {
        if (request == null) throw new NullPointerException("request is null");
        if (request.getContentType() != null && request.getContentType().contains("multipart/form-data")) {
            return getMultipartParameter(request);
        } else{
            Parameter parameter = new Parameter();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String value = request.getParameter(paramName);
                try {
                    value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new HttpServletException(INTERNAL_SERVER_ERROR, UNSUPPORTED_ENCODING, e);
                }
                parameter.put(paramName, value);
            }
            return parameter;
        }
    }

    /**
     * Parses mulipart parameters.
     *
     * @param request The request
     * @return A map of Parameter name mapped to the value.
     * @throws HttpServletException
     */
    private Parameter getMultipartParameter(HttpServletRequest request) throws HttpServletException {
        Parameter parameter = new Parameter();
        List<FileItem> items;
        try {
            items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        } catch (FileUploadException e) {
            throw new HttpServletException(BAD_REQUEST, e.getMessage());
        }
        for (FileItem item : items) {
            if (item.isFormField()) {
                try {
                    parameter.put(item.getFieldName(), item.getString("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                parameter.put(item.getFieldName(), item);
            }
        }
        return parameter;
    }

    protected void doCreate(HttpServletRequest request, HttpServletResponse response, UserSession session, Parameter parameter)
            throws HttpServletException, IOException {
        throw new HttpServletException(BAD_REQUEST, ACTION_NOT_IMPLEMENTED);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response, UserSession session, Parameter parameter)
            throws HttpServletException, IOException {
        throw new HttpServletException(BAD_REQUEST, ACTION_NOT_IMPLEMENTED);

    }

    protected void doUpdate(HttpServletRequest request, HttpServletResponse response, UserSession session, Parameter parameter)
            throws HttpServletException, IOException {
        throw new HttpServletException(BAD_REQUEST, ACTION_NOT_IMPLEMENTED);
    }

    protected void doView(HttpServletRequest request, HttpServletResponse response, UserSession session, Parameter parameter)
            throws HttpServletException, IOException {
        throw new HttpServletException(BAD_REQUEST, ACTION_NOT_IMPLEMENTED);
    }

    protected void doReplace(HttpServletRequest request, HttpServletResponse response, UserSession session, Parameter parameter)
            throws HttpServletException, IOException {
        throw new HttpServletException(BAD_REQUEST, ACTION_NOT_IMPLEMENTED);
    }

}
