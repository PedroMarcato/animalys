package br.gov.pr.guaira.animalys.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.primefaces.model.UploadedFile;

/**
 * Utilitário para upload de arquivos de receitas/laudos
 */
public class FileUploadUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(FileUploadUtil.class);

	// Diretório onde os arquivos serão salvos
	// Usa o diretório home do usuário para compatibilidade entre Windows e Linux
	private static final String UPLOAD_DIRECTORY = System.getProperty("user.home") +
			File.separator + "animalys" + File.separator + "receitas" + File.separator;

	// Extensões permitidas (imagens e PDF)
	private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
			"jpg", "jpeg", "png", "gif", "bmp", "pdf");

	// Tamanho máximo: 10MB
	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

	/**
	 * Salva um arquivo enviado e retorna o nome único gerado
	 * 
	 * @param uploadedFile Arquivo enviado pelo usuário
	 * @return Nome único do arquivo salvo (UUID + extensão original)
	 * @throws IOException              Se houver erro ao salvar o arquivo
	 * @throws IllegalArgumentException Se o arquivo for inválido
	 */
	public static String salvarArquivo(UploadedFile uploadedFile) throws IOException {
		logger.info("salvarArquivo: início do processamento de upload");
		System.out.println("[server-debug][FileUploadUtil] salvarArquivo: início - nome="
				+ (uploadedFile != null ? uploadedFile.getFileName() : "(null)") + " size="
				+ (uploadedFile != null ? uploadedFile.getSize() : 0));
		if (uploadedFile == null || uploadedFile.getContents() == null) {
			logger.warn("salvarArquivo: arquivo nulo ou vazio recebido");
			throw new IllegalArgumentException("Arquivo inválido ou vazio");
		}

		// Validar tamanho
		if (uploadedFile.getSize() > MAX_FILE_SIZE) {
			throw new IllegalArgumentException("Arquivo muito grande. Tamanho máximo: 10MB");
		}

		// Obter extensão original
		String nomeOriginal = uploadedFile.getFileName();
		String extensao = obterExtensao(nomeOriginal);

		// Validar extensão
		if (!ALLOWED_EXTENSIONS.contains(extensao.toLowerCase())) {
			throw new IllegalArgumentException(
					"Tipo de arquivo não permitido. Permitidos: imagens (JPG, PNG, GIF, BMP) e PDF");
		}

		// Gerar nome único
		String nomeUnico = UUID.randomUUID().toString() + "." + extensao;
		logger.debug("salvarArquivo: nome original='" + nomeOriginal + "', gerando nome unico='" + nomeUnico + "'");

		// Criar diretório se não existir
		File diretorio = new File(UPLOAD_DIRECTORY);
		if (!diretorio.exists()) {
			diretorio.mkdirs();
		}

		// Salvar arquivo
		File arquivo = new File(UPLOAD_DIRECTORY + nomeUnico);
		try (InputStream input = uploadedFile.getInputstream();
				OutputStream output = new FileOutputStream(arquivo)) {

			logger.debug("salvarArquivo: salvando arquivo em '" + arquivo.getAbsolutePath() + "'");
			System.out.println("[server-debug][FileUploadUtil] salvarArquivo: salvando arquivo em '"
					+ arquivo.getAbsolutePath() + "'");
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			logger.info("salvarArquivo: arquivo salvo com sucesso: '" + arquivo.getAbsolutePath() + "'");
			System.out.println("[server-debug][FileUploadUtil] salvarArquivo: arquivo salvo com sucesso: '"
					+ arquivo.getAbsolutePath() + "'");
		} catch (IOException ioe) {
			logger.error("salvarArquivo: falha ao salvar arquivo: " + ioe.getMessage(), ioe);
			System.out.println(
					"[server-debug][FileUploadUtil] salvarArquivo: falha ao salvar arquivo: " + ioe.getMessage());
			throw ioe;
		}

		return nomeUnico;
	}

	/**
	 * Remove um arquivo do diretório de uploads
	 * 
	 * @param nomeArquivo Nome do arquivo a ser removido
	 * @return true se o arquivo foi removido, false caso contrário
	 */
	public static boolean removerArquivo(String nomeArquivo) {
		logger.info("removerArquivo: solicitacao para remover '" + nomeArquivo + "'");
		System.out.println(
				"[server-debug][FileUploadUtil] removerArquivo: solicitacao para remover '" + nomeArquivo + "'");
		if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
			logger.warn("removerArquivo: nomeArquivo nulo ou vazio");
			System.out.println("[server-debug][FileUploadUtil] removerArquivo: nomeArquivo nulo ou vazio");
			return false;
		}

		File arquivo = new File(UPLOAD_DIRECTORY + nomeArquivo);
		if (arquivo.exists()) {
			boolean removed = arquivo.delete();
			logger.info("removerArquivo: arquivo '" + arquivo.getAbsolutePath() + "' removido? " + removed);
			System.out.println("[server-debug][FileUploadUtil] removerArquivo: arquivo '" + arquivo.getAbsolutePath()
					+ "' removido? " + removed);
			return removed;
		}
		logger.warn("removerArquivo: arquivo não encontrado: '" + arquivo.getAbsolutePath() + "'");
		System.out.println("[server-debug][FileUploadUtil] removerArquivo: arquivo não encontrado: '"
				+ arquivo.getAbsolutePath() + "'");
		return false;
	}

	/**
	 * Verifica se um arquivo existe no diretório de uploads
	 * 
	 * @param nomeArquivo Nome do arquivo
	 * @return true se o arquivo existe
	 */
	public static boolean arquivoExiste(String nomeArquivo) {
		if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
			return false;
		}

		File arquivo = new File(UPLOAD_DIRECTORY + nomeArquivo);
		return arquivo.exists();
	}

	/**
	 * Retorna o caminho completo de um arquivo
	 * 
	 * @param nomeArquivo Nome do arquivo
	 * @return Caminho completo
	 */
	public static String obterCaminhoCompleto(String nomeArquivo) {
		return UPLOAD_DIRECTORY + nomeArquivo;
	}

	/**
	 * Retorna a URL para acessar o arquivo via contexto /uploads/receitas
	 * 
	 * @param nomeArquivo Nome do arquivo
	 * @return URL relativa
	 */
	public static String obterUrlArquivo(String nomeArquivo) {
		return "/uploads/receitas/" + nomeArquivo;
	}

	/**
	 * Verifica se o arquivo é uma imagem baseado na extensão
	 * 
	 * @param nomeArquivo Nome do arquivo
	 * @return true se for imagem
	 */
	public static boolean isImagem(String nomeArquivo) {
		if (nomeArquivo == null) {
			return false;
		}
		String extensao = obterExtensao(nomeArquivo).toLowerCase();
		return Arrays.asList("jpg", "jpeg", "png", "gif", "bmp").contains(extensao);
	}

	/**
	 * Verifica se o arquivo é um PDF baseado na extensão
	 * 
	 * @param nomeArquivo Nome do arquivo
	 * @return true se for PDF
	 */
	public static boolean isPdf(String nomeArquivo) {
		if (nomeArquivo == null) {
			return false;
		}
		return "pdf".equalsIgnoreCase(obterExtensao(nomeArquivo));
	}

	/**
	 * Extrai a extensão de um nome de arquivo
	 * 
	 * @param nomeArquivo Nome do arquivo
	 * @return Extensão (sem o ponto)
	 */
	private static String obterExtensao(String nomeArquivo) {
		if (nomeArquivo == null || !nomeArquivo.contains(".")) {
			return "";
		}
		return nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1);
	}
}
