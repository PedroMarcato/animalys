package br.gov.pr.guaira.animalys.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/fotos/*")
public class FotoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Caminho base onde as fotos são salvas
    // Usa o diretório home do usuário para compatibilidade entre Windows e Linux
    private static final String CAMINHO_BASE = System.getProperty("user.home") + 
        File.separator + "animalys" + File.separator + "fotos" + File.separator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Pega o nome do arquivo após /fotos/
        String nomeArquivo = req.getPathInfo();
        if (nomeArquivo != null && nomeArquivo.startsWith("/")) {
            nomeArquivo = nomeArquivo.substring(1);
        }

        // Se não foi informado ou arquivo não existe, usa a imagem padrão
        File arquivo = null;
        if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
            arquivo = new File(CAMINHO_BASE, "no_image.jpg");
        } else {
            arquivo = new File(CAMINHO_BASE, nomeArquivo);
            if (!arquivo.exists() || !arquivo.isFile()) {
                arquivo = new File(CAMINHO_BASE, "no_image.jpg");
            }
        }

        // Descobre o tipo de conteúdo da imagem (ex: image/jpeg)
        String contentType = Files.probeContentType(arquivo.toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        resp.setContentType(contentType);

        // Exibe inline no navegador
        resp.setHeader("Content-Disposition", "inline; filename=\"" + arquivo.getName() + "\"");

        // Copia o conteúdo da imagem para a resposta HTTP
        Files.copy(arquivo.toPath(), resp.getOutputStream());
    }
}