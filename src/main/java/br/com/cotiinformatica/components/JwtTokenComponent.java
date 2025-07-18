package br.com.cotiinformatica.components;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenComponent {

	/*
	 * Lendo o valor da chave secreta para criptografar os TOKENS JWT
	 */
	@Value("${jwt.secretkey}")
	private String secret;
	
	/*
	 * Lendo o valor do tempo de expiração dos TOKENS
	 */
	@Value("${jwt.expiration}")
	private Long expiration;
	
	/*
	 * Método para retornar o tempo de expiração dos TOKENS
	 */
	public Date getExpirationDate() {
		var dataAtual = new Date();
		return new Date(dataAtual.getTime() + expiration);
	}
	
	/*
	 * Método para gerar e retornar os TOKENS JWT
	 */
	public String generateToken(UUID id, String perfil) {
	
		var dataExpiracao = getExpirationDate();
		
		return Jwts.builder()
				.setSubject(id.toString()) //identificação do usuário para o qual o token foi gerado
				.claim("perfil", perfil) //nome do perfil do usuário
				.setIssuedAt(new Date()) //data e hora da geração do token
				.setExpiration(dataExpiracao) //data e hora que o token expira
				.signWith(SignatureAlgorithm.HS256, secret) //criptografando a chave do token
				.compact();
	}	
}
