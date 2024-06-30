using CA_Project_Activation_Codes.Models;
using Org.BouncyCastle.Asn1.Ocsp;
using MySqlConnector;
using Org.BouncyCastle.Asn1.Crmf;


namespace CA_Project_Activation_Codes.Data
{
    public class CartDAO:baseDAO
    {

        public static List<Cart_Prod> GetAllCart(string custId)
        {

            List<Cart_Prod> CartList = new List<Cart_Prod>();
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                string sql = @"select * from purchases p
                           JOIN productdetails pd ON p.ProdID = pd.Id
                           WHERE p.Status = 0 and p.CustID = @custID";

                MySqlCommand cmd = new MySqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@CustID", custId);

                MySqlDataReader reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    Cart_Prod cartItem = new Cart_Prod()
                    {
                        TranId = (string)reader["TranID"],
                        ProdId = (string)reader["ProdID"],
                        ProdName = (string)reader["Name"],
                        ProdQuantity = (int)(long)reader["ProdQuantity"],
                        Description = (string)reader["Description"],
                        Price = (double)reader["Price"],
                        Image = (string)reader["Image"]

                    };
                    CartList.Add(cartItem);
                }
                conn.Close();

                return CartList;
            }
        }


        public static void checkOut(string custTranId,string custId,string insertACode, List<Cart_Prod> cartItem)
        {
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                conn.Open();

                //INSERT TransactionDetail DB (GET TRAN ID, Timestamp)
                //INSERT INTO TransactionDetail (TranID, TimeStamp) 
                //VALUES
                //(@tranID,now)

                string insertTransactionDetailSql = "INSERT INTO transactiondetails (TranID, TimeStamp) VALUES (@tranID, NOW())";
                MySqlCommand insertTransactionDetailCmd = new MySqlCommand(insertTransactionDetailSql, conn);
                insertTransactionDetailCmd.Parameters.AddWithValue("@tranID", custTranId);
                insertTransactionDetailCmd.ExecuteNonQuery();

                //UPDATE Purchase DB - CHECK OUT FULLY
                // UPDATE purchases 
                //SET status = 1
                //WHERE prodid = @prodid AND custid = @custid AND TranID = @tranID; 

                foreach (var cart in cartItem)
                {
                    string updatePurchaseSql = "UPDATE purchases SET status = 1 WHERE prodid = @prodid AND custid = @custid AND TranID = @tranID";
                    MySqlCommand updatePurchaseCmd = new MySqlCommand(updatePurchaseSql, conn);
                    updatePurchaseCmd.Parameters.AddWithValue("@custid", custId);
                    updatePurchaseCmd.Parameters.AddWithValue("@tranID", custTranId);
                    updatePurchaseCmd.Parameters.AddWithValue("@prodid", cart.ProdId);
                    updatePurchaseCmd.ExecuteNonQuery();

                }

                //this one need forloop
                //INSERT INTO activationcode (ActivationCode, TranID, ProdID) 
                //VALUES
                //(ActivationCode(),@tranID,@ProdID)
                //Console.WriteLine(insertACode);

                string insertActivationCodeSql = "INSERT INTO activationcode (ActivationCode, TranID, ProdID) VALUES" + insertACode.TrimEnd(',') + ";";
                MySqlCommand insertActivationCodeCmd = new MySqlCommand(insertActivationCodeSql, conn);
                insertActivationCodeCmd.ExecuteNonQuery();

                conn.Close();

            }
            
        }


    }
}
