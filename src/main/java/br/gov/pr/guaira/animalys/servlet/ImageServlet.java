package br.gov.pr.guaira.animalys.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream; // Adicionado
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_PATH = System.getProperty("java.io.tmpdir") + File.separator + "animalys"
            + File.separator + "fotos" + File.separator;

    // Caminho para a imagem padrão - usa user.home para compatibilidade
    // Windows/Linux
    private static final String CAMINHO_IMAGEM_PADRAO = System.getProperty("user.home") + File.separator + "animalys"
            + File.separator + "fotos" + File.separator + "no_image.jpg";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String requestedFile = request.getPathInfo();

        // Se o nome do arquivo for nulo, vazio ou apenas "/", envia a imagem padrão.
        if (requestedFile == null || requestedFile.trim().isEmpty() || requestedFile.equals("/")) {
            enviarImagemPadrao(response);
            return;
        }

        // Remove a barra inicial para processamento
        if (requestedFile.startsWith("/")) {
            requestedFile = requestedFile.substring(1);
        }

        // Constrói o caminho completo do arquivo no sistema de arquivos
        String caminhoCompleto = UPLOAD_PATH + requestedFile.replace("/", File.separator);
        File file = new File(caminhoCompleto);

        // Se o arquivo não existe ou não é um arquivo (é um diretório), envia a imagem
        // padrão.
        if (!file.exists() || !file.isFile()) {
            enviarImagemPadrao(response);
            return;
        }

        // Verifica se o caminho do arquivo é seguro para evitar ataques
        try {
            String canonicalUploadPath = new File(UPLOAD_PATH).getCanonicalPath();
            String canonicalFilePath = file.getCanonicalPath();
            if (!canonicalFilePath.startsWith(canonicalUploadPath)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso Negado");
                return;
            }
        } catch (IOException e) {
            // Logar o erro é uma boa prática aqui
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Verifica se o tipo de conteúdo é uma imagem
        String contentType = getServletContext().getMimeType(file.getName());
        if (contentType == null || !contentType.startsWith("image/")) {
            enviarImagemPadrao(response);
            return;
        }

        // Se tudo estiver OK, envia o arquivo da foto do animal
        enviarArquivo(file, request, response);
    }

    /**
     * Envia o arquivo de imagem solicitado com headers de cache.
     */
    private void enviarArquivo(File file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Configura a resposta com o tipo de conteúdo e tamanho
        response.setContentType(getServletContext().getMimeType(file.getName()));
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

        // Define headers de cache para o navegador (1 ano)
        long lastModified = file.lastModified();
        response.setDateHeader("Last-Modified", lastModified);
        response.setHeader("Cache-Control", "public, max-age=31536000");

        // Otimização: se o navegador já tem a imagem em cache, envia um status 304 Not
        // Modified
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifModifiedSince != -1 && lastModified <= ifModifiedSince) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        // Envia os bytes do arquivo
        try (FileInputStream input = new FileInputStream(file);
                OutputStream output = response.getOutputStream()) {

            byte[] buffer = new byte[4096]; // Buffer um pouco maior para performance
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        }
    }

    /**
     * Envia a imagem padrão (sem-foto.png) como resposta.
     */
    private void enviarImagemPadrao(HttpServletResponse response) throws IOException {
        // Usa o ClassLoader do Servlet para encontrar o recurso dentro do projeto
        try (InputStream input = getServletContext().getResourceAsStream(CAMINHO_IMAGEM_PADRAO)) {

            if (input == null) {
                // Se nem a imagem padrão for encontrada, envia um erro 404 definitivo.
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Imagem padrão não encontrada.");
                return;
            }

            // Configura a resposta para o tipo de imagem padrão (PNG)
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "inline; filename=\"no_image.jpg\"");
            // Cache para a imagem padrão pode ser mais curto, se desejado
            response.setHeader("Cache-Control", "public, max-age=86400"); // Cache de 1 dia

            // Envia os bytes da imagem padrão para o navegador
            try (OutputStream output = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
        }
    }
}
