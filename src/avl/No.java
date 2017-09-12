/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avl;

/**
 *
 * @author pablo
 */

public class No {
    private Integer chave;
    private Integer fb;
    private No esquerda,direita,parente;

    public No(Integer chave, No parent) {
        this.chave = chave;
        this.parente = parent;
        this.fb = 0;
    }

    public Integer getChave() {
        return chave;
    }

    public void setChave(Integer chave) {
        this.chave = chave;
    }

    public Integer getFb() {
        return fb;
    }

    public void setFb(Integer fb) {
        this.fb = fb;
    }

    public No getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(No left) {
        this.esquerda = left;
    }

    public No getDireita() {
        return direita;
    }

    public void setDireita(No right) {
        this.direita = right;
    }

    public No getParente() {
        return parente;
    }

    public void setParente(No parent) {
        this.parente = parent;
    }
    
    
}

