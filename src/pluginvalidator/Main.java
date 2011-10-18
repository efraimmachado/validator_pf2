/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pluginvalidator;

import java.io.File;
import java.net.URI;
import validator.SimilarityFilter;
import validator.SimilarityValidator;

/**
 *
 * @author Administrador
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        SimilarityFilter i, o, p, e;
        i = new SimilarityFilter(true,false,false,false,false);//exact
        o = new SimilarityFilter(true,false,false,false,false);//exact
        p = null;//new SimilarityFilter(true,false,false,false,false);
        e = null;//new SimilarityFilter(true,false,false,false,false);
        System.out.println("INICIANDO...");
        File entrada = new File("E:\\Minhas Pastas\\Efraim\\validator_pf2\\testes\\teste.owl");
        //URI compositionURI = new URI("http://localhost/Calculator/owls/somaSubComp.owl");//entrada.toURI();
        URI compositionURI = new URI("http://on.cs.unibas.ch/owl-s/1.2/FindCheaperBook.owl");
        System.out.println("INICIANDO VALIDADOR");
        SimilarityValidator simValidator = new SimilarityValidator(compositionURI, i,o,p,e);

        if (simValidator.validate())
        {
                System.out.println("Sem erros de similaridade");
                //dataflowValidator
        }
        else
        {
            for (int index = 0; index < simValidator.getErrorLog().size(); index++)
            {
                System.out.println(simValidator.getErrorLog().get(index));
            }

        }

        System.out.println("FINALIZANDO...");
    }

}
