package br.com.cotiinformatica;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.cotiinformatica.dtos.AutenticarUsuarioRequest;
import br.com.cotiinformatica.dtos.CriarUsuarioRequest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class ProjetoUsuariosApiApplicationTests {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	
	//Atributos para armazenar os dados do usuário cadastrado
	private static String emailAcesso;
	private static String senhaAcesso;
	
	@Test
	@Order(1)
	@DisplayName("Deve criar um usuário com sucesso.")
	void deveCriarUsuarioComSucesso() {
		try {
			
			var faker = new Faker();
			
			//Arrange (preparar os dados do teste)
			var request = new CriarUsuarioRequest();
			request.setNome(faker.name().fullName()); //gerando um nome completo
			request.setEmail(faker.internet().emailAddress()); //gerando um endereço de email
			request.setSenha("@Teste2025"); //definindo uma senha padrão
			
			//Act (executar a ação que será testada)
			var result = mockMvc.perform(post("/api/v1/usuario/criar") //ENDPOINT da API
							.contentType("application/json") //configurando o envio como JSON
							.content(objectMapper.writeValueAsString(request))); //serializando os dados em json
			
			//Assert (verificar os resultados obtidos)
			result.andExpect(status().isOk());
			
			//armazenar o email e senha do usuário cadastrado
			emailAcesso = request.getEmail();
			senhaAcesso = request.getSenha();
		}
		catch(Exception e) {
			fail("Erro: " + e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	@DisplayName("Deve autenticar um usuário com sucesso.")
	void deveAutenticarUsuarioComSucesso() {
		try {
			
			//Arrange (preparar os dados do teste)
			var request = new AutenticarUsuarioRequest();		
			request.setEmail(emailAcesso); //email do usuário cadastrado 
			request.setSenha(senhaAcesso); //senha do usuário cadastrado
			
			//Act (executar a ação que será testada)
			var result = mockMvc.perform(post("/api/v1/usuario/autenticar") //ENDPOINT da API
							.contentType("application/json") //configurando o envio como JSON
							.content(objectMapper.writeValueAsString(request))); //serializando os dados em json
			
			//Assert (verificar os resultados obtidos)
			result.andExpect(status().isOk());
		}
		catch(Exception e) {
			fail("Erro: " + e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	@DisplayName("Não deve permitir cadastrar usuários com o mesmo email.")
	void naoDevePermitirCadastrarUsuariosComMesmoEmail() {
		try {
			
			var faker = new Faker();
			
			//Arrange (preparar os dados do teste)
			var request = new CriarUsuarioRequest();
			request.setNome(faker.name().fullName()); //criando um nome de usuário
			request.setEmail(emailAcesso); //email do usuário cadastrado 
			request.setSenha(senhaAcesso); //senha do usuário cadastrado
			
			//Act (executar a ação que será testada)
			var result = mockMvc.perform(post("/api/v1/usuario/criar") //ENDPOINT da API
							.contentType("application/json") //configurando o envio como JSON
							.content(objectMapper.writeValueAsString(request))); //serializando os dados em json
			
			//Assert (verificar os resultados obtidos)
			result.andExpect(status().isBadRequest());
			
			//capturando a resposta de texto da API
			var response = result.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
			//verificando a resposta contem a mensagem desejada
			assertTrue(response.contains("O email informado já está cadastrado. Tente outro."));
		}
		catch(Exception e) {
			fail("Erro: " + e.getMessage());
		}
	}

	@Test
	@Order(4)
	@DisplayName("Não deve permitir autenticar um usuário inválido.")
	void naoDevePermitirAutenticarUsuarioInvalido() {
		try {
			
			//Arrange (preparar os dados do teste)
			var request = new AutenticarUsuarioRequest();
			request.setEmail("teste@teste.com");  
			request.setSenha("@Teste123"); 
			
			//Act (executar a ação que será testada)
			var result = mockMvc.perform(post("/api/v1/usuario/autenticar") //ENDPOINT da API
							.contentType("application/json") //configurando o envio como JSON
							.content(objectMapper.writeValueAsString(request))); //serializando os dados em json
			
			//Assert (verificar os resultados obtidos)
			result.andExpect(status().isUnauthorized());
			
			//capturando a resposta de texto da API
			var response = result.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
			//verificando a resposta contem a mensagem desejada
			assertTrue(response.contains("Acesso negado. Verifique as credenciais informadas."));
		}
		catch(Exception e) {
			fail("Erro: " + e.getMessage());
		}
	}
	
	@Test
	@Order(5)
	@DisplayName("Não deve permitir o cadastro de um usuário com senha fraca.")
	void naoDevePermitirCadastroDeUsuarioComSenhaFraca() {
		try {
			
			var faker = new Faker();
			
			//Arrange (preparar os dados do teste)
			var request = new CriarUsuarioRequest();
			request.setNome(faker.name().fullName());
			request.setEmail(faker.internet().emailAddress());  
			request.setSenha("teste1234"); 
			
			//Act (executar a ação que será testada)
			var result = mockMvc.perform(post("/api/v1/usuario/criar") //ENDPOINT da API
							.contentType("application/json") //configurando o envio como JSON
							.content(objectMapper.writeValueAsString(request))); //serializando os dados em json
			
			//Assert (verificar os resultados obtidos)
			result.andExpect(status().isBadRequest());
			
			//capturando a resposta de texto da API
			var response = result.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
			//verificando a resposta contem a mensagem desejada
			assertTrue(response.contains("A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial."));
		}
		catch(Exception e) {
			fail("Erro: " + e.getMessage());
		}
	}
}
