using CA_Project_Activation_Codes.Data;
using CA_Project_Activation_Codes.Models;
using Google.Protobuf.WellKnownTypes;
using Microsoft.AspNetCore.Mvc;
using MySqlConnector;
using System;
using System.Collections.Generic;
using System.Data;
using System.Security.Cryptography;
using System.Text;
using static Org.BouncyCastle.Asn1.Cmp.Challenge;

namespace CA_Project_Activation_Codes.Controllers;

public class CartController : Controller
{
    //global value
    string connectionString = @"server=localhost;uid=root;pwd=root;database=testdata";
    private string custTranId;
    string custId;
    //hello

    public IActionResult Index()
    {

        ISession sessionObject = HttpContext.Session;
        string? useridInSession = sessionObject.GetString("userid");
        ViewData["LoginStatus"] = useridInSession == null ? "display" : "hide";
        ViewData["LogoutStatus"] = useridInSession == null ? "hide" : "display";

        List<Cart_Prod> CartList = GetAllCart();
        ViewBag.CartList = CartList;

        ViewData["Cart"] = CartList.Count < 0 ? "display" : "hide";

        return View();
    }

    //Generate code
    public string GenActivationCode()
    {
        string ActiviationCode = Guid.NewGuid().ToString().Substring(0, 20).ToUpper();
        return ActiviationCode;
    }


    //Check Out Purchases
    public IActionResult CheckOut(List<Cart_Prod> cartItem)
    {
        //Console.WriteLine("Check Out");

        // if customer hasn't logged in redirect to login page
        ISession sessionObject = HttpContext.Session;
        string? useridInSession = sessionObject.GetString("userid");
        string? usernameInSession = sessionObject.GetString("username");

        if (useridInSession == null)
        {
            return RedirectToAction("Index", "Home");
        }
        else
        {
            custId = useridInSession;
            custTranId = baseDAO.getTranID(custId);
        }

        string now = DateTime.Now.ToString();
        string insertACode = "";
        var response = new { success = false, customerName = usernameInSession };

        //to form the string of sql insert code to activationcode (table)
        foreach (var cart in cartItem)
        {
            if (cart.ProdQuantity > 1)
            {
                for (int i = 0; i < cart.ProdQuantity; i++)
                {
                    insertACode += "(\"" + GenActivationCode() + "\",\"" + custTranId + "\",\"" + cart.ProdId + "\"),";
                }
            }
            else
            {
                insertACode += "(\"" + GenActivationCode() + "\",\"" + custTranId + "\",\"" + cart.ProdId + "\"),";
            }
        }


        try
        {
            CartDAO.checkOut(custTranId,custId,insertACode,cartItem);
        }
        catch (MySqlException ex)
        {
            //Console.WriteLine("An error occurred: " + ex.Message);
            response = new { success = false, customerName = usernameInSession }; // Create the response object
            return Ok(response); // Return the response immediately
        }


        response = new { success = true, customerName = usernameInSession }; // You can customize this response as needed
        return Ok(response);
    }


    public List<Cart_Prod> GetAllCart()
    {
        //Cart will show even if customer haven't log in
        string? useridInSession = HttpContext.Session.GetString("userid");
        String SessionID = FindSessionId.GetSessionId(Request, Response);
        custId = useridInSession == null ? SessionID : useridInSession;
  
        List<Cart_Prod> CartList = CartDAO.GetAllCart(custId);
        
        return CartList;
        
    }


    /// Update quantity

    [HttpPost]
    public IActionResult UpdateQuantity(string tranId, string prodId, int quantity)
    {
        String SessionID = FindSessionId.GetSessionId(Request, Response);
        tranId = tranId == null ? SessionID + "T" : tranId;

        using (MySqlConnection conn = new MySqlConnection(connectionString))
        {
            conn.Open();
            string sql = @"UPDATE purchases SET ProdQuantity = @Quantity WHERE TranID = @TranId AND ProdId=@ProdId";
            MySqlCommand cmd = new MySqlCommand(sql, conn);
            cmd.Parameters.AddWithValue("@Quantity", quantity);
            cmd.Parameters.AddWithValue("@TranId", tranId);
            cmd.Parameters.AddWithValue("@ProdId", prodId);
            cmd.ExecuteNonQuery();
            conn.Close();
        }

        return Ok();
    }


    //Remove product
    [HttpPost]
    public IActionResult RemoveProduct(string tranId, string prodId)
    {
        String SessionID = FindSessionId.GetSessionId(Request, Response);
        tranId = tranId == null ? SessionID + "T" : tranId;

        using (MySqlConnection conn = new MySqlConnection(connectionString))
        {
            conn.Open();
            string sql = @"DELETE FROM purchases WHERE TranID = @TranId and ProdId = @prodId";
            MySqlCommand cmd = new MySqlCommand(sql, conn);
            cmd.Parameters.AddWithValue("@TranId", tranId);
            cmd.Parameters.AddWithValue("@prodId", prodId);

            cmd.ExecuteNonQuery();
            conn.Close();
        }
        return Ok();
    }


    //Get Total Amount in Cart
    public IActionResult GetCartTotalSum()
    {
        var cartItems = GetAllCart();
        var totalSum = cartItems.Sum(item => item.Price * item.ProdQuantity);
        return Json(totalSum);
       
    }
}