package owls.validator.similarity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.process.ParameterList;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Parameter;

public class SimilarityValidator {

	private URI compositionURI;
	OWLKnowledgeBase compositionKB;
	OWLOntology compositionOntology;
	private SimilarityFilter inputFilter;
	private SimilarityFilter outputFilter;
	private SimilarityFilter preconditionFilter;
	private SimilarityFilter resultFilter;
	private ArrayList<String> errorLog;
	
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
 	    compositionOntology = compositionKB.read(compositionURI);
	    OWLIndividualList services = (OWLIndividualList) compositionKB.readAllServices(compositionURI);
	    
	    for (int i = 0; i < services.size(); i++)
	    {
	    	Service service = (Service)services.get(i);
	    	Process process = service.getProcess();
	    	ParameterList parList = process.getParameters();
	    	System.out.println(">"+services.get(i).toString());
	    	for (int j = 0; j < parList.size(); j++)
	    	{
	    		Parameter par = (Parameter) parList.get(j);
	    		System.out.println(">>"+par.toString());
	    	}
	    	
	    }
		FunctionalMatcher functMatcher = new FunctionalMatcher();
		if (inputFilter == null && outputFilter == null && preconditionFilter == null
				&& resultFilter == null)
			return true;
		for (int i = 0; i < 1; i++)
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
				}*/
		}
		return true;
	}
}
