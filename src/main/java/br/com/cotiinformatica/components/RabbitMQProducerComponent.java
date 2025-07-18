package br.com.cotiinformatica.components;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cotiinformatica.dtos.CriarUsuarioResponse;

@Component
public class RabbitMQProducerComponent {

	@Autowired RabbitTemplate rabbitTemplate; 	//Classe para acessar o RabbitMQ
	@Autowired ObjectMapper objectMapper;		//Classe para transformar dados em JSON
	@Autowired Queue queue;						//Classe para acessar a fila
	
	/*
	 * Método para gravar os dados do usuário na fila
	 * O que será enviado para a fila são os dados
	 * de um usuário que acabou de ser cadastrado no sistema
	 */
	public void send(CriarUsuarioResponse usuario) {
		try {
			//Transformar os dados do usuário em JSON antes de envia-lo para a fila
			var json = objectMapper.writeValueAsString(usuario);
			//Enviando os dads do usuário para a fila
			rabbitTemplate.convertAndSend(queue.getName(), json);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
