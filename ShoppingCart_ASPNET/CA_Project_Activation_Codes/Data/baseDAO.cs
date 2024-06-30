using MySqlConnector;


namespace CA_Project_Activation_Codes.Data
{
    public class baseDAO
    {
        protected static string connectionString = @"server=localhost;uid=root;pwd=root;database=testdata";


         public static int getCartCount(string CustID)
        {

            int totalQuantity = 0;

            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                // Start connecting to the database
                conn.Open();

                // SQL query
                string sql = @"SELECT SUM(ProdQuantity) AS TotalQuantity FROM purchases WHERE CustID = @custId AND Status = 0;";
                MySqlCommand cmd = new MySqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@custId", CustID);

                // Execute the SQL command and retrieve the sum
                object result = cmd.ExecuteScalar();
                if (result != null && result != DBNull.Value)
                {
                    totalQuantity = Convert.ToInt32(result);
                }

                // Close the connection
                conn.Close();
            }

            // Convert the total quantity to a string and return it
            return totalQuantity;
        }





        public static string getTranID(string CustID)
        {
            string TranID = "";

            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                // Start connecting to the database
                conn.Open();

                // SQL query LAST PAY TRANSACTION
                string sql = @"select TranID from purchases where CustID = @custId  and Status = 1 Order By TranID DESC LIMIT 1";
                MySqlCommand cmd = new MySqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@custId", CustID);

                // Execute the SQL command and retrieve the sum
                object result = cmd.ExecuteScalar();
                if (result != null && result != DBNull.Value)
                {
                    TranID = Convert.ToString(result);
                }

                // Close the connection
                conn.Close();
            }
            if (String.IsNullOrEmpty(TranID))
            {
                return CustID + "T1";
            }


            return setranID(TranID);
        }

        // SET TRANID 
        public static string setranID(string TranID)
        {
            // Split the input string into prefix and numeric part
            string[] parts = TranID.Split('C', 'T');

            if (parts.Length == 3 && int.TryParse(parts[2], out int number))
            {
                // Increment the numeric part
                number++;

                // Reconstruct the string
                return $"C{parts[1]}T{number}";
            }

            // Return the original input string if the format is not as expected
            return TranID;
        }


        public static List<string> checkCart_ProdType(string UserId)
        {
            string custId = UserId;
            List<string> MyCartList = new List<string>();
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                string sql = @"SELECT DISTINCT ProdID from  purchases
                              where CustID = @custID and 
                              Status = 0";

                MySqlCommand cmd = new MySqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@custID", custId);

                MySqlDataReader reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    MyCartList.Add((string)reader["ProdID"]);
                }
                conn.Close();

            }
            return MyCartList;
            //PrintList(CustCartList);
        }


        public static void compareCart_ProdType(string CustID, string TranID,
            List<string> CustList, string sessionID, List<string> SessionList)
        {
            int temp_count = 0;
            //if custList is empty, can go straight to swap
            if (CustList.Count < 0)
            {
                Console.WriteLine("CustList No Item");
            }

            foreach (var item in SessionList)
            {
                temp_count++;
                //Console.WriteLine(temp_count);
                //Only care which item exist or anot
                if (CustList.Contains(item))
                {
                    //Console.WriteLine("item exist");
                    updateToCartItem(CustID, TranID, item, sessionID);
                }
                else
                {
                    //Console.WriteLine("new item");
                    idSwapToCartItem(CustID, TranID, item, sessionID);
                }
            }

        }



        public static void PrintList(List<string> MyList)
        {
            foreach (string item in MyList)
            {
                Console.Write("," + item);
            }
        }

        public static void idSwapToCartItem(string CustID, string TranID, string ProdID, string sessionID)
        {
            //Console.WriteLine("UPDATE purchases SET CustID={2}, TranID={3} " +
            //"where CustID={0} and ProdID={1} and Status=0;", sessionID, ProdID, CustID, TranID);

            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                // Start connecting to the database
                conn.Open();

                // SQL query LAST PAY TRANSACTION
                string sql = @"UPDATE purchases
                        SET CustID = @CustID,
                            TranID = @TranID
                        WHERE CustID = @sessionID AND ProdID = @ProdID AND status = 0";
                MySqlCommand cmd = new MySqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@sessionID", sessionID);
                cmd.Parameters.AddWithValue("@CustID", CustID);
                cmd.Parameters.AddWithValue("@TranID", TranID);
                cmd.Parameters.AddWithValue("@ProdID", ProdID);


                cmd.ExecuteNonQuery();

                // Execute the SQL command and retrieve the sum

                // Close the connection
                conn.Close();
            }
            //Console.WriteLine("Close idSwapToCartItem");

        }


        public static void updateToCartItem(string CustID, string TranID, string ProdID, string sessionID)
        {
            //Console.WriteLine(
            //    "UPDATE purchases p SET p.ProdQuantity = p.ProdQuantity "+
            //    "+ (SELECT ProdQuantity FROM purchases p2 WHERE p2.CustID={0} AND p2.ProdID={1} AND p2.Status=0 ) " +
            //    "WHERE p.CustID={2} AND p.ProdID={3} AND p.TranID={4} AND p.Status=0 "
            //    , sessionID, ProdID, CustID, ProdID, TranID);

            //Console.WriteLine("DELETE from purchases where CustID ={0} and ProdID ={1}", sessionID, ProdID);

            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                // Start connecting to the database
                conn.Open();

                //Update Product Quantity For existing item to Customers Cart List
                string update_sql = @"UPDATE purchases as p JOIN (
                                        SELECT CustID, ProdID, ProdQuantity
                                        FROM purchases 
                                        WHERE CustID = @sessionID 
                                        AND ProdID = @ProdID  
                                        AND Status = 0
                                    ) AS p2 ON p.CustID = @CustID 
                                                AND p.ProdID = @ProdID 
                                                AND p.TranID = @TranID 
                                                AND p.Status = 0
                                SET p.ProdQuantity = p.ProdQuantity + p2.ProdQuantity";
                MySqlCommand updateCartCartList = new MySqlCommand(update_sql, conn);
                updateCartCartList.Parameters.AddWithValue("@sessionID", sessionID);
                updateCartCartList.Parameters.AddWithValue("@CustID", CustID);
                updateCartCartList.Parameters.AddWithValue("@TranID", TranID); // incase?
                updateCartCartList.Parameters.AddWithValue("@ProdID", ProdID);
                updateCartCartList.ExecuteNonQuery();

                //Delete Session ID Transaction
                string delete_sql = @"DELETE from purchases where CustID =@sessionID and ProdID =@ProdID";
                MySqlCommand deleteSessionTran = new MySqlCommand(delete_sql, conn);
                deleteSessionTran.Parameters.AddWithValue("@sessionID", sessionID);
                deleteSessionTran.Parameters.AddWithValue("@ProdID", ProdID);
                deleteSessionTran.ExecuteNonQuery();

                // Close the connection
                conn.Close();
            }

            //Console.WriteLine("Close updateToCartItem");
        }








    }

}
