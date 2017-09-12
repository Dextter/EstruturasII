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
public class AVL {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Arvore t = new Arvore(); 
         t.inserir(1);
            t.inserir(2);
            t.inserir(3);
            t.inserir(4);
            t.inserir(5);
            t.inserir(6);
            t.inserir(7);
            t.inserir(8);
            t.inserir(9);
            t.displayTree();
    }
    
}
