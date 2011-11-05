/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package owlsapi_validation;

import java.net.URI;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ParameterValueVisitor;
import org.mindswap.owls.process.variable.ValueConstant;
import org.mindswap.owls.process.variable.ValueOf;

/**
 *
 * @author Efraim Machado
 */
public class ParamValueVisitor implements ParameterValueVisitor {

    URI fromSource;

    public URI getFromSource() {
        return fromSource;
    }

    public void visit(ValueOf vo) {
        // get process:fromProcess
        Perform sourcePerform = vo.getPerform();
        // get process:theVar
        Parameter sourceParam = vo.getParameter();
        System.out.println("OMG " + sourceParam);
        fromSource = sourceParam.getURI();
    }

    public void visit(ValueConstant vc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void visit(org.mindswap.owls.process.variable.ValueForm vf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void visit(org.mindswap.owls.process.variable.ValueFunction<?> vf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
