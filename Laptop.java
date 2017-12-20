import java.util.*;
import javax.jdo.*;

@javax.jdo.annotations.PersistenceCapable

public class Laptop extends Product
{
	int price; // in dollars
	boolean hasHDScreen; // has a high-definition screen?
	int hardDriveCapacity; // in GB

	Processor processor; // the preinstalled processor
	Memory memory; // the preinstalled memory 
	Company madeBy; // the inverse of Company.makeLaptops

	
	public Laptop(String mn, int p, boolean hd, int hdc)
	{
		modelName = mn;
		price = p;
		hasHDScreen = hd;
		hardDriveCapacity = hdc;	
	}

	public String toString()
	{
		return madeBy.name+" "+modelName+"; "+
		       processor.toString()+"; "+
		       memory.toString()+"; "+
		       "harddrive: "+hardDriveCapacity+" GB";
	}
	
	public static Laptop find(String mName, PersistenceManager pm)

	/* Returns the laptop with the given model name "mName"; returns null if no such laptop exists. 
	   The function is applied to the database held by the persistence manager pm. */

	{
		Query q=pm.newQuery(Laptop.class);
		q.declareParameters("String name");
		q.setFilter("this.modelName==name");
		
		Collection<Laptop> ll= (Collection<Laptop>) q.execute(mName);
		Laptop l = Utility.extract(ll);
		q.close(ll);
		return l;

	}

	public static Collection<Laptop> HDandHardDrive(int x, Query q)

	/* Returns the collection of all laptops that have an HD screen and at least x GB of harddrive.
	   Sort the result by (hardDriveCapacity, modelName). */

	{
		q.setClass(Laptop.class);
		q.declareParameters("int x");
		q.setFilter("this.hasHDScreen==true&&"+
					"this.hardDriveCapacity>=x");
		
		q.setOrdering("this.hardDriveCapacity ascending, this.modelName ascending");
		
		return (Collection<Laptop>) q.execute(new Integer (x));

	}

	public static Collection<Laptop> speedPrice(float c, int p1, int p2, Query q)

	/* Returns the collection of all laptops that have a processor clock speed of at least "c" GHz
           and a price of at least "p1" and at most "p2" dollars.
	   Sort the result by (processor.clockSpeed, price, modelName). */

	{
		q.setClass(Laptop.class);
		q.declareParameters("float cc, int p11, int p22");
		q.setFilter("this.processor.clockSpeed>=cc &&"+
					"this.price>=p11&&"+"this.price<=p22"); //using path expression here
		/*q.declareVariables("Processor p");
		q.setFilter("this.processor.contains(p)&& "+ "p.clockSpeed==cc&&"+
					"this.price>=p11 &&" +"this.price<=p22");*/
		q.setOrdering("this.processor.clockSpeed ascending, this.price ascending, this.modelName ascending");
		return (Collection<Laptop>) q.execute(new Float (c), new Integer (p1), new Integer (p2));
	
	}

	public static Collection<Laptop> hasProcessor(String cName, Query q)

	/* Returns the collection of all laptops that have processors made by
	   the company with the name "cName". Sort the result by (madeBy.name, modelName). */

	{
		q.setClass(Laptop.class);
		q.declareParameters("String c");
		q.setFilter("this.processor.madeBy.name ==c"); //path expression
		q.setOrdering("this.processor.madeBy.name ascending, this.modelName ascending");
		return (Collection<Laptop>) q.execute( cName);
	
	}

	public static Collection<Object[]> laptopProcessorMadeBySameCompany(Query q)

	/* Returns the set of 3-tuples <lt: Laptop, p: Processor, c: Company> such that
	   laptop "lt" is preinstalled with processor "p" and company "c" makes both "lt" and "p". 
	   Sort the result by (c.name, lt.modelName). */

	{
		//setResult
		q.setClass(Laptop.class);
		q.declareVariables("Laptop lt; Processor p; Company c");
		
		q.setFilter("c.makeLaptops.contains(lt)&&"+"c.makeProcessors.contains(p)&&"+
					"lt.processor.contains(p)");
	
		q.setResult("distinct lt , lt.processor, lt.madeBy");// distinct or it will repeat multiple time.
		
		q.setOrdering("lt.madeBy.name ascending, lt.modelName ascending");
		
		return(Collection<Object[]>) q.execute();

	}
	
	public static Collection<Laptop> sameProcessor(Query q)
	
	/* Returns the collection of all laptops each of which has at least one other laptop 
	   preinstalled with the same processor. Sort the result by (madeBy.name, modelName). */
	
	{
		q.setClass(Laptop.class);
		q.declareVariables("Laptop l1"); 
		q.setFilter("this.processor.modelName==l1.processor.modelName&&"+
					"this!=l1");
		q.setOrdering("this.madeBy.name ascending, this.modelName ascending");
		return (Collection <Laptop>) q.execute();
		


	}

	public static Collection<Object[]> groupByCompany(Query q)

	/* Group the laptops by the companies that make them.
	   Then return the set of 4-tuples <c: Company, num: int, minSpeed: float, maxSize: int> where:

	   num = the total number of laptops made by c
	   minSpeed = the minimum clock speed of the processors preinstalled on the laptops made by c
	   maxSize = the maximum memory size of the memories preinstalled on the laptops made by c     

	   Sort the result by c.name. */

	{
		q.setClass(Company.class);
		q.declareVariables("Laptop l; Processor p; Memory m");
		
		q.setFilter("this.makeLaptops.contains(l)&&"+"l.processor.contains(p)&&"+"l.memory.contains(m)");
		q.setGrouping("this");
		q.setResult(" this, count(l.madeBy), min(p.clockSpeed), max(m.size)");
		q.setOrdering("this.name ascending");
		return (Collection<Object[]>) q.execute();
		
	}
}