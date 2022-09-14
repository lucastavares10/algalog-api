package com.algaworks.algalog.api.exceptionhandler;

public class Campo {

    private String nome;
    private String mensagem;

    public Campo(String nome, String mensagem) {
        super();
        this.nome = nome;
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }
    public String getMensagem() {
        return mensagem;
    }

}
