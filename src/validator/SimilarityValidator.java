package validator;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.variable.InputBinding;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.ValueForm;
import org.mindswap.owls.process.ValueFunction;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ParameterValueVisitor;
import org.mindswap.owls.process.variable.ValueConstant;
import org.mindswap.owls.process.variable.ValueOf;
import owlsapi_validation.ParamValueVisitor;


public class SimilarityValidator {

	private URI compositionURI;
	OWLKnowledgeBase compositionKB;
	OWLOntology compositionOntology;
	private SimilarityFilter inputFilter;
	private SimilarityFilter outputFilter;
	private SimilarityFilter preconditionFilter;
	private SimilarityFilter resultFilter;
	private ArrayList<String> errorLog;

    public ArrayList<String> getErrorLog()
    {
        return errorLog;
    }
	
	public SimilarityValidator (URI compURI, SimilarityFilter inFilter, SimilarityFilter outFilter,
			SimilarityFilter preFilter, SimilarityFilter resFilter)
	{
		compositionURI = compURI; 
		inputFilter = inFilter;
		outputFilter = outFilter;
		preconditionFilter = preFilter;
		resultFilter = resFilter;
		errorLog = new ArrayList<String>();
		
	}
	
	public boolean validate() throws Exception 
	{
            System.out.println("INICIANDO VALIDACAO DE SIMILARIDADE");
            //-------
            compositionKB = OWLFactory.createKB();
            compositionKB.getReader().setIgnoreFailedImport(true);
 	    //compositionOntology = compositionKB.read(new URI("http://on.cs.unibas.ch/owl-s/1.2/BravoAirService.owl"));
	    OWLIndividualList<Service> services = compositionKB.readAllServices(new URI("http://on.cs.unibas.ch/owl-s/1.2/FindCheaperBook.owl"));
	    System.out.println("ITERANDO NOS SERVIÇOS");
            if (services.size() == 0)
            {
                errorLog.add("[ERRO] Nenhum serviço foi encontrado.");
                return false;
            }
            else
            {
	    	Process process = services.get(0).getProcess();
                if (!(process instanceof CompositeProcess))
                {
                    errorLog.add("[ERRO] Nenhuma composição de serviços foi encontrada.");
                    return false;
                }
                else
                {
                    CompositeProcess compositeProcess = (CompositeProcess) process;
                    ControlConstruct controlConstruct = compositeProcess.getComposedOf();
                    if (controlConstruct == null)
                    {
                        errorLog.add("[ERRO] Nenhuma estrutura de composição foi encontrada.");
                        return false;
                    }
                    else
                    {
                        if (!(controlConstruct instanceof Sequence))
                        {
                            errorLog.add("[ERRO] A composição deve ser formada incialmente por um SEQUENCE.");
                            return false;
                        }
                        else
                        {
                            Sequence sequence = (Sequence) controlConstruct;
                            System.out.println("ANTES");
                            for (int j = 0; j < sequence.getAllBindings().size();j++)
                            {
                                
                                //System.out.println("toInput = "+sequence.getAllBindings().get(j).getProcessVar().getURI());
                                //System.out.println(
                                ParamValueVisitor visitor = new ParamValueVisitor();
                                sequence.getAllBindings().get(j).getValue().accept(visitor);
                                //System.out.println("fromOutput = "+visitor.getFromSource());
                                System.out.println("TO "+sequence.getAllBindings().get(j).getProcessVar());
                            }

                            OWLIndividualList<ControlConstruct> controlConstructList = sequence.getConstructs();
                            if (controlConstructList == null)
                            {
                                errorLog.add("[ERRO] Não existe elementos no SEQUENCE.");
                                return false;
                            }
                            else
                            {
                                return validateComposition(controlConstructList);
                            }
                        }
                    }
                }
            }
	}

