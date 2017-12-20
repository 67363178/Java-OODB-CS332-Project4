import javax.jdo.*;

@javax.jdo.annotations.PersistenceCapable

public abstract class Product
{
	String modelName; // key

	
	public String toString()
	{
		return modelName;
	}	
}