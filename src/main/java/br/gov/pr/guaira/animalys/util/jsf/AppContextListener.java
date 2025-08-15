package br.gov.pr.guaira.animalys.util.jsf;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
	
	//USADO PARA ALTERAR A CONVERSãO PARA ZERO QUANDO O ATRIBUTO � DO TIPO INTEGER OU LONG
	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}

}
