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
public class Arvore {
    
    private No raiz;
    private Boolean removido; // informa se foi removido na direita (true) ou esquerda (false)
    private No npd; // varizvel indicativa de um no que pode estar desbalanceado

    //INSERÇÃO DE UM NOVO NO
    public Boolean inserir(Integer chave) {
        //Caso não exista raiz esse no se torna a raiz
        if (isEmpty()) {
            raiz = new No(chave, null);
        } else {
            No n = raiz;
            No pai;

            while (true) {
                //Se a chave já existior na arvore não a necessidade de inseri-la novamente
                if (n.getChave() == chave) {
                    return false;
                }

                pai = n;

                Boolean goEsquerda = n.getChave() > chave; //Verificamos se a chave tem o valor maior ou menor, para decidir se vai para esquerda ou direita
                n = goEsquerda ? n.getEsquerda() : n.getDireita(); //a partir dos dados acima decidimos se vamos ir para esquerda ou direita da arvore

                if (n == null) {

                    No no = new No(chave, pai);

                    if (goEsquerda) {
                        pai.setEsquerda(no);
                    } else {
                        pai.setDireita(no);
                    }

                    reBalance(no, true);

                    break;
                }
            }
        }
        return true;
    }

    //EXECUÇÃO DE UM REBALANCEAMENTO
    private void reBalance(No n, Boolean inserir) {
        if (inserir) {
            setBalanceInserir(n);
        } else {
            setBalanceDeletar(n);
        }

        n = this.npd; //o n agora é o ultimo nó que teve seu fb atualizado                

        if (n.getFb() == -2) {

            if (n.getDireita().getFb() <= 0) {
                n = rotacaoEsquerda(n);
            } else {
                n = rotacaoDuplaDireita(n);// rotação dupla a esquerda
            }
        } else if (n.getFb() == 2) {

            if (n.getEsquerda().getFb() >= 0) {
                n = rotacaoDireita(n);
            } else {
                n = rotacaoDuplaEsquerda(n); // rotação dupla a direita            
            }
        }

        if (n.getParente() == null) {
            this.raiz = n;
        }

    }

    //REMOÇÃO DE UM NO
    private void deletar(No node) {
        if (node.getEsquerda() == null && node.getDireita() == null) { //É FOLHA?
            if (node.getParente() == raiz) //se for somente o raiz esvazia a arvore
            {
                raiz = null;
            } else {
                No pai = node.getParente();

                //Define qual dos elementos vai ser removido 
                if (pai.getEsquerda() == node) {
                    pai.setEsquerda(null);
                    removido = true;
                } else {
                    pai.setDireita(null);
                    removido = false;
                }

                reBalance(pai, false);
            }
            return;
        }

        if (node.getEsquerda() != null) { // uma pra esquerda o resto pra direita
            No filho = node.getEsquerda();
            while (filho.getDireita() != null) {
                filho = filho.getDireita();
            }
            node.setChave(filho.getChave());
            Arvore.this.deletar(filho);
        } else { // uma pra direita o resto pra esquerda
            No filho = node.getDireita();
            while (filho.getEsquerda() != null) {
                filho = filho.getEsquerda();
            }
            node.setChave(filho.getChave());
            Arvore.this.deletar(filho);
        }

    }

    public void deletar(Integer chave) {
        if (isEmpty()) {
            return;//CASO A ARVORE ESTEJA VAZIA PROSSEGUE COM RAIZ IGUAL A NULL 
        }                   //AGUARDANDO UMA INSERÇÃO PARA INICIAR A POPULAÇÃO DA ARVORE                   

        No node = raiz;//VARIAVEL DO TIPO NO RECEBE A RAIZ
        No filho = node;

        while (filho != null) {
            node = filho;
            filho = chave >= node.getChave() ? node.getDireita() : node.getEsquerda();
            if (chave == node.getChave()) {
                Arvore.this.deletar(node);
                return;
            }
        }
    }
    //---------------------------------------------------------------------------//

    //##----------------BALANCEAMENTO----------------##//
    //Vai subindo na arvore para atualizar os fb's
    private void setBalanceInserir(No node) {
        No pai = node.getParente();

        //verificar se é filho direito ou esquerdo
        if (pai.getEsquerda() != null && pai.getEsquerda().equals(node)) {
            pai.setFb(pai.getFb() + 1);
        } else if (pai.getDireita() != null) {
            pai.setFb(pai.getFb() - 1);
        }

        if (pai.getFb() == 0 || pai.getFb() > 1 || pai.getFb() < -1 || pai.getParente() == null) {
            this.npd = pai;
            return;
        }

        setBalanceInserir(pai);
    }

    private void setBalanceDeletar(No no) {
        if (this.removido) //removido na esquerda
        {
            no.setFb(no.getFb() - 1);
        } else // removido na direita
        {
            no.setFb(no.getFb() + 1);
        }

        if (no == raiz) {
            this.npd = no;
            return;
        }

        No pai = no.getParente();

        //verificação de lado a qual o filho pertence (direita ou esquerda)
        if (pai.getEsquerda() != null && pai.getEsquerda().equals(no)) {
            this.removido = true;
        } else {
            this.removido = false;
        }

        if (no.getFb() != 0 || no.getFb() > 1 || no.getFb() < -1) {
            this.npd = no;
            return;
        }
        setBalanceDeletar(pai);
    }

