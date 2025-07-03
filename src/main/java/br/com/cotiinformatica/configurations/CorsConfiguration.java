package br.com.cotiinformatica.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**") //permissão para todos os endpoints
			.allowedOrigins("*") //permissão para qualquer domínio
			.allowedMethods("*") //permissão para qualquer método
			.allowedHeaders("*"); //permissão para quaisquer parâmetros
	}
}
