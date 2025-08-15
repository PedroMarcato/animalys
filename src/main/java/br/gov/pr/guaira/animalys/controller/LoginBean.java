package br.gov.pr.guaira.animalys.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.annotation.SessionScope;

@Named
@SessionScope
public class LoginBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;
	@Inject
	private HttpServletRequest request;
	@Inject
	private HttpServletResponse response;
	
	private String nomeUsuario;
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
	public void preRender() {
		if("true".equals(request.getParameter("invalid"))){
			br.gov.pr.guaira.animalys.util.jsf.FacesUtil.addErrorMessage("Usuário ou senha inválido");
		}
	}
	
	public void login() throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Login.xhtml");
		dispatcher.forward(request, response);
		
		facesContext.responseComplete();
	}
}