    private void setBalanceRotacaoLeft(No a, No b) {

        a.setFb(a.getFb() + 1 - Math.min(b.getFb(), 0));
        b.setFb(b.getFb() + 1 + Math.max(a.getFb(), 0));
    }

    private void setBalanceRotacaoRight(No a, No b) {
        a.setFb(a.getFb() - 1 - Math.max(b.getFb(), 0));
        b.setFb(b.getFb() - 1 + Math.min(a.getFb(), 0));
    }
    //##----------------FIM DO BALANCEAMENTO----------------##//

    //##----------------ROTAÇÕES----------------##//
    //rotação simples a esqueda
    private No rotacaoEsquerda(No a) {
        No b = a.getDireita();
        b.setParente(a.getParente());

        a.setDireita(b.getEsquerda());

        if (a.getDireita() != null) {
            a.getDireita().setParente(a);
        }

        b.setEsquerda(a);
        a.setParente(b);

        if (b.getParente() != null) {
            if (b.getParente().getDireita() == a) {
                b.getParente().setDireita(b);
            } else {
                b.getParente().setEsquerda(b);
            }
        }

        setBalanceRotacaoLeft(a, b);

        return b;
    }

    //rotação simples a direita
    private No rotacaoDireita(No a) {
        No b = a.getEsquerda();
        b.setParente(a.getParente());

        a.setEsquerda(b.getDireita());

        if (a.getEsquerda() != null) {
            a.getEsquerda().setParente(a);
        }

        b.setDireita(a);
        a.setParente(b);

        if (b.getParente() != null) {
            if (b.getParente().getDireita() == a) {
                b.getParente().setDireita(b);
            } else {
                b.getParente().setEsquerda(b);
            }
        }

        setBalanceRotacaoRight(a, b);

        return b;
    }

    //Rotação dupla a Esquerda
    private No rotacaoDuplaEsquerda(No n) {
        n.setEsquerda(rotacaoEsquerda(n.getEsquerda()));
        return rotacaoDireita(n);
    }

    //Rotação dupla a direita
    private No rotacaoDuplaDireita(No no) {
        no.setDireita(rotacaoDireita(no.getDireita()));
        return rotacaoEsquerda(no);
    }
    //##----------------FIM DAS ROTAÇÕES----------------##//

    //##----------------MÉTODOS GENÉRIOS----------------##//
    //execução da verificação dos nos
    public void percorrer(No no) {
        if (no == null) {
            return;//EM CASO DE RAIZ NULA NAO HA O QUE PERCORRER
        }
        System.out.println("Chave:" + no.getChave() + " FB:" + no.getFb());
        percorrer(no.getEsquerda());//SEGUE A VERIFICACAO COM O FILHO ESQUERDO DO NO
        percorrer(no.getDireita()); //SEGUE A VERIFICACAO COM O FILHO DIREITO DO NO       
    }

    // Retorna a altura da árvore
    private static int altura(No t) {
        return t == null ? -1 : t.getHeight();
    }

    //Verificacao para o caso da arvore é vazia
    public Boolean isEmpty() {
        return this.raiz == null;//SETA UMA RAIZ NULA COMO RETORNO
    }

    //recupera a raiz da arvore
    public No getRoot() {
        return raiz;
    }
    //##----------------FIM DOS MÉTODOS GENÉRIOS----------------##//

    //##----------------EXIBIÇÃO GRÁFICA DA ARVORE----------------##//
    protected No buscarPai(int el) {
        No no = raiz;
        No anterior = null;
        while (no != null && !(no.getChave() == el)) {  // acha o nó p com a chave el
            anterior = no;
            if (no.getChave() < el) {
                no = no.getDireita();
            } else {
                no = no.getEsquerda();
            }
        }
        if (no != null && no.getChave() == el) {
            return anterior;
        }
        return null;
    }

    public void displayTree() {
        if (isEmpty()) {
            System.out.println("Árvore vazia!");
            return;
        }
        String separar = String.valueOf("  |__");
        System.out.println(this.raiz.getChave() + "(" + raiz.getHeight() + ")");
        displaySubTree(raiz.getEsquerda(), separar);
        displaySubTree(raiz.getDireita(), separar);
    }

    private void displaySubTree(No no, String separar) {
        if (no != null) {
            No pai = this.buscarPai(no.getChave());
            if (no.equals(pai.getEsquerda()) == true) {
                System.out.println(separar + no.getChave() + "(" + no.getHeight() + ")" + " (ESQ)");
            } else {
                System.out.println(separar + no.getChave() + "(" + no.getHeight() + ")" + " (DIR)");
            }
            displaySubTree(no.getEsquerda(), "     " + separar);
            displaySubTree(no.getDireita(), "     " + separar);
        }
    }
    //##----------------FIM DA EXIBIÇÃO GRÁFICA DA ARVORE----------------##//
}
