import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class CreateDBRecords {

	public static void main(String[] args) throws IOException {

		OutputStream output = new FileOutputStream("db-inserts.sql");

//		while(moreData) {
//			  int data = getMoreData();
//			  output.write(data);
//			}
			
		output.write(String.valueOf("use bp_rds_db;\n").getBytes());


			output.write(String.valueOf("insert into BalmoProductCode (id, type, name) values (1, 'Liquid', 'Oil');\n").getBytes());
		output.write(String.valueOf("insert into BalmoProductCode (id, type, name) values (2, 'Aerium', 'Gas');\n").getBytes()); 
		output.write(String.valueOf("insert into BalmoProductCode (id, type, name ) values (3, 'Liquid', 'Petrol');\n").getBytes()); 

		output.write(String.valueOf("insert into FormulaLine (id, type, name) values (1, 'Extractable', 'Extractable');\n").getBytes()); 

		output.write(String.valueOf("insert into FormulaPrice (id, price, name) values (1, '150', 'OilPrice\\Barrel');\n").getBytes()); 
		output.write(String.valueOf("insert into FormulaPrice (id, price, name) values (2, '7', 'Gas\\Cubic Metre');\n").getBytes()); 
		output.write(String.valueOf("insert into FormulaPrice (id, price, name) values (3, '2', 'Petro\\Litre');\n").getBytes()); 

		int tmp = 1;
		for (int i = 1; i < 15000; i++){
//			"insert into DerivativeProduct (id, name, type, phone_number) values (0, 'John Smith', 'john.smith@mailinator.com', '2125551212')"; 
			
			int fk = 0;
			if (tmp == 1) {
				fk = 1;
			} else if (tmp == 2) {
				fk = 2;
			} else {
				tmp = 1;
				fk = 3;
			}
			output.write(String.valueOf("insert into DerivativeProduct (id, name, type, product_code, formula_line, formula_price) "
					+ "values ('"+i+"', 'DerivProduct-"+i+"', 'product-"+i+"', '"+fk+"', '1', '"+fk+"');\n").getBytes()); 
			tmp++;
		}
		
		output.close();

		
	}

}
