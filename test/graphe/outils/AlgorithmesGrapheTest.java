package graphe.outils;

import graphe.impl.Graphe;
import graphe.modele.IGraphe;

public class AlgorithmesGrapheTest extends AbstractAlgorithmesGrapheTest {

    @Override
    protected IGraphe creerGrapheVide() {
        return new Graphe();
    }
}