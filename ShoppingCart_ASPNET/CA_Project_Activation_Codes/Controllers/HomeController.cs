using CA_Project_Activation_Codes.Data;
using CA_Project_Activation_Codes.Models;
using Microsoft.AspNetCore.Mvc;
using System.Diagnostics;

namespace CA_Project_Activation_Codes.Controllers
{
    public class HomeController : Controller
    {

        public IActionResult Index(string username, string password)
        {
            // Everytime cutomer enter the login page reset loginStatus into 'notLogged'
            // so that the 'Logout' button at the banner can be hidden.
            ViewData["loginValid"] = "hide";
            ViewData["LogoutStatus"] = "hide";
            ViewData["LoginStatus"] = "hide"; 

            ISession sessionObject = HttpContext.Session;
            // get session username
            string? usernameInSession = sessionObject.GetString("username");
            if (usernameInSession != null)
            {
                // customer has looged in 
                // redirect to product list page
                return RedirectToAction("Index", "Gallery");
            }
            // cusotmer entered username && password
            if (username != null && password != null)
            {

                // password validation
                if (ValidateCustomer.CheckPassword(username, password) != null)
                {
                    //Console.WriteLine("Login");
                    string? userId = ValidateCustomer.CheckPassword(username, password);
                    // customer login successfully
                    // use session setstring to store the current username in the sessions 
                    sessionObject.SetString("username", username);
                    sessionObject.SetString("userid", userId);

                    ////get TranID
                    String SessionID = FindSessionId.GetSessionId(Request, Response);
                    string getTranId = baseDAO.getTranID(userId);

                    // Push Session List Into Cust List
                    List<string> CustList= baseDAO.checkCart_ProdType(userId);
                    List<string> SessionList = baseDAO.checkCart_ProdType(SessionID);

                    //Transfer Outside Cart to User Cart
                    baseDAO.compareCart_ProdType(userId, getTranId, CustList, SessionID, SessionList);


                    ViewData["loginStatus"] = "valid";
                    return RedirectToAction("Index", "Gallery");
                }
                else
                {
                    ViewData["loginValid"] = "display";
                }
            }

            string? useridInSession = sessionObject.GetString("userid");
            String checkSessionID = FindSessionId.GetSessionId(Request, Response);
            string custId = useridInSession == null ? checkSessionID : useridInSession;

            int cartItemCount = HttpContext.Session.GetInt32("CartItemCount") ?? 0;
            ViewBag.CartItemCount = baseDAO.getCartCount(custId);

            return View();
        }

        public IActionResult Logout()
        {
            ISession sessionObject = HttpContext.Session;
            // clear all the sessions
            sessionObject.Remove("username");
            sessionObject.Remove("userid");

            ViewData["loginStatus"] = "display";
            ViewData["logoutStatus"] = "hide";
            sessionObject.Clear();
            return RedirectToAction("Index", "Home");
        }


        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}
