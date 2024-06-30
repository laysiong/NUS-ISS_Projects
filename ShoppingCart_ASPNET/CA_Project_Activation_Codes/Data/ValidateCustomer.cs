using CA_Project_Activation_Codes.Models;
using MySql.Data.MySqlClient;

namespace CA_Project_Activation_Codes.Data
{
    public class ValidateCustomer
    {
        // method CheckPassword is to check whether the customer input password
        // eaquls the one in our database
        public static string? CheckPassword(string input_name, string input_pwd)
        {
            // set connectionString
            string connectionString = @"server=localhost;uid=root;pwd=root;database=testdata";
            Customer customer = new Customer();
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                string sql = @"SELECT * FROM customers WHERE 
                    Username = " + "\"" + input_name + "\""
                    + " AND Password = " + "\"" + input_pwd + "\"";
                MySqlCommand cmd = new MySqlCommand(sql, conn);
                MySqlDataReader dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    customer = new Customer()
                    {
                        id = (string)dr["CustID"],
                        name = (string)dr["Username"],
                        pwd = (string)dr["Password"],
                    };
                }
                // if found --> valid
                if (customer.id != null) { return customer.id; }
                conn.Close();
            }
            return null;
        }

    }
}
