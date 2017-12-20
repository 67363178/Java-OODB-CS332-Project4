import java.util.*;
import javax.jdo.*;
import com.objectdb.Utilities;

public class Test2
{
	public static void main(String argv[])
	{
		PersistenceManager pm = Utilities.getPersistenceManager( "laptop2.odb" );

		System.out.println( "-- TEST Laptop.find --\n" );
		Laptop xmp = Laptop.find( "Cyber Force XMP 3009", pm );
		Laptop dual = Laptop.find( "Dual Power A554", pm );

		System.out.println( xmp );
		System.out.println( dual );
		System.out.println();

		System.out.println( "-- TEST Laptop.HDandHardDrive --\n" );
		Query q = pm.newQuery();
		Collection<Laptop> ll = Laptop.HDandHardDrive(700, q);
		Utility.printCollection( ll );
		q.closeAll();
		System.out.println();

		System.out.println( "-- TEST Laptop.speedPrice --\n" );
		q = pm.newQuery();
		ll = Laptop.speedPrice(8.0f, 500, 1000, q);
		Utility.printCollection( ll );
		q.closeAll();
		System.out.println();

		System.out.println( "-- TEST Laptop.hasProcessor --\n" );
		q = pm.newQuery();
		ll = Laptop.hasProcessor("Delion", q);
		Utility.printCollection( ll );
		q.closeAll();
		System.out.println();
		ll = Laptop.hasProcessor("BBN", q);
		Utility.printCollection( ll );
		q.closeAll();
		System.out.println();

		System.out.println( "-- TEST Laptop.laptopProcessorMadeBySameCompany --\n" );
		q = pm.newQuery();
		Collection<Object[]> tuples = Laptop.laptopProcessorMadeBySameCompany(q);
		Utility.printCollectionOfArrays( tuples );
		q.closeAll();
		System.out.println();
		
		System.out.println( "-- TEST Laptop.sameProcessor --\n" );
		q = pm.newQuery();
		ll = Laptop.sameProcessor(q);
		Utility.printCollection( ll );
		q.closeAll();
		System.out.println();
		
		System.out.println( "-- TEST Processor.installedOn --\n" );
		q = pm.newQuery();
		ll = xmp.processor.installedOn(q);
		Utility.printCollection( ll );
		q.closeAll();
		System.out.println();

		System.out.println( "-- TEST Memory.installedOn --\n" );
		q = pm.newQuery();
		ll = dual.memory.installedOn(q);
		Utility.printCollection( ll );
		q.closeAll();
		System.out.println();

		System.out.println( "-- TEST Company.memoryProcessor --\n" );
		q = pm.newQuery();
		Collection<Company> cc = Company.memoryProcessor(7.5f, 12, q);
		Utility.printCollection( cc );
		q.closeAll();
		System.out.println();

		System.out.println( "-- TEST Company.differentCompanyProcessor --\n" );
		q = pm.newQuery();
		cc = Company.differentCompanyProcessor(q);
		Utility.printCollection( cc );
		q.closeAll();
		System.out.println();

		System.out.println( "-- TEST Laptop.groupByCompany --\n" );
		q = pm.newQuery();
		tuples = Laptop.groupByCompany(q);
		Utility.printCollectionOfArrays( tuples );
		q.closeAll();


		if ( !pm.isClosed() )
        		pm.close();
	}
}