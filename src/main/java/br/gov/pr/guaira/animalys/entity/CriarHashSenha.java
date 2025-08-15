package br.gov.pr.guaira.animalys.entity;

import javax.inject.Inject;
import org.springframework.security.crypto.bcrypt.BCrypt;

import br.gov.pr.guaira.animalys.repository.Usuarios;

public class CriarHashSenha {

	@Inject
	private Usuarios usuarios;

	public String adicionaCriptografado(String senha) {

		// Gera um sal aleat�rio
		String salGerado = BCrypt.gensalt();
		System.out.println("O sal gerado foi $" + salGerado + "$");

		// Gera a senha hasheada utilizando o sal gerado
		String senhaHasheada = BCrypt.hashpw(senha, salGerado);

		return senhaHasheada;
	}

	public boolean autentica(Usuario usuarioCandidato) {

		String loginDoCandidato = usuarioCandidato.getNomeUsuario();
		String senhaDoCandidato = usuarioCandidato.getSenha(); // Essa senha est� em texto puro, sem hash.

		Usuario usuarioComSenhaHasheada = this.usuarios.porNomeUsuario(loginDoCandidato);
		String senhaDoBanco = usuarioComSenhaHasheada.getSenha(); // Essa senha est� hasheada.

		// Usa o BCrypt para verificar se a senha passada est� correta.
		// Isso envolve ler a senhaDoBanco, separar o que � sal e o que � hash
		// usar o sal para criar um hash da senhaDoCandidato e, por fim
		// verificar se os hashes gerados s�o iguais.
		boolean autenticacaoBateu = BCrypt.checkpw(senhaDoCandidato, senhaDoBanco);

		return autenticacaoBateu;

	}
}
