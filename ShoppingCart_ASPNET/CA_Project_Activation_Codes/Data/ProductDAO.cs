using CA_Project_Activation_Codes.Models;
using MySqlConnector;


namespace CA_Project_Activation_Codes.Data
{
    public class ProductDAO:baseDAO
    {
        //Display all products on Gallery
        public static List<Product> GetAllProducts()
        {
            // Intialize new products object
            List<Product> products = new List<Product>();
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                // Start connecting to the database
                conn.Open();

                // SQL query
                string sql = @"SELECT Id, Name, Description, Price, Image FROM ProductDetails";

                MySqlCommand cmd = new MySqlCommand(sql, conn);
                MySqlDataReader reader = cmd.ExecuteReader();

                while (reader.Read())
                {
                    Product product = new Product()
                    {
                        ProdId = (string)reader["Id"],
                        ProdName = (string)reader["Name"],
                        Description = (string)reader["Description"],
                        Price = (double)reader["Price"],
                        Image = (string)reader["Image"]
                    };
                    products.Add(product);
                }
                conn.Close();
            }

            return products;
        }


        public static void addToCart(string ProdId, string custId, string custTranId)
        {
            try
            {
                using (MySqlConnection conn3 = new MySqlConnection(connectionString))
                {
                    conn3.Open();
                    
                    //To check if item exist
                    string sql = @"SELECT COUNT(*) FROM purchases WHERE ProdID = @prodid AND CustID = @custid AND tranid = @tranid";
                    MySqlCommand cmd = new MySqlCommand(sql, conn3);
                    cmd.Parameters.AddWithValue("@prodid", ProdId);
                    cmd.Parameters.AddWithValue("@custid", custId);
                    cmd.Parameters.AddWithValue("@tranid", custTranId);

                    int rowCount = Convert.ToInt32(cmd.ExecuteScalar());
                    if (rowCount > 0)
                    {
                        // Record exists, update the quantity
                        string sqlUpdate = @"
                            UPDATE purchases 
                            SET ProdQuantity = ProdQuantity + 1
                            WHERE prodid = @prodid AND custid = @custid AND status = 0";
                        MySqlCommand cmdUpdate = new MySqlCommand(sqlUpdate, conn3);
                        cmdUpdate.Parameters.AddWithValue("@prodid", ProdId);
                        cmdUpdate.Parameters.AddWithValue("@custid", custId);
                        cmdUpdate.Parameters.AddWithValue("@tranid", custTranId);
                        cmdUpdate.ExecuteNonQuery();
                    }
                    else
                    {
                        // Record doesn't exist, insert a new record
                        string insertSql = @"
                            INSERT INTO purchases (TranID, CustID, ProdID, ProdQuantity, Status) 
                            VALUES (@tranid, @custid, @prodid, 1, 0)";
                        MySqlCommand insertCmd = new MySqlCommand(insertSql, conn3);
                        insertCmd.Parameters.AddWithValue("@tranid", custTranId);
                        insertCmd.Parameters.AddWithValue("@custid", custId);
                        insertCmd.Parameters.AddWithValue("@prodid", ProdId);
                        insertCmd.ExecuteNonQuery();
                    }

                    conn3.Close();
                }
            }
            catch (MySqlException ex)
            {
                Console.WriteLine("An error occurred: " + ex.Message);
            }
        }   
    }
}
