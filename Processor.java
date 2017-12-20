import java.util.*;
import javax.jdo.*;

@javax.jdo.annotations.PersistenceCapable

public class Processor extends Product
{
	float clockSpeed; // in gigahertz (GHz)

	Company madeBy; // the inverse of Company.makeProcessors


	public Processor(String mn, float cs)
	{
		modelName = mn;
		clockSpeed = cs;	
	}

	public String toString()
	{
		return madeBy.name+" "+modelName+" "+clockSpeed+" GHz";
	}
	
	public Collection<Laptop> installedOn(Query q)

	/* Returns the collection of all laptops on which the target processor is pre-installed. 
	   Represents the inverse of Laptop.processor. Sort the result by (madeBy.name, modelName). */
	{
		q.setClass(Laptop.class);
		q.declareParameters("Processor p");
		q.setFilter("this.processor==p");
		
		q.setOrdering("this.madeBy.name ascending,this.modelName ascending");
		return (Collection<Laptop>) q.execute(this);
	
	}
}