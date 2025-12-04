package br.gov.pr.guaira.animalys.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.primefaces.model.UploadedFile;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.FotoAnimal;

@Named
@ApplicationScoped
public class FotoService {

    private String caminhoFotosBase;

    @PostConstruct
    public void init() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                .getContext();
        this.caminhoFotosBase = servletContext.getInitParameter("caminhoFotos");

        if (this.caminhoFotosBase == null || this.caminhoFotosBase.isEmpty()) {
            // Fallback para um caminho padrão ou lançar exceção se a configuração for
            // obrigatória
            // Para fins de desenvolvimento, pode ser útil um fallback, mas em produção, é
            // melhor que seja configurado.
            this.caminhoFotosBase = System.getProperty("user.home") +
                    java.io.File.separator + "animalys" + java.io.File.separator + "fotos" + java.io.File.separator;
            System.err.println("ATENÇÃO: Parâmetro 'caminhoFotos' não configurado no web.xml. Usando fallback: "
                    + this.caminhoFotosBase);
        }

        // Garante que o diretório exista
        try {
            Files.createDirectories(Paths.get(this.caminhoFotosBase));
            System.out.println("Diretório de fotos inicializado em: " + this.caminhoFotosBase);
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de fotos: " + this.caminhoFotosBase + " - " + e.getMessage());
            // Lançar uma exceção de tempo de execução se a criação do diretório for crítica
            throw new RuntimeException("Não foi possível inicializar o diretório de fotos.", e);
        }
    }

    /*
     * PARA SALVAR EM RESOURCES:
     *
     * @PostConstruct
     * public void init() {
     * ServletContext servletContext = (ServletContext)
     * FacesContext.getCurrentInstance().getExternalContext().getContext();
     * // Pega o caminho físico da pasta 'resources/fotos' dentro do webapp
     * this.caminhoFotosBase = servletContext.getRealPath("/resources/fotos");
     * 
     * if (this.caminhoFotosBase == null) {
     * throw new
     * RuntimeException("Não foi possível determinar o caminho físico para /resources/fotos"
     * );
     * }
     * 
     * // Garante que o diretório exista
     * try {
     * Files.createDirectories(Paths.get(this.caminhoFotosBase));
     * System.out.println("Diretório de fotos inicializado em: " +
     * this.caminhoFotosBase);
     * } catch (IOException e) {
     * System.err.println("Erro ao criar diretório de fotos: " +
     * this.caminhoFotosBase + " - " + e.getMessage());
     * throw new
     * RuntimeException("Não foi possível inicializar o diretório de fotos.", e);
     * }
     * }
     *
     */

    public FotoAnimal salvarFoto(UploadedFile uploadedFile, Animal animal) throws IOException {
        String nomeUnico = UUID.randomUUID().toString() + "_" + uploadedFile.getFileName();
        Path caminhoDestino = Paths.get(this.caminhoFotosBase, nomeUnico);

        try (InputStream input = uploadedFile.getInputstream()) {
            Files.copy(input, caminhoDestino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Foto salva fisicamente em: " + caminhoDestino.toString());
        }

        FotoAnimal fotoAnimal = new FotoAnimal();
        fotoAnimal.setNomeArquivo(nomeUnico);
        fotoAnimal.setNomeOriginal(uploadedFile.getFileName());
        fotoAnimal.setTipoMime(uploadedFile.getContentType());
        fotoAnimal.setTamanho(uploadedFile.getSize());
        fotoAnimal.setAnimal(animal);
        fotoAnimal.setPrincipal(true); // Definir como principal por padrão, ou ter uma lógica para isso

        return fotoAnimal;
    }

    public String obterUrlFoto(FotoAnimal fotoAnimal) {
        // Esta URL será usada para exibir a imagem no front-end.
        // Se você está salvando as fotos em um diretório que é servido diretamente pelo
        // servidor web
        // (e.g., via um alias ou contexto virtual), você pode retornar a URL relativa a
        // esse mapeamento.
        // Se as fotos estão dentro de webapp/resources/fotos, então o caminho relativo
        // é suficiente.
        // Se as fotos estão fora do webapp, você precisará de um servlet para
        // servi-las.
        return "/resources/fotos/" + fotoAnimal.getNomeArquivo();
    }
}