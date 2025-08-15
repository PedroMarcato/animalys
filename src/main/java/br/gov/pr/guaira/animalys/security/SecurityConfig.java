package br.gov.pr.guaira.animalys.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public AppUserDetailsService userDetailsService() {
		return new AppUserDetailsService();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new JsfLoginUrlAuthenticationEntryPoint();
		jsfLoginEntry.setLoginFormUrl("/Login.xhtml");
		jsfLoginEntry.setRedirectStrategy(new JsfRedirectStrategy());

		JsfAccessDeniedHandler jsfDeniedEntry = new JsfAccessDeniedHandler();
		jsfDeniedEntry.setLoginPath("/AcessoNegado.xhtml");
		jsfDeniedEntry.setContextRelative(true);

		http.csrf().disable().headers().frameOptions().sameOrigin().and()

				.authorizeRequests()

				.antMatchers("/Login.xhtml", "/Erro.xhtml", "/cadastros/cadastroAnimal.xhtml","/index.html",
						"/javax.faces.resource/**")
				.permitAll().antMatchers("/index.xhtml", "/AcessoNegado.xhtml", "/DashBoard.xhtml").authenticated()
				.antMatchers("/cadastros/**").hasRole("GERENCIAR_CADASTRO").antMatchers("/usuario/**")
				.hasRole("GERENCIAR_USUARIO").antMatchers("/estoque/**").hasRole("GERENCIAR_ESTOQUE")
				.antMatchers("/relatorios/**").hasRole("GERENCIAR_RELATORIO").antMatchers("/atendimento/**")
				.hasRole("GERENCIAR_ATENDIMENTO").antMatchers("/solicitacoes/**").hasRole("GERENCIAR_SOLICITACAO").and()

				.formLogin().loginPage("/Login.xhtml").failureUrl("/Login.xhtml?invalid=true").and()

				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).and()

				.exceptionHandling().accessDeniedPage("/AcessoNegado.xhtml").authenticationEntryPoint(jsfLoginEntry)
				.accessDeniedHandler(jsfDeniedEntry);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
	}
}
