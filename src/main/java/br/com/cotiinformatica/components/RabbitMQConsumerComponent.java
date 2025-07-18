package br.com.cotiinformatica.components;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cotiinformatica.dtos.CriarUsuarioResponse;

@Component
public class RabbitMQConsumerComponent {

	@Autowired JavaMailSender javaMailSender;
	@Autowired ObjectMapper objectMapper;
	
	/*
	 * Método para ler e processar cada usuário contido na fila
	 * de modo a enviar um email de boas vindas.
	 */
	@RabbitListener(queues = "usuarios")
	public void consume(@Payload String usuario) {
		try {
			//ler e deserializar os dados de cada usuário contido na fila
			//transformar de JSON para objeto (Classe Java)
			var registro = objectMapper.readValue(usuario, CriarUsuarioResponse.class);
			
			//construindo o email para o usuário
			var message = javaMailSender.createMimeMessage();
			var helper = new MimeMessageHelper(message, true, "UTF-8");
			
			helper.setTo(registro.getEmail()); //Destinatário
			helper.setSubject("Olá " + registro.getNome() + ", seja bem vindo ao sistema!"); //Assunto
			helper.setText("""
					<h2>Parabéns! Sua conta foi criada com sucesso.</h2>
					<p>A partir de agora você poderá acessar sua conta no sistema.</p>
					<p>Seja bem vindo e aproveite as funcionalidades da nossa aplicação.</p>
					<p>Att,</p>
					<p>Equipe COTI Informática,</p>
					""");//ler e deserializar os dados de cada usuário contido na fila
			//enviando a mensagem
			javaMailSender.send(message);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
