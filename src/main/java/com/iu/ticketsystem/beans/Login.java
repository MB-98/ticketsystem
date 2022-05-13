package com.iu.ticketsystem.beans;

import java.util.Optional;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.iu.ticketsystem.controller.NavigationController;
import com.iu.ticketsystem.dao.UserDao;
import com.iu.ticketsystem.entity.User;
import com.iu.ticketsystem.util.SessionUtils;

@ManagedBean
@SessionScoped
public class Login {

	private String email;
	private String password;
	private User user;

	NavigationController navigationController = new NavigationController();

	public String validateUsernamePassword() {
		System.out.println(email);
		System.out.println(password);
		Optional<User> oUser = UserDao.getByEmailAndPassword(email, password);
		if (oUser.isPresent()) {
			System.out.println("User exists");
			User userTemp = oUser.get();
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("user", userTemp);
			user = userTemp;
			return navigationController.getRequirementOverview();
		} else {
			System.out.println("User dont exists");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Incorrect Username or Password", "Please enter correct username and Password"));

			return navigationController.getLogin();
		}
	}

	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		user = null;
		return navigationController.getLogin();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
