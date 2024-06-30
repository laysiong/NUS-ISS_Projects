using CA_Project_Activation_Codes.Models;
using MySqlConnector;


namespace CA_Project_Activation_Codes.Data
{
    public class PurchaseDAO:baseDAO
    {
        public static List<Prod_Purchased> GetAllPurchases(string custId)
        {
            List<Prod_Purchased> ProdList = new List<Prod_Purchased>();
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                string sql = @"SELECT 
                                p.TranID, 
                                p.CustID, 
                                p.ProdID,
                                MIN(p.ProdQuantity) AS TotalQuantity, 
                                MAX(td.TimeStamp) AS PurchaseDate,
                                pd.Name AS ProdName, 
                                pd.Description,
                                pd.Price, 
                                pd.Image
                            FROM 
                                purchases p
                            JOIN 
                                productdetails pd ON p.ProdID = pd.Id
                            JOIN 
                                transactiondetails td ON p.TranID = td.TranID
                            WHERE 
                                p.Status = 1 
                                AND p.CustID = @custID
                            GROUP BY
                                p.TranID, 
                                p.CustID, 
                                p.ProdID, 
                                pd.Name, 
                                pd.Description, 
                                pd.Price, 
                                pd.Image;
                            ";

                MySqlCommand cmd = new MySqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@CustID", custId);

                MySqlDataReader reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    Prod_Purchased prodDetail = new Prod_Purchased()
                    {
                        TranId = (string)reader["TranID"],
                        ProdId = (string)reader["ProdID"],
                        ProdName = (string)reader["ProdName"],
                        ProdQuantity = (int)(long)reader["TotalQuantity"],
                        Description = (string)reader["Description"],
                        Dates = (string)reader["PurchaseDate"],
                        Image = (string)reader["Image"]

                    };
                    ProdList.Add(prodDetail);
                }


                conn.Close();
            }

            //request activition code 
            foreach (var prod in ProdList)
            {
                using (MySqlConnection conn2 = new MySqlConnection(connectionString))
                {
                    conn2.Open();
                    string sql = @"select ac.ActivationCode from activationcode ac 
                                  JOIN purchases p ON ac.TranID = p.TranID and ac.ProdID = p.ProdID
                                  WHERE p.TranID=@TranID and p.ProdID=@ProdID and Status = 1;";

                    MySqlCommand cmd = new MySqlCommand(sql, conn2);
                    cmd.Parameters.AddWithValue("@TranID", prod.TranId);
                    cmd.Parameters.AddWithValue("@ProdID", prod.ProdId);

                    MySqlDataReader reader = cmd.ExecuteReader();
                    while (reader.Read())
                    {
                        prod.Add((string)reader["ActivationCode"]);
                    }
                    conn2.Close();
                }
            }



            return ProdList;
        }
    }
}
