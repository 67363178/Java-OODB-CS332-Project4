import java.util.*;
import javax.jdo.*;

@javax.jdo.annotations.PersistenceCapable

public class Memory extends Product
{
	int size; // memory size in GB


	public Memory(String mn, int s)
	{
		modelName = mn;
		size = s;	
	}

	public String toString()
	{
		return modelName+" "+size+" GB";
	}

	public Collection<Laptop> installedOn(Query q)

	/* Returns the collection of all laptops on which the target memory is preinstalled. 
	   Represents the inverse of Laptop.memory. Sort the result by (madeBy.name, modelName). */

	{
		q.setClass(Laptop.class);
		q.declareParameters("Memory m");
	
		q.setFilter("this.memory==m");
		q.setOrdering("this.madeBy.name ascending, this.modelName ascending");
		return (Collection<Laptop>) q.execute(this);
	
	}
}