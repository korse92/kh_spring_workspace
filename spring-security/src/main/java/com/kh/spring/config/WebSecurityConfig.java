package com.kh.spring.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.kh.spring.member.model.service.MemberService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private DataSource dataSource;

	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		return tokenRepository;
	}

	
	/**
	 * 클래스레벨에 작성한 @Configuration 하위에서 작동. 
	 * 리턴 객체를 빈으로 등록하는 사용.
	 * id : passwordEncoder 메소드 이름
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * WebSecurity객체를 통해 인증대상에서 제외될 자원을 설정
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	/**
	 * HttpSecurity객체를 통해 인증/권한, 로그인/로그아웃등을 설정
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/member/**").authenticated()
			.antMatchers("/board/**").hasRole(MemberService.ROLE_USER)
			.antMatchers("/admin/**").hasRole(MemberService.ROLE_ADMIN)
			.antMatchers("/").permitAll();
		
		http.formLogin()
			.loginPage("/member/memberLogin.do")	// GET /login
			.loginProcessingUrl("/member/memberLogin.do") // POST /login
			.usernameParameter("id")
			.passwordParameter("password")
			.defaultSuccessUrl("/")
			.permitAll();
		
		//remember-me 기능 : session timeout이상으로 인증을 유지
		http.rememberMe()
			.key("spring-security-app") // application별 고유문자열
			.tokenValiditySeconds(60 * 60 * 24 * 14) // 14일 기본값
			.tokenRepository(tokenRepository()); // database관련정보 제공
		
		http.logout()
			.logoutUrl("/member/memberLogout.do") // POST
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true);
		
		//접근권한 없음 페이지 연결
		http.exceptionHandling()
			.accessDeniedPage("/error/accessDenied.do");
		
	}

	/**
	 * AuthenticationManagerBuilder객체를 통해
	 * UserDetailsService구현체 빈, 비밀번호 암호화 빈 등록
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService)
			.passwordEncoder(passwordEncoder());
		
	}
	
	

	
	
	
}
