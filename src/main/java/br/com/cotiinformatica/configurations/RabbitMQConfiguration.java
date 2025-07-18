package br.com.cotiinformatica.configurations;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

	/*
	 * Método de configuração para mapear o nome da fila
	 * que iremos acessar dentro do servidor de mensageria
	 */
	@Bean
	public Queue queue() {
		return new Queue("usuarios", true);
	}
}