    private boolean validateComposition(OWLIndividualList<ControlConstruct> controlConstructList)
    {

        for (int i = 0; i < controlConstructList.size(); i++)
        {
             URI fromOutput, toInput;
             fromOutput = null;
             toInput = null;
             ControlConstruct cc = controlConstructList.get(i);
            if (cc instanceof Perform)
            {
                System.out.println("----PERFORM-----");
              Perform perf = (Perform) cc;
                System.out.println("----BINDINGS-----");
                for (int j = 0; j < perf.getBindings().size();j++)
                {
                    System.out.println(perf.getBindings().get(j).getProcessVar());
                    toInput = perf.getBindings().get(j).getProcessVar().getURI();
                    //System.out.println(
                    ParamValueVisitor visitor = new ParamValueVisitor();
                    perf.getBindings().get(j).getValue().accept(visitor);
                    fromOutput = visitor.getFromSource();
                }
                //System.out.println("----INPUTS-----");
                for (int j = 0; j < perf.getProcess().getInputs().size();j++)
                {
                   // System.out.println(perf.getProcess().getInputs().get(j));
                }
                //System.out.println("----OUTPUTS-----");
                for (int j = 0; j < perf.getProcess().getOutputs().size();j++)
                {
                   // System.out.println(perf.getProcess().getOutputs().get(j));
                }
            }
            else if (cc instanceof SplitJoin)
            {
                System.out.println("----SPLITJOIN-----");
                SplitJoin splitJoin = (SplitJoin) cc;
                //System.out.println("----BINDINGS-----");
                for (int j = 0; j < splitJoin.getAllBindings().size();j++)
                {
                    //System.out.println("p "+splitJoin.getAllBindings().get(j));
                }
                System.out.println("----BINDINGS-----");
                for (int j = 0; j < splitJoin.getAllBindings().size();j++)
                {
                    System.out.println(splitJoin.getAllBindings().get(j).getProcessVar());
                    toInput = splitJoin.getAllBindings().get(j).getProcessVar().getURI();
                    //System.out.println(
                    ParamValueVisitor visitor = new ParamValueVisitor();
                    splitJoin.getAllBindings().get(j).getValue().accept(visitor);
                    fromOutput = visitor.getFromSource();
                }

                //System.out.println("----INPUTS-----");
                for (int j = 0; j < splitJoin.getAllProcesses(true).size();j++)
                {
                    for (int k = 0; k < splitJoin.getAllProcesses(true).get(j).getInputs().size(); k++);
                      //  System.out.println(splitJoin.getAllProcesses(true).get(j).getInputs().get(k));
                }
                //System.out.println("----OUTPUTS-----");
                for (int j = 0; j < splitJoin.getAllProcesses(true).size();j++)
                {
                    for (int k = 0; k < splitJoin.getAllProcesses(true).get(j).getOutputs().size(); k++);
                        //System.out.println(splitJoin.getAllProcesses(true).get(j).getOutputs().get(k));
                }
            }
            System.out.println("IN: "+fromOutput+"\nOUT: "+toInput);
            
        FunctionalMatcher functMatcher = new FunctionalMatcher();
        if (inputFilter == null && outputFilter == null && preconditionFilter == null
                        && resultFilter == null)
                return true;      
      int simIODegree = functMatcher.CalculateDegree(fromOutput, toInput, FunctionalMatcher.INPUT);
      System.out.println("GRAU "+simIODegree);

                if (inputFilter != null)
                        if (!inputFilter.isAcceptable(simIODegree))
                        {
                                //return false;
                                errorLog.add("[ERROR][INPUT FILTER] "+fromOutput.toString()+" and "+
                                                toInput.toString()+" are "+inputFilter.getTestedBy());
                        }
                if (outputFilter != null)
                        if (!outputFilter.isAcceptable(simIODegree))
                        {
                                //return false;
                                errorLog.add("[ERROR][OUTPUT FILTER] "+fromOutput.toString()+" and "+
                                                toInput.toString()+" are "+outputFilter.getTestedBy());
                        }
        }
        /*for (int i = 0; i < 1; i++)
        {
                URI fromOutput, toInput;
                fromOutput = null;
                toInput = null;
                fromOutput = new URI("http://seila.com");
                toInput = new URI("http://seila.com");
                //input output similarity matcher
                int simIODegree = functMatcher.CalculateDegree(fromOutput, toInput, FunctionalMatcher.INPUT);
                if (inputFilter != null)
                        if (!inputFilter.isAcceptable(simIODegree))
                        {
                                //return false;
                                errorLog.add("[ERROR][INPUT FILTER]"+fromOutput.toString()+" and "+
                                                toInput.toString()+" are not "+inputFilter.getTestedBy());
                        }
                if (outputFilter != null)
                        if (!outputFilter.isAcceptable(simIODegree))
                        {
                                //return false;
                                errorLog.add("[ERROR][OUTPUT FILTER]"+fromOutput.toString()+" and "+
                                                toInput.toString()+" are not "+outputFilter.getTestedBy());
                        }
/*
                if (preconditionFilter != null)
                        if (!preconditionFilter.isAcceptable(simDegree))
                        {
                                //return false;
                                errorLog.add("[ERROR][PRECONDITION FILTER]"+fromOutput.toString()+" and "+
                                                toInput.toString()+" are not "+preconditionFilter.getTestedBy());
                        }
                if (resultFilter != null)
                        if (!resultFilter.isAcceptable(simDegree))
                        {
                                //return false;
                                errorLog.add("[ERROR][RESULT FILTER]"+fromOutput.toString()+" and "+
                                                toInput.toString()+" are not "+resultFilter.getTestedBy());
                        }
            }*/
        return (errorLog.isEmpty());
    }
}
