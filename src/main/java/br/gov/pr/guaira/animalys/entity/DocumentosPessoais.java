package br.gov.pr.guaira.animalys.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "documentospessoais", schema = "public")
public class DocumentosPessoais implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documentos")
    private Long id;

    @Column(name = "card_unico")
    private String cardUnico;

    @Column(name = "documento_com_foto")
    private String documentoComFoto;

    @Column(name = "comprovante_endereco")
    private String comprovanteEndereco;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardUnico() {
        return cardUnico;
    }

    public void setCardUnico(String cardUnico) {
        this.cardUnico = cardUnico;
    }

    public String getDocumentoComFoto() {
        return documentoComFoto;
    }

    public void setDocumentoComFoto(String documentoComFoto) {
        this.documentoComFoto = documentoComFoto;
    }

    public String getComprovanteEndereco() {
        return comprovanteEndereco;
    }

    public void setComprovanteEndereco(String comprovanteEndereco) {
        this.comprovanteEndereco = comprovanteEndereco;
    }

    public Long getId_documentos() {
        return id;
    }    
}
