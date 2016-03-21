/**
 * 
 */
package ch.avocado.share.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.avocado.share.model.data.User;

/**
 * @author coffeemakr
 *
 */

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	static final String LOGIN_FORM_URL = "/login_form.jsp";
	
	static final public String LOGIN_ERROR = "login_error";
	static final public String FIELD_EMAIL = "email";
	static final public String FIELD_PASSWORD = "password";

	static private final String COOKIE_CHECK_NAME = "are_cookies_enabled";
	static private final String COOKIE_CHECK_VALUE = "yes! :)";
	
	private static final long serialVersionUID = 5348852043943606854L;
	
	
	public User checkLogin(String email, String password) {

		//User user = UserController.findByEmail(email);
		User user = null;
		if (user != null && user.getPassword().matchesPassword(password)) {
			return user;
		}
		return null;
	}

	private void renderLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(LOGIN_FORM_URL);
		dispatcher.forward(request, response);
	}


	private void addTestCookie(HttpServletResponse response) {
		Cookie checkCookie = new Cookie(COOKIE_CHECK_NAME, COOKIE_CHECK_VALUE);
		response.addCookie(checkCookie);
	}

	public boolean checkTestCookie(HttpServletRequest request) {
		for(Cookie cookie: request.getCookies()) {
			if(cookie.getName().equals(COOKIE_CHECK_NAME) && cookie.getValue().equals(COOKIE_CHECK_VALUE)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		addTestCookie(response);
		renderLogin(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		String email = request.getParameter(FIELD_EMAIL);
		String password = request.getParameter(FIELD_PASSWORD);
		if(email == null) {
			request.setAttribute(LOGIN_ERROR, "Kein Passwort eingegeben.");
			renderLogin(request, response);
		} else if (password == null) {
			request.setAttribute(LOGIN_ERROR, "Kein Passwort eingegeben.");
			renderLogin(request, response);
		} else {
			if(!checkTestCookie(request)) {
				request.setAttribute(LOGIN_ERROR, "Cookies scheinen deaktiviert zu sein. Bitte überprüfen Sie das.");
			}
			User user = checkLogin(email, password);
			if (user != null) {
				
			} else {
				request.setAttribute(LOGIN_ERROR, "Passwort oder E-Mail-Adresse stimmt nicht.");
				renderLogin(request, response);			
			}			
		}
	}
}
