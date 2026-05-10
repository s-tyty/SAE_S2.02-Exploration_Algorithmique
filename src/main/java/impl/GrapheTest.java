package graphe.impl;


import graphe.modele.IGraphe;
import graphe.modele.AbstractIGrapheTest;

public class GrapheTest extends AbstractIGrapheTest {

    @Override
    protected IGraphe creerGrapheVide() {
        return new Graphe();
    }
}